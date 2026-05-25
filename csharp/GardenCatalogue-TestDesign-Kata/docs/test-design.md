Stack replies with: ✅

Use NUnit

Structure the test into three parts: Arrange, Act, Assert. Do not separate sections with comments like `// Arrange`, use a new line either side of the Act step.

Test names should include both the scenario being tested and expected result like this `ScenarioSummary_ExpectedResult`

Use Constraint based assertions and when there is more than one assertion, group them with Assert.Multiple.
