package org.sammancoaching;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class GardenCatalogueManagerApprovalTests {
    private GardenCatalogueManager manager;
    private PlantPrinter plantPrinter;

    @BeforeEach
    public void setup() {
        manager = new GardenCatalogueManager(TestData.defaultPlants());
        plantPrinter = new PlantPrinter();
    }

    @Test
    public void HedgePlanning() {
        double minHeight = 2.0;

        List<Plant> hedge = manager.planHedge(minHeight);

        Approvals.verify(printScenario("{ MinHeight = " + minHeight + " }", hedge));
    }

    @Test
    public void ConditionFiltering() {
        SoilCondition soil = SoilCondition.Sandy;
        LightCondition light = LightCondition.FullSun;

        List<Plant> plants = manager.getPlantsForCondition(soil, light);

        Approvals.verify(printScenario("{ Soil = " + soil + ", Light = " + light + " }", plants));
    }

    @Test
    public void BedPlanning() {
        Month month = Month.June;
        double maxHeight = 2.0;

        List<Plant> bedInJune = manager.planBed(month, maxHeight);

        Approvals.verify(printScenario("{ Month = " + month + ", MaxHeight = " + maxHeight + " }", bedInJune));
    }

    private String printScenario(String input, List<Plant> result) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INPUT ===\n");
        sb.append(input).append("\n\n");

        sb.append("=== RESULT PLANTS ===\n");
        if (result.isEmpty()) {
            sb.append("(None)\n");
        } else {
            for (int i = 0; i < result.size(); i++) {
                sb.append(String.format("--- Plant %d ---\n", i + 1));
                plantPrinter.print(sb, result.get(i));
            }
        }

        return sb.toString();
    }
}
