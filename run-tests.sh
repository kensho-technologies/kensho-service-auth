for folder in python java ; do 
    echo $folder
    cd $folder
    ./run-tests.sh
    cd ..
done
