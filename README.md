# Soapy using Groovy and Pico

## Running the stories

This will run the build and run all the tests

    mvn install
    or
    mvn clean install

To run a single story (one contained in a etsy_cart.story file):

    mvn install -DstoryFilter=i_can_parse_json_array
    mvn install -DstoryFilter=i_can_parse_json_value