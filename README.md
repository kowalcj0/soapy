# Soapy using Groovy and Pico

## Running the stories

This will run the build and run all the tests

    mvn install
    or
    mvn clean install

To run a single story (one contained in a etsy_cart.story file):

    mvn install -DstoryFilter=i_can_parse_json_array
    mvn install -DstoryFilter=i_can_parse_json_value

## Building documentation
To build GroovyDocs for this project, simply run this profile:
mvn package -P groovy-docs 

## Including Xeger lib

Unfortunately Xeger is not available (yet) in any public M2 repo.
That's why we need to install it manually to our local repo to avoid using
system scope dependencies in you pom.
More info at:
http://stackoverflow.com/questions/3642023/having-a-3rd-party-jar-included-in-maven-shaded-jar-without-adding-it-to-local-r

Download latest build of Xeger from: http://code.google.com/p/xeger/

Then install it to your local Maven repo using:
mvn install:install-file -Dfile=/full/path/to/xeger/jar/file/xeger-1.0-SNAPSHOT.jar -DgroupId=nl.flotsam -DartifactId=xeger -Dversion=1.0-SNAPSHOT -Dpackaging=jar
