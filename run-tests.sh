EXIT_CODE=0
for folder in python java ; do 
    echo $folder
    cd $folder
    ./run-tests.sh
    if [ $? -ne 0 ] 
    then
        EXIT_CODE=1
    fi
    cd ..
done
exit $EXIT_CODE
