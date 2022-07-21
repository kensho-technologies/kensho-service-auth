
## Setup Options

*Java version must be at least 15*

1. Run the jar file.

```
$ java -cp kensho_auth.jar com.example.App <clientid> <privatekeyfilename> <scope>
```

Or

1. Run mvn package in auth folder.

```
$ cd auth
$ mvn clean
$ mvn package
```

2. Run the jar file with dependencies in the created target folder.

```
$ java -cp target/auth-0.1-jar-with-dependencies.jar com.example.App <clientid> <privatekeyfilename> <scope>
```


