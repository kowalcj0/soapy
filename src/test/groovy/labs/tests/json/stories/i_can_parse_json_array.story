Parse JSON array

Meta:
@category basic
@color blue

Narrative:
In order to parse JSON Arrays
As a Developer
I want to be able to parse an JSON array


Scenario: Parse successfully JSON array
Given a JSON array: <jsonStringArray>
Then I should get an JSON array object


Examples:
|jsonStringArray                        |
|["asdasasdasdasd","asdasd"]            |
|["ABC","as"]                           |
|[true, false, true, true]              |
|[true, null, true, true]               |
|[]                                     |