package org.sammancoaching;

import java.util.List;
import java.util.Map;

public class TestData {
    public static final List<Plant> DEFAULT_PLANTS = List.of(
            new Plant("Lavender", "Lavandula angustifolia", "FLO-001", PlantType.Flower, BloomPeriod.fromRange(Month.June, Month.August), 0.6, SoilCondition.Sandy, LightCondition.FullSun, Map.of()),
            new Plant("Rose", "Rosa", "BSH-001", PlantType.Bush, BloomPeriod.fromRange(Month.June, Month.September), 1.5, SoilCondition.Loamy, LightCondition.FullSun, Map.of()),
            new Plant("Hydrangea", "Hydrangea macrophylla", "BSH-002", PlantType.Bush, BloomPeriod.fromRange(Month.July, Month.September), 1.2, SoilCondition.Clay, LightCondition.PartialShade, Map.of()),
            new Plant("Boxwood", "Buxus sempervirens", "BSH-003", PlantType.Bush, new BloomPeriod(List.of()), 1.0, SoilCondition.Any, LightCondition.PartialShade, Map.of()),
            new Plant("Beech", "Fagus sylvatica", "TRE-001", PlantType.Tree, new BloomPeriod(List.of(Month.May)), 5.0, SoilCondition.Any, LightCondition.FullSun, Map.of()),
            new Plant("Yew", "Taxus baccata", "TRE-002", PlantType.Tree, new BloomPeriod(List.of()), 4.0, SoilCondition.Any, LightCondition.PartialShade, Map.of()),
            new Plant("Oak", "Quercus robur", "TRE-003", PlantType.Tree, new BloomPeriod(List.of()), 20.0, SoilCondition.Any, LightCondition.FullSun, Map.of()),
            new Plant("Silver Birch", "Betula pendula", "TRE-004", PlantType.Tree, new BloomPeriod(List.of()), 15.0, SoilCondition.Sandy, LightCondition.FullSun, Map.of()),
            new Plant("Fern", "Polypodiopsida", "BSH-004", PlantType.Bush, new BloomPeriod(List.of()), 0.5, SoilCondition.Loamy, LightCondition.FullShade, Map.of())
    );
}
