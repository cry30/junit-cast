#!/bin/bash

# Will print the current build number by parsing the CHANGELOG.md. This script
# is used in the Makefile when building the project.

echo $(grep -E '^v\d+.*' CHANGELOG.md | sed -n '1p' | awk -F" - " '{print $1}' | cut -c 2-)
