package org.sammancoaching;

import java.util.*;

public class PlantBuilder {
    private String name = "Default Plant";
    private String latinName = "";
    private String articleNumber = "";
    private PlantType type = PlantType.Flower;
    private BloomPeriod bloomPeriod = new BloomPeriod(List.of());
    private double maxHeight = 1.0;
    private SoilCondition soil = SoilCondition.Any;
    private LightCondition light = LightCondition.FullSun;
    private Map<String, String> properties = new TreeMap<>();

    public PlantBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PlantBuilder withLatinName(String latinName) {
        this.latinName = latinName;
        return this;
    }

    public PlantBuilder withArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
        return this;
    }

    public PlantBuilder withType(PlantType type) {
        this.type = type;
        return this;
    }

    public PlantBuilder withBloomPeriod(BloomPeriod bloomPeriod) {
        this.bloomPeriod = bloomPeriod;
        return this;
    }

    public PlantBuilder withMaxHeight(double maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public PlantBuilder withSoil(SoilCondition soil) {
        this.soil = soil;
        return this;
    }

    public PlantBuilder withLight(LightCondition light) {
        this.light = light;
        return this;
    }

    public PlantBuilder withProperty(String key, String value) {
        this.properties.put(key, value);
        return this;
    }

    public static PlantBuilder flower() {
        return new PlantBuilder().withType(PlantType.Flower);
    }

    public static PlantBuilder bush() {
        return new PlantBuilder().withType(PlantType.Bush);
    }

    public static PlantBuilder tree() {
        return new PlantBuilder().withType(PlantType.Tree);
    }

    public Plant build() {
        return new Plant(
                name,
                latinName,
                articleNumber,
                type,
                bloomPeriod,
                maxHeight,
                soil,
                light,
                Collections.unmodifiableMap(new TreeMap<>(properties)));
    }
}
