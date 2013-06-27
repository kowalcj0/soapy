# Soapy using Groovy and Pico
Soapy is a utility library for SoapUI.


## Installation notes

### SoapUI free
* build the jar
* copy the jar to soapui\_folder/bin/ext

### SoapUI Pro
If you don't plan to add / modify scripts, then follow the same instructions as
for SoapUI Free.  
In case you'd like to fiddle with the code, go to:
* Preferences
* SoapUI Pro
* in 'Script Library' point at: soapy\_folder/src/main/groovy folder
* copy all the jars from the soapy\_folder/lib to soapui\_pro\_dir/bin/ext
* restart SoapUI Pro
* check in soapUI log tab that all the jars were added to classpath and script library points at the right directory


## Build the soapy jar from sources

This will run the build and run all the tests
```bash
    mvn install
```
or
```bash
    mvn clean install
```

At the moment this project has just few unit tests written in a BDD manner.
We're using JBehave to run all the stories. You can find story files in: 
```
    soapy/src/main/resources/com/yelllabs/stories
```
All the tests are executed automaticall during the build. 
In case you'd like to run them selectively use:
```bash
    mvn install -DstoryFilter=i_can_parse_json_array
    mvn install -DstoryFilter=i_can_parse_json_value
```bash


## Generate documentation
This project comes with JavaDoc style documentation available in the doc folder.
If you'd like to build docs after changing something, simply run:
```bash
    mvn package -P groovy-docs
```
Newly generated docs should be available in docs folder.

## Including Xeger lib

Unfortunately Xeger is not available (yet) in any public M2 repo.
That's why we need to install it manually to our local repo to avoid using
system scope dependencies in you pom.
More info at: [http://stackoverflow.com/questions/3642023/having-a-3rd-party-jar-included-in-maven-shaded-jar-without-adding-it-to-local-r](http://stackoverflow.com/questions/3642023/having-a-3rd-party-jar-included-in-maven-shaded-jar-without-adding-it-to-local-r)

Download latest build of Xeger from: [http://code.google.com/p/xeger/](http://code.google.com/p/xeger/)

Then install it to your local Maven repo using:
```bash
    cd to_soapy_project
    mvn install:install-file -Dfile=libs/xeger-1.0-SNAPSHOT.jar -DgroupId=nl.flotsam -DartifactId=xeger -Dversion=1.0-SNAPSHOT -Dpackaging=jar
```
