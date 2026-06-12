package org.sammancoaching;

import java.util.List;
import java.util.Map;

public record Plant(
        String name,
        String latinName,
        String articleNumber,
        PlantType type,
        BloomPeriod bloomPeriod,
        double maxHeight,
        SoilCondition soil,
        LightCondition light,
        Map<String, String> properties
) {
    public Plant {
        properties = Map.copyOf(properties);
    }

    public Plant(String name, PlantType type, BloomPeriod bloomPeriod, double maxHeight, SoilCondition soil, LightCondition light) {
        this(name, "", "", type, bloomPeriod, maxHeight, soil, light, Map.of());
    }
    @Override
    public String toString() {
        return "Plant{" +
                "name='" + name + '\'' +
                ", latinName='" + latinName + '\'' +
                ", articleNumber='" + articleNumber + '\'' +
                ", type=" + type +
                ", maxHeight=" + maxHeight +
                ", soil=" + soil +
                ", light=" + light +
                '}';
    }
}
