package org.sammancoaching;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GardenManagementTests {

    @Test
    public void ManagingCatalogueScenario() {
        GardenCatalogueManager manager = new GardenCatalogueManager(TestData.defaultPlants());

        assertEquals(9, manager.getPlantCount());

        manager.addPlant(new PlantBuilder().withName("Tulip").withLatinName("Tulipa").withArticleNumber("FLO-004").withBloomPeriod(BloomPeriod.fromRange(Month.April, Month.May)).withMaxHeight(0.4).withSoil(SoilCondition.Loamy).withLight(LightCondition.FullSun).build());

        assertEquals(10, manager.getPlantCount());
        assertNotNull(manager.getPlantByName("Tulip"));

        List<Plant> springBed = manager.planBed(Month.April, 0.5);

        assertEquals(1, springBed.size());
        assertEquals("Tulip", springBed.get(0).name());

        List<Plant> privacyHedge = manager.planHedge(1.5);

        assertEquals(5, privacyHedge.size());
        assertTrue(privacyHedge.stream().anyMatch(p -> p.name().equals("Rose")));
        assertTrue(privacyHedge.stream().anyMatch(p -> p.name().equals("Beech")));

        List<Plant> shadePlants = manager.getPlantsForCondition(SoilCondition.Loamy, LightCondition.FullShade);

        assertEquals(1, shadePlants.size());
        assertEquals("Fern", shadePlants.get(0).name());
    }
}
