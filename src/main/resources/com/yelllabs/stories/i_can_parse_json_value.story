Parse JSON value

Meta:
@category basic
@color red

Narrative:
In order to parse JSON structures
I want to be able to parse an JSON value
A value can be a string in double quotes, or a number, or true or false or null, or an object or an array. These structures can be nested.
JSON-Simple library doesn't differentiate JSON objects or arrays as values, so they're not used in examples
For more details please refer to: http://code.google.com/p/tests.json-simple/source/browse/branches/mavenization/src/main/java/org/tests.json/simple/parser/JSONParser.java

Scenario: Parse successfully JSON value
Given a JSON value: <jsonStringValue>
Then I should get an JSON value object


Examples:
|jsonStringValue                        |
|"string"                               |
|1                                      |
|-1445                                  |
|true                                   |
|false                                  |
|null                                   |