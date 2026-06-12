package org.sammancoaching;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GardenCatalogueManagerTests {

    @Test
    public void GetPlantsForSandySoilAndFullSun_ReturnsLavenderBeechOakAndSilverBirch() {
        GardenCatalogueManager manager = new GardenCatalogueManager(TestData.defaultPlants());
        SoilCondition soil = SoilCondition.Sandy;
        LightCondition light = LightCondition.FullSun;

        List<Plant> result = manager.getPlantsForCondition(soil, light);

        List<String> names = result.stream().map(Plant::name).sorted().toList();
        assertEquals(List.of("Beech", "Lavender", "Oak", "Silver Birch"), names);
    }

    @Test
    public void PlanBedInJulyWithMaxHeightOneMeter_ReturnsLavender() {
        GardenCatalogueManager manager = new GardenCatalogueManager(TestData.defaultPlants());
        Month month = Month.July;
        double maxHeight = 1.0;

        List<Plant> result = manager.planBed(month, maxHeight);

        List<String> names = result.stream().map(Plant::name).toList();
        assertEquals(List.of("Lavender"), names);
    }

    @Test
    public void PlanBedWithSpringSeasonPlant_ReturnsCrocusInSpringAndEmptyInWinter() {
        GardenCatalogueManager manager = new GardenCatalogueManager(TestData.defaultPlants());
        manager.addPlant(new Plant("Spring Crocus", "Crocus vernus", "FLO-002", PlantType.Flower, BloomPeriod.fromSeason(Season.Spring), 0.1, SoilCondition.Any, LightCondition.FullSun, java.util.Map.of()));

        List<Plant> marchBed = manager.planBed(Month.March, 1.0);
        List<Plant> aprilBed = manager.planBed(Month.April, 1.0);
        List<Plant> mayBed = manager.planBed(Month.May, 1.0);
        List<Plant> februaryBed = manager.planBed(Month.February, 1.0);

        assertEquals(List.of("Spring Crocus"), marchBed.stream().map(Plant::name).toList());
        assertEquals(List.of("Spring Crocus"), aprilBed.stream().map(Plant::name).toList());
        assertEquals(List.of("Spring Crocus"), mayBed.stream().map(Plant::name).toList());
        assertTrue(februaryBed.isEmpty());
    }

    @Test
    public void PlanBedWithSpecificMonthsPlant_ReturnsLilyInJulyAugustAndEmptyInJune() {
        GardenCatalogueManager manager = new GardenCatalogueManager();
        manager.addPlant(new Plant("Summer Lily", "Lilium", "FLO-003", PlantType.Flower, new BloomPeriod(List.of(Month.July, Month.August)), 0.5, SoilCondition.Any, LightCondition.FullSun, java.util.Map.of()));

        List<Plant> julyBed = manager.planBed(Month.July, 1.0);
        List<Plant> augustBed = manager.planBed(Month.August, 1.0);
        List<Plant> juneBed = manager.planBed(Month.June, 1.0);

        assertEquals(List.of("Summer Lily"), julyBed.stream().map(Plant::name).toList());
        assertEquals(List.of("Summer Lily"), augustBed.stream().map(Plant::name).toList());
        assertTrue(juneBed.isEmpty());
    }

    @Test
    public void PlanHedgeWithMinHeightFourMeters_ReturnsBeechOakSilverBirchAndYew() {
        GardenCatalogueManager manager = new GardenCatalogueManager(TestData.defaultPlants());
        double minHeight = 4.0;

        List<Plant> result = manager.planHedge(minHeight);

        List<String> names = result.stream().map(Plant::name).sorted().toList();
        assertEquals(List.of("Beech", "Oak", "Silver Birch", "Yew"), names);
    }
}
