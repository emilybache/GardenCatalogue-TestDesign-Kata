package org.sammancoaching;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GardenManagementTests {

    @Test
    public void testLongScenario() {
        GardenCatalogueManager manager = new GardenCatalogueManager();
        
        // Add some plants
        manager.addPlant(new Plant("Lavender", "Lavandula", "FLO-001", PlantType.Flower, BloomPeriod.fromRange(Month.June, Month.August), 0.6, SoilCondition.Sandy, LightCondition.FullSun, java.util.Map.of()));
        manager.addPlant(new Plant("Rose", "Rosa", "BSH-001", PlantType.Bush, BloomPeriod.fromRange(Month.June, Month.September), 1.5, SoilCondition.Loamy, LightCondition.FullSun, java.util.Map.of()));
        
        // Check count
        assertEquals(2, manager.getPlantCount());
        
        // Find by name
        Plant p = manager.getPlantByName("Lavender");
        assertNotNull(p);
        assertEquals("Lavandula", p.latinName());
        
        // Plan bed
        List<Plant> bed = manager.planBed(Month.July, 1.0);
        assertEquals(1, bed.size());
        assertEquals("Lavender", bed.get(0).name());
        
        // Plan hedge
        assertEquals(1, manager.planHedge(1.0).size());
        
        // Add a tree
        manager.addPlant(new Plant("Oak", "Quercus", "TRE-001", PlantType.Tree, new BloomPeriod(List.of()), 20.0, SoilCondition.Any, LightCondition.FullSun, java.util.Map.of()));
        
        // Check count again
        assertEquals(3, manager.getPlantCount());
        
        // Plan hedge again
        List<Plant> hedge = manager.planHedge(10.0);
        assertEquals(1, hedge.size());
        assertEquals("Oak", hedge.get(0).name());
        
        // Filter by conditions
        List<Plant> sunnyPlants = manager.getPlantsForCondition(SoilCondition.Sandy, LightCondition.FullSun);
        // Lavender is Sandy/FullSun. Oak is Any/FullSun.
        assertEquals(2, sunnyPlants.size());
    }
}
