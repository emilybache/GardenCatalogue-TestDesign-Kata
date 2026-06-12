package org.sammancoaching;

import org.approvaltests.Approvals;
import org.approvaltests.scrubbers.RegExScrubber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class GardenManagerApprovalTests {
    private GardenCatalogueManager manager;

    @BeforeEach
    public void setup() {
        manager = new GardenCatalogueManager(TestData.DEFAULT_PLANTS);
    }

    @Test
    public void testHedgePlanning() {
        List<Plant> hedge = manager.planHedge(2.0);
        Approvals.verifyAll("", hedge);
    }

    @Test
    public void testConditionFiltering() {
        List<Plant> plants = manager.getPlantsForCondition(SoilCondition.Sandy, LightCondition.FullSun);
        Approvals.verifyAll("", plants);
    }

    @Test
    public void testBedPlanning() {
        List<Plant> bedInJune = manager.planBed(Month.June, 2.0);
        Approvals.verifyAll("", bedInJune);
    }
}
