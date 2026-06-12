package org.sammancoaching;

import java.util.List;
import java.util.Map;

public class TestData {
    public static List<Plant> defaultPlants() {
        return List.of(
            new PlantBuilder().withName("Lavender").withLatinName("Lavandula angustifolia").withArticleNumber("FLO-001").withBloomPeriod(BloomPeriod.fromRange(Month.June, Month.August)).withMaxHeight(0.6).withSoil(SoilCondition.Sandy).withLight(LightCondition.FullSun).build(),
            new PlantBuilder().withName("Rose").withLatinName("Rosa").withArticleNumber("BSH-001").withType(PlantType.Bush).withBloomPeriod(BloomPeriod.fromRange(Month.June, Month.September)).withMaxHeight(1.5).withSoil(SoilCondition.Loamy).withLight(LightCondition.FullSun).build(),
            new PlantBuilder().withName("Hydrangea").withLatinName("Hydrangea macrophylla").withArticleNumber("BSH-002").withType(PlantType.Bush).withBloomPeriod(BloomPeriod.fromRange(Month.July, Month.September)).withMaxHeight(1.2).withSoil(SoilCondition.Clay).withLight(LightCondition.PartialShade).build(),
            new PlantBuilder().withName("Boxwood").withLatinName("Buxus sempervirens").withArticleNumber("BSH-003").withType(PlantType.Bush).withMaxHeight(1.0).withSoil(SoilCondition.Any).withLight(LightCondition.PartialShade).build(),
            new PlantBuilder().withName("Beech").withLatinName("Fagus sylvatica").withArticleNumber("TRE-001").withType(PlantType.Tree).withBloomPeriod(new BloomPeriod(List.of(Month.May))).withMaxHeight(5.0).withSoil(SoilCondition.Any).withLight(LightCondition.FullSun).build(),
            new PlantBuilder().withName("Yew").withLatinName("Taxus baccata").withArticleNumber("TRE-002").withType(PlantType.Tree).withMaxHeight(4.0).withSoil(SoilCondition.Any).withLight(LightCondition.PartialShade).build(),
            new PlantBuilder().withName("Oak").withLatinName("Quercus robur").withArticleNumber("TRE-003").withType(PlantType.Tree).withMaxHeight(20.0).withSoil(SoilCondition.Any).withLight(LightCondition.FullSun).build(),
            new PlantBuilder().withName("Silver Birch").withLatinName("Betula pendula").withArticleNumber("TRE-004").withType(PlantType.Tree).withMaxHeight(15.0).withSoil(SoilCondition.Sandy).withLight(LightCondition.FullSun).build(),
            new PlantBuilder().withName("Fern").withLatinName("Polypodiopsida").withArticleNumber("BSH-004").withType(PlantType.Bush).withMaxHeight(0.5).withSoil(SoilCondition.Loamy).withLight(LightCondition.FullShade).build()
        );
    }
}
