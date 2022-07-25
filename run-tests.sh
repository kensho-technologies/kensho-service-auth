EXIT_CODE=0
for folder in python java js ; do 
    echo $folder
    cd $folder
    ./run-tests.sh
    if [ $? -eq 1 ] 
    then
        EXIT_CODE=1
    fi
    cd ..
done
exit $EXIT_CODE
