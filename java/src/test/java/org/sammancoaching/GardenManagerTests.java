package org.sammancoaching;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

public class GardenManagerTests {

    @Test
    public void testGetPlantsForSandySoilAndFullSunReturnsLavenderBeechOakAndSilverBirch() {
        GardenCatalogueManager manager = new GardenCatalogueManager(TestData.DEFAULT_PLANTS);
        SoilCondition soil = SoilCondition.Sandy;
        LightCondition light = LightCondition.FullSun;

        List<Plant> result = manager.getPlantsForCondition(soil, light);

        List<String> names = result.stream().map(Plant::name).sorted().toList();
        assertEquals(List.of("Beech", "Lavender", "Oak", "Silver Birch"), names);
    }

    @Test
    public void testPlanBedInJulyWithMaxHeightOneMeterReturnsLavender() {
        GardenCatalogueManager manager = new GardenCatalogueManager(TestData.DEFAULT_PLANTS);
        Month month = Month.July;
        double maxHeight = 1.0;

        List<Plant> result = manager.planBed(month, maxHeight);

        List<String> names = result.stream().map(Plant::name).toList();
        assertEquals(List.of("Lavender"), names);
    }

    @Test
    public void testPlanBedWithSpringSeasonPlantReturnsCrocusInSpringAndEmptyInWinter() {
        GardenCatalogueManager manager = new GardenCatalogueManager(TestData.DEFAULT_PLANTS);
        manager.addPlant(new Plant("Spring Crocus", "Crocus vernus", "FLO-002", PlantType.Flower, BloomPeriod.fromSeason(Season.Spring), 0.1, SoilCondition.Any, LightCondition.FullSun, java.util.Map.of()));

        assertEquals(List.of("Spring Crocus"), manager.planBed(Month.March, 1.0).stream().map(Plant::name).toList());
        assertEquals(List.of("Spring Crocus"), manager.planBed(Month.April, 1.0).stream().map(Plant::name).toList());
        assertEquals(List.of("Spring Crocus"), manager.planBed(Month.May, 1.0).stream().map(Plant::name).toList());
        assertTrue(manager.planBed(Month.February, 1.0).isEmpty());
    }

    @Test
    public void testPlanBedWithSpecificMonthsPlantReturnsLilyInJulyAugustAndEmptyInJune() {
        GardenCatalogueManager manager = new GardenCatalogueManager();
        manager.addPlant(new Plant("Summer Lily", "Lilium", "FLO-003", PlantType.Flower, new BloomPeriod(List.of(Month.July, Month.August)), 0.5, SoilCondition.Any, LightCondition.FullSun, java.util.Map.of()));

        assertEquals(List.of("Summer Lily"), manager.planBed(Month.July, 1.0).stream().map(Plant::name).toList());
        assertEquals(List.of("Summer Lily"), manager.planBed(Month.August, 1.0).stream().map(Plant::name).toList());
        assertTrue(manager.planBed(Month.June, 1.0).isEmpty());
    }

    @Test
    public void testPlanHedgeWithMinHeightFourMetersReturnsBeechOakSilverBirchAndYew() {
        GardenCatalogueManager manager = new GardenCatalogueManager(TestData.DEFAULT_PLANTS);
        double minHeight = 4.0;

        List<Plant> result = manager.planHedge(minHeight);

        List<String> names = result.stream().map(Plant::name).sorted().toList();
        assertEquals(List.of("Beech", "Oak", "Silver Birch", "Yew"), names);
    }
}
