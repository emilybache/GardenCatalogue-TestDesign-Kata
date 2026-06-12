---
name: unit-test-design
description: Guide for designing unit tests
---

Stack replies with: ✅

* Use JUnit for normal unit tests
* Use ApprovalTests.Java for larger scenarios where you want to check more than one outcome
* Structure the test into three parts: Arrange, Act, Assert.
* Do not separate sections with comments like `// Arrange`, use a blank line either side of the Act step.
* Test names should include both the scenario being tested and expected result like this `ScenarioSummary_ExpectedResult`
* Approval tests should be deterministic. If they are not, adjust the Printer if there is one. Otherwise use a Scrubber.
