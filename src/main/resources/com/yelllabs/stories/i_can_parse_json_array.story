Parse JSON array

Meta:
@category basic
@color blue

Narrative:
In order to parse JSON structures
I want to be able to parse an JSON array
An array is an ordered collection of values. An array begins with [ (left bracket) and ends with ] (right bracket). Values are separated by , (comma).

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