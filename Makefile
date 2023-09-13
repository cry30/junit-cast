CURRENT_BUILD=$(shell scripts/set_build_number.sh)

help:
	@echo "make -s test - run unit tests"
	@echo "make -s version-get - Displays the current version number by extracting it from the CHANGELOG.md"
	@echo "make -s version-set - display the current artifact version"
	@echo "make -s version-commit - Deletes the version backup file."
	@echo "make -s clean - delete generated files"
	@echo "make -s verify - run tests and check javadocs"
	@echo "make -s release - release to maven central"
	@echo "-s option hides the Make invocation command (@echo)."

clean:
	mvn clean
	rm -rf target/
	rm -rf bin/

build:
	mvn package

test:
	mvn test


version-get:
	./scripts/set_build_number.sh


version-set:
	mvn versions:set -DnewVersion=${CURRENT_BUILD}

version-commit:
	mvn versions:commit

verify:
	mvn verify

release:
	mvn clean deploy -P release

.PHONY: help test init clean run version-set version-get release verify
