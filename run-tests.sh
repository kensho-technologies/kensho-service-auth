#!/bin/bash
EXIT_CODE=0
read -p "Please enter CLIENT ID: " CLIENT_ID
read -p "Please enter scopes (separated by spaces): " SCOPES
export CLIENT_ID
export SCOPES
for directory in $(ls -d $(dirname $0)/*/); do
    echo $directory
    ./$directory/run-tests.sh
    if [ $? -ne 0 ]
    then
        EXIT_CODE=1
    fi
done
exit $EXIT_CODE
