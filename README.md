Garden Catalogue Test Design Kata
=================================

This application manages a catalogue of garden plants with information about when they flower, how big they grow, and the soil and light conditions in which they thrive. There is functionality to plan a bed of flowers, a hedge or background greenery.

There are several test design issues you could look at, all of which will make interpreting failures more challenging:

1) The test setup is duplicated and a little unclear in the GardenManagerTests.
2) Which scenario is being tested is unclear in GardenManagerApprovalTests.
3) PlantDataParserTests contain a lot of assertions.
4) There is a rather long scenario in GardenManagementTests that tests several things.


Some language versions contain scripts that you can use to insert bugs so you can get some test failures and find out whether your design changes have helped.

