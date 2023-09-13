package junitcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.github.roycetech.ruleengine.Rule;
import com.github.roycetech.ruleengine.converter.ElementConverter;
import com.github.roycetech.ruleengine.converter.StringConverter;

import junitcast.util.RuleUtil;
import junitcast.util.StringUtil;

/**
 * TODO: Re-design to simplify.
 *
 * This uses resource bundle to configure test case. REVIEW: Lasagne code.
 *
 * @author Royce Remulla
 */
@SuppressWarnings({ "PMD.GodClass", "PMD.TooManyMethods" })
public class ResourceFixture {

	/** Set of Cases. */
	private final transient Set<String> casesSet = new LinkedHashSet<>();

	/**
	 * Cases (OAS), Variables (Value Presence, Duplication, etc), Combinations (No
	 * Value, Has Value).
	 */
	private final transient List<List<List<Object>>> caseVarList = new ArrayList<>();

	/** */
	private final transient List<List<ElementConverter>> caseConverterList = new ArrayList<>();

	/** Case to List of Rules. */
	private final transient List<String> ruleList = new ArrayList<>();

	/** */
	private final transient List<Map<String, ElementConverter>> ruleTokConverter = new ArrayList<>();

	/** Case to List of Attributes. */
	private final transient List<List<String>> attrList = new ArrayList<>();

	/** Cases index, Set of exempt rules. */
	private final transient Map<Integer, String> caseExemptMap = new ConcurrentHashMap<>();

	/** Pair mapping for binary cases. */
	private final transient Map<Integer, String> listPairMap = new ConcurrentHashMap<>();

	/** Can be set in resource to skip prior indexes to speed up testing. */
	private transient int debugStart;

	/** Converter map. */
	private static Map<Class<? extends ElementConverter>, ElementConverter> convertMap = new ConcurrentHashMap<>();

	/** */
	private final transient String resourceUri;

	/** Resource file key prefix. */
	private enum ResourceKey {
		/** */
		casedesc, var, rule, pair,

		/** */
		caseId, exempt,

		/** */
		commonexempt, commonvar,

		/** */
		converter, debug_index;
	}

	/**
	 * @param pResource resource uri.
	 */
	public ResourceFixture(final String pResource) {
		this.resourceUri = pResource;
	}

	/**
	 * Generate case fixtures. There is a tricky behavior when properties file are
	 * name similar with a class file, @see {@link ResourceBundle}. We will attempt
	 * to retry to work around this. The error happens only on the first try when
	 * running ant from the windows command line.
	 */
	/* default */ void generateCases()
	{
		final ResourceBundle resBundle = ResourceBundle.getBundle(this.resourceUri,
				Locale.getDefault(),
				ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
		initCases(resBundle);
		initVars(resBundle);
		initRules(resBundle);
		initIdentifier(resBundle);
		initExempt(resBundle);
		initPair(resBundle);
	}

	/** */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> getFixtures()
	{
		generateCases();
		final List<CaseFixture<String>> retval = new ArrayList<>(getCaseList().size());

		for (int index = 0; index < getCaseList().size(); index++) {
			final String caseDesc = getCaseList().toArray(new String[getCaseList().size()])[index];
			final List<List<Object>> variables = getVarList().get(index);

			final Rule rule = new Rule(RuleUtil.parseRuleDefinition(getRuleList().get(index)));
			final String pair = this.listPairMap.get(index);
			final String exempt = getCaseExemptMap().get(index);
			final List<String> caseId = getAttrList().get(index);
			retval.add(new CaseFixture(caseDesc, variables, rule).pair(pair).exempt(exempt)
					.caseIdentifier(caseId).convert(this.caseConverterList.get(index))
					.ruleConverter(this.ruleTokConverter.get(index)));
		}
		return retval;
	}

	/**
	 * Initialize cases from casedesc key in property file.
	 *
	 * @param resBundle resource bundle instance.
	 */
	/* default */ final void initCases(final ResourceBundle resBundle)
	{
		if (resBundle.containsKey(ResourceKey.debug_index.name())) {
			final String debugStartStr = resBundle.getString(ResourceKey.debug_index.name()).trim();
			try {
				this.debugStart = Integer.valueOf(debugStartStr);
			} catch (final NumberFormatException e) {
				this.debugStart = 0;
			}
		} else {
			this.debugStart = 0;
		}

		int caseIndex = this.debugStart;
		while (true) {
			final String key = ResourceKey.casedesc.name() + caseIndex++;
			if (resBundle.containsKey(key)) {
				final String kaso = resBundle.getString(key);
				getCaseList().add(kaso.trim());
			} else {
				break;
			}
		}
	}

	/**
	 * Initialize variables.
	 *
	 * @param resBundle resource bundle instance.
	 */
	/* default */ void initVars(final ResourceBundle resBundle)
	{
		List<List<Object>> commonVars;
		if (resBundle.containsKey(ResourceKey.commonvar.name())) {
			commonVars = fetchVariables(resBundle, -1, ResourceKey.commonvar.name(), ",");
		} else {
			commonVars = new ArrayList<>();
		}

		for (int i = 0; i < getCaseList().size(); i++) {

			final int actualIdx = i + this.debugStart;
			final String varkey = ResourceKey.var.name() + actualIdx;
			final String convertkey = ResourceKey.converter.name() + actualIdx;

			String converters = null; // NOPMD: null default, conditionally redefine.
			if (resBundle.containsKey(convertkey)) {
				converters = resBundle.getString(convertkey);
			}

			this.ruleTokConverter.add(new HashMap<String, ElementConverter>()); // NOPMD: False
																				// Positive.
			final List<List<Object>> caseVariables = fetchVariables(resBundle, i, varkey, ",",
					converters);

			final Set<List<Object>> specificVars = new LinkedHashSet<List<Object>>(); // NOPMD:
																						// False
																						// Positive.
			caseVariables.addAll(commonVars);

			caseVariables.addAll(specificVars);
			getCaseVarList().add(caseVariables);
		}
	}

	/**
	 * @param resBundle resource bundle instance.
	 * @param caseIndex case index.
	 * @param key       resource key.
	 * @param separator values separator.
	 */
	/* default */ List<List<Object>> fetchVariables(final ResourceBundle resBundle,
			final int caseIndex, final String key, final String separator)
	{
		return fetchVariables(resBundle, caseIndex, key, separator, null);
	}

	/**
	 * @param resBundle  resource bundle instance.
	 * @param caseIndex  case index.
	 * @param key        resource key.
	 * @param separator  values separator.
	 * @param converters element type converter.
	 */
	/* default */ List<List<Object>> fetchVariables(final ResourceBundle resBundle,
			final int caseIndex, final String key, final String separator, final String converters)
	{

		List<List<Object>> retval;
		if (resBundle.containsKey(key)) {
			final String commonVarRaw = resBundle.getString(key);
			retval = extractCombinations(caseIndex, commonVarRaw, separator, converters);
		} else {
			retval = new ArrayList<>();
		}
		return retval;
	}

	/**
	 * TODO: Make smarter to use same converter if variable set is equal.
	 *
	 * @param caseIndex    case index.
	 * @param commonVarRaw common variable in raw form.
	 * @param separator    values separator.
	 * @param converters   element type converter.
	 */
	/* default */ List<List<Object>> extractCombinations(final int caseIndex,
			final String commonVarRaw, final String separator, final String converters)
	{
		final String[] rawGroup = StringUtil.trimArray(commonVarRaw.split("\\|"));

		String[] converterArr;
		if (StringUtil.hasValue(converters)) {
			converterArr = converters.split("\\|");
		} else {
			converterArr = new String[rawGroup.length];
			Arrays.fill(converterArr, StringConverter.class.getName());
		}

		final List<ElementConverter> elConvList = new ArrayList<>();
		this.caseConverterList.add(elConvList);
		final List<List<Object>> caseComb = new ArrayList<>();
		for (int i = 0; i < rawGroup.length; i++) {
			final String nextGroup = rawGroup[i];

			final ElementConverter elConvert = getConverter(converterArr[i]);
			elConvList.add(elConvert);

			if (!"".equals(nextGroup)) {
				final String[] nextGroupArr = StringUtil.trimArray(nextGroup.split(separator));
				if (caseIndex > -1) { // TODO: Unsupported typed common variables.
					for (final String string : nextGroupArr) {
						final Map<String, ElementConverter> ruleTokenMap = this.ruleTokConverter
								.get(caseIndex);
						ruleTokenMap.put(string, elConvert);
					}
				}

				caseComb.add(new ArrayList<>(convert(nextGroupArr, elConvert)));
			}
		}
		return caseComb;
	}

	/**
	 * @param converterClsName converter class name.
	 */
	@SuppressWarnings("unchecked")
	/* default */ synchronized ElementConverter getConverter(final String converterClsName)
	{
		Class<ElementConverter> converterCls;
		try {
			converterCls = (Class<ElementConverter>) Class.forName(converterClsName);
		} catch (final ClassNotFoundException e1) {
			throw new ResourceFixtureException("Cannot find converter class: " + converterClsName,
					e1);
		}

		if (convertMap.get(converterCls) == null) {
			try {
				convertMap.put(converterCls,
						converterCls.getDeclaredConstructor(new Class[0]).newInstance());
			} catch (final Exception e) {
				throw new ResourceFixtureException("Error instantiating converter: " + converterCls,
						e);

			}
		}
		return convertMap.get(converterCls);
	}

	/**
	 * @param nextGroupArr next group array.
	 * @param converter    element converter.
	 */
	/* default */ List<Object> convert(final String[] nextGroupArr,
			final ElementConverter converter)
	{
		final List<Object> retval = new ArrayList<>();
		for (final String string : nextGroupArr) {
			retval.add(converter.convert(string));
		}
		return retval;
	}

	/**
	 * Initialize rules from resource bundle.
	 *
	 * @param resBundle resource bundle instance.
	 */
	/* default */ void initRules(final ResourceBundle resBundle)
	{
		for (int i = 0; i < getCaseList().size(); i++) {
			final int actualIdx = i + this.debugStart;

			final String ruleRaw = resBundle.getString(ResourceKey.rule.name() + actualIdx);
			getRuleList().add(ruleRaw);
		}
	}

	/** @param resBundle resource bundle instance. */
	/* default */ void initIdentifier(final ResourceBundle resBundle)
	{
		for (int i = 0; i < getCaseList().size(); i++) {

			final int actualIdx = i + this.debugStart;
			final String key = ResourceKey.caseId.name() + actualIdx;
			if (resBundle.containsKey(key) && !"".equals(resBundle.getString(key).trim())) {
				final String raw = resBundle.getString(key);
				getAttrList().add(Arrays.asList(StringUtil.trimArray(raw.split(","))));
			} else {
				final String caseId = getCaseList().toArray(new String[getCaseList().size()])[i];
				getAttrList().add(Arrays.asList(new String[] { caseId }));
			}
		}
	}

	/**
	 * This will also include common exclusions for all.
	 *
	 * @param resBundle resource bundle instance.
	 */
	/* default */ void initExempt(final ResourceBundle resBundle)
	{
		String commonExempt = null; // NOPMD: null default, conditionally redefine.
		if (resBundle.containsKey(ResourceKey.commonexempt.name())) {
			commonExempt = resBundle.getString(ResourceKey.commonexempt.name());
		}

		for (int i = 0; i < getCaseList().size(); i++) {
			final StringBuilder exemptRule = new StringBuilder();

			final int actualIdx = i + this.debugStart;

			final String key = String.valueOf(ResourceKey.exempt) + actualIdx;
			String caseExempt = null; // NOPMD: null default, conditionally redefine.
			if (resBundle.containsKey(key)) {
				caseExempt = resBundle.getString(key);
			}

			if (StringUtil.hasValue(commonExempt) && StringUtil.hasValue(caseExempt)) {
				exemptRule.append(String.format("(%s)|%s", commonExempt, caseExempt));
			} else if (StringUtil.hasValue(caseExempt)) {
				exemptRule.append(caseExempt);
			} else if (StringUtil.hasValue(commonExempt)) {
				exemptRule.append(commonExempt);
			}

			if (StringUtil.hasValue(exemptRule.toString())) {
				getCaseExemptMap().put(i, exemptRule.toString());
			}
		}
	}

	/**
	 * Initialize pair in binary rules.
	 *
	 * @param resBundle resource bundle instance.
	 */
	/* default */ void initPair(final ResourceBundle resBundle)
	{
		for (int i = 0; i < getCaseList().size(); i++) {
			final String key = ResourceKey.pair.name() + i;
			if (resBundle.containsKey(key)) {
				final String pairRaw = resBundle.getString(key);
				this.listPairMap.put(i, pairRaw.trim());
			}
		}

	}

	/**
	 * @return the caseList
	 */
	public Set<String> getCaseList()
	{
		return this.casesSet;
	}

	/**
	 * @return the varList
	 */
	public List<List<List<Object>>> getVarList()
	{
		return getCaseVarList();
	}

	/**
	 * @return the caseVarList
	 */
	public List<List<List<Object>>> getCaseVarList()
	{
		return this.caseVarList;
	}

	/**
	 * @return the ruleList
	 */
	public List<String> getRuleList()
	{
		return this.ruleList;
	}

	/**
	 * @return the attrList
	 */
	public List<List<String>> getAttrList()
	{
		return this.attrList;
	}

	/**
	 * @return the caseExempMap
	 */
	public Map<Integer, String> getCaseExemptMap()
	{
		return this.caseExemptMap;

	}

	/**
	 * @return the resource
	 */
	public String getResourceUri()
	{
		return this.resourceUri;
	}

}

/** Custom class exception. */
class ResourceFixtureException extends RuntimeException {
	private static final long serialVersionUID = 3996097978039160817L;

	/** @param string exception message. */
	/* default */ ResourceFixtureException(final String string, final Throwable cause) {
		super(string, cause);
	}
}
