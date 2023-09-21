package io.github.roycetech.junitcast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.github.roycetech.ruleengine.Rule;
import com.github.roycetech.ruleengine.converter.ElementConverter;
import com.github.roycetech.ruleengine.converter.StringConverter;
import com.github.roycetech.ruleengine.utils.StringUtil;

import io.github.roycetech.junitcast.initializer.CasesInitializer;
import io.github.roycetech.junitcast.initializer.ExemptInitializer;
import io.github.roycetech.junitcast.initializer.IdentifierInitializer;
import io.github.roycetech.junitcast.initializer.VariablesInitializer;
import io.github.roycetech.junitcast.util.RuleUtil;

/**
 * This uses resource bundle to configure test case.
 */
@SuppressWarnings({ "PMD.GodClass", "PMD.TooManyMethods" })
public class ResourceFixture {

	/**
	 * Set of Cases.
	 */
	private final transient Set<String> casesSet = new LinkedHashSet<>();

	/**
	 * Cases (OAS), Variables (Value Presence, Duplication, etc), Combinations (No
	 * Value, Has Value).
	 */
	private final transient List<List<List<Object>>> caseVarList = new ArrayList<>();

	/**
	 *
	 */
	private final transient List<List<ElementConverter>> caseConverterList = new ArrayList<>();

	/**
	 * Case to List of Rules.
	 */
	private final transient List<String> ruleList = new ArrayList<>();

	/**
	 *
	 */
	private final transient List<Map<String, ElementConverter>> ruleTokenConverter = new ArrayList<>();

	/**
	 * Case to List of Attributes.
	 */
	private final transient List<List<String>> attrList = new ArrayList<>();

	/**
	 * Cases index, Set of exempt rules.
	 */
	private final transient Map<Integer, String> caseExemptMap = new ConcurrentHashMap<>();

	/**
	 * Pair mapping for binary cases.
	 */
	private final transient Map<Integer, String> listPairMap = new ConcurrentHashMap<>();

	/**
	 * Can be set in resource to skip prior indexes to speed up testing.
	 */
	private transient int debugStart;

	/**
	 * Converter map.
	 */
	private static final Map<Class<? extends ElementConverter>, ElementConverter> convertMap = new ConcurrentHashMap<>();

	/**
	 * The configuration to be parsed by this instance.
	 */
	private final transient ResourceBundle resourceBundle;

	/**
	 * Resource file key prefix.
	 */
	public enum ResourceKey {
		/**
		 * Case description configuration key.
		 */
		casedesc,

		/**
		 * Variables configuration key.
		 */
		var,

		/**
		 * Rules configuration key.
		 */
		rule,

		/**
		 * Optional binary result configuration key.
		 */
		pair,

		/**
		 * Optional case ID configuration key.
		 */
		caseId,

		/**
		 * Optional exempt configuration key.
		 */
		exempt,

		/**
		 * Optional common exempt configuration key.
		 */
		commonexempt,

		/**
		 * Optional common variables configuration key.
		 */
		commonvar,

		/**
		 * Optional converters configuration key.
		 */
		converter,

		/**
		 * Optional configuration key for the starting test case for debugging.
		 */
		debug_index
	}

	/**
	 * Constructs a new ResourceFixture instance.
	 *
	 * @param resourceUri The URI of the resource to load. If null, the
	 *                    resourceBundle will be set to null. Otherwise, it attempts
	 *                    to load a resource bundle using the provided URI.
	 */
	public ResourceFixture(final String resourceUri) {
		if (resourceUri == null) {
			this.resourceBundle = null;
		} else {
			this.resourceBundle = ResourceBundle.getBundle(resourceUri, Locale.getDefault(),
					ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));
		}
	}

	/**
	 * Generate case fixtures. There is a tricky behavior when properties file are
	 * name similar with a class file, @see {@link ResourceBundle}. We will attempt
	 * to retry to work around this. The error happens only on the first try when
	 * running ant from the windows command line.
	 */
	/* default */ void generateCases()
	{
		new CasesInitializer(this).initialize();
		new VariablesInitializer(this).initialize();
		initRules();
		new IdentifierInitializer(this).initialize();
		new ExemptInitializer(this).initialize();
		initPair();
	}

	/**
	 * Retrieves the list of test fixtures.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<?> getFixtures()
	{
		generateCases();
		final List<CaseFixture<String>> fixtureList = new ArrayList<>(getCaseList().size());
		for (int index = 0; index < getCaseList().size(); index++) {
			final String caseDesc = getCaseList().toArray(new String[0])[index];
			final List<List<Object>> variables = getCaseVarList().get(index);

			final Rule rule = new Rule(RuleUtil.parseRuleDefinition(getRuleList().get(index)));
			final String pair = this.listPairMap.get(index);
			final String exempt = getCaseExemptMap().get(index);
			final List<String> caseId = getAttrList().get(index);

			// @formatter:off
			fixtureList.add(new CaseFixture(caseDesc, variables, rule)
				.pair(pair).exempt(exempt)
				.caseIdentifier(caseId)
				.convert(this.caseConverterList.get(index))
				.ruleConverter(this.ruleTokenConverter.get(index)));
			// @formatter:on
		}
		return fixtureList;
	}

	/**
	 * Gets a string for the given key from this resource bundle. This method is
	 * created for test-ability.
	 * @param key the resource key.
	 * @return the string for the given key
	 */
	public String getResourceString(final String key)
	{
		return getResourceBundle().getString(key);
	}

	/**
	 * Makes the resourceBundle available for testing.
	 *
	 * @return the resource bundle instance.
	 */
	public ResourceBundle getResourceBundle()
	{
		return this.resourceBundle;
	}

	/**
	 *
	 *
	 * @param caseIndex  case index.
	 * @param key        resource key.
	 * @param separator  values separator.
	 * @param converters element type converter.
	 *
	 * @return all the variables combinations from the configuration.
	 */
	public List<List<Object>> fetchVariables(final int caseIndex, final String key,
			final String separator, final String converters)
	{
		Objects.requireNonNull(key, "key cannot be null");

		if (getResourceBundle().containsKey(key)) {
			final String commonVarRaw = getResourceString(key);
			return extractCombinations(caseIndex, commonVarRaw, separator, converters);
		}

		return new ArrayList<>();
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
		final String[] converterArr = buildConverterArray(converters, rawGroup);

		final List<ElementConverter> elConvList = new ArrayList<>();
		this.caseConverterList.add(elConvList);
		final List<List<Object>> caseComb = new ArrayList<>();
		for (int i = 0; i < rawGroup.length; i++) {
			final String nextGroup = rawGroup[i];
			final ElementConverter elConvert = getConverter(converterArr[i]);
			elConvList.add(elConvert);

			if (!"".equals(nextGroup)) {
				final String[] nextGroupArr = StringUtil.trimArray(nextGroup.split(separator));

				remapConverterByToken(caseIndex, elConvert, nextGroupArr);

				caseComb.add(new ArrayList<>(convert(nextGroupArr, elConvert)));
			}
		}
		return caseComb;
	}

	/**
	 * Refactored out of extractCombinations.
	 *
	 * @param caseIndex        - the next case index.
	 * @param elementConverter - the next elementConverter.
	 * @param groupArray       - not sure what this group is.
	 */
	private void remapConverterByToken(final int caseIndex, final ElementConverter elementConverter,
			final String[] groupArray)
	{
		if (caseIndex > -1) { // TODO: Unsupported typed common variables.
			for (final String nextGroup : groupArray) {
				final Map<String, ElementConverter> ruleTokenMap = this.ruleTokenConverter
						.get(caseIndex);
				ruleTokenMap.put(nextGroup, elementConverter);
			}
		}
	}

	/**
	 * Refactored out of {@link #extractCombinations(int, String, String, String)}
	 *
	 * @param converters - the pipe delimited string representation of the
	 *                   converters.
	 * @param rawGroup   -
	 * @return
	 */
	private String[] buildConverterArray(final String converters, final String[] rawGroup)
	{
		if (StringUtil.hasValue(converters)) {
			return converters.split("\\|");
		}

		final String[] converterArr = new String[rawGroup.length];
		Arrays.fill(converterArr, StringConverter.class.getName());
		return converterArr;
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
	 * Applies the conversion to the designated java object type to each item in the
	 * group array.
	 *
	 * @param groupArray group array.
	 * @param converter  element converter.
	 */
	/* default */ List<Object> convert(final String[] groupArray, final ElementConverter converter)
	{
		return Arrays.stream(groupArray).map(converter::convert).collect(Collectors.toList());
	}

	/**
	 * Initialize pair in binary rules.
	 */
	private void initPair()
	{
		IntStream.range(0, getCaseList().size())
				.filter(i -> getResourceBundle().containsKey(ResourceKey.pair.name() + i))
				.forEach(i -> this.listPairMap.put(i,
						getResourceBundle().getString(ResourceKey.pair.name() + i).trim()));
	}

	/**
	 * Initialize rules from resource bundle.
	 */
	/* default */ void initRules()
	{
		IntStream.range(0, getCaseList().size()).map(i -> i + this.debugStart)
				.mapToObj(i -> getResourceBundle().getString(ResourceKey.rule.name() + i))
				.forEach(getRuleList()::add);
	}

	/**
	 * Gets the set of case descriptions.
	 *
	 * @return A set containing the description of the test cases.
	 */
	public Set<String> getCaseList()
	{
		return this.casesSet;
	}

	/**
	 * Gets the list of variable lists for each test case.
	 *
	 * @return A list containing variable lists for each test case.
	 */
	public List<List<List<Object>>> getCaseVarList()
	{
		return this.caseVarList;
	}

	/**
	 * Gets the list of rule strings for each test case.
	 *
	 * @return A list containing rule strings for each test case.
	 */
	public List<String> getRuleList()
	{
		return this.ruleList;
	}

	/**
	 * Gets the list of attribute lists for each test case.
	 *
	 * @return A list containing attribute lists for each test case.
	 */
	public List<List<String>> getAttrList()
	{
		return this.attrList;
	}

	/**
	 * Gets the mapping of case indices to exemption rules.
	 *
	 * @return A map containing exemption rules associated with specific case
	 *         indices.
	 */
	public Map<Integer, String> getCaseExemptMap()
	{
		return this.caseExemptMap;
	}

	/**
	 * Returns the debugStart property for test-ability purpose.
	 *
	 * @return debugStart property.
	 */
	public int getDebugStart()
	{
		return this.debugStart;
	}

	/**
	 * Sets the value for the debugStart property.
	 *
	 * @param debugStart the debugStart to set
	 */
	public void setDebugStart(final int debugStart)
	{
		this.debugStart = debugStart;
	}

	/**
	 * Returns the rule token converters.
	 *
	 * @return the ruleTokenConverter property
	 */
	public List<Map<String, ElementConverter>> getRuleTokenConverter()
	{
		return this.ruleTokenConverter;
	}
}

/**
 * Custom class exception.
 */
class ResourceFixtureException extends RuntimeException {
	private static final long serialVersionUID = 3996097978039160817L;

	/**
	 * @param string exception message.
	 */
	/* default */ ResourceFixtureException(final String string, final Throwable cause) {
		super(string, cause);
	}
}
