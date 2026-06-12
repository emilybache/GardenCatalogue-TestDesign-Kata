package org.sammancoaching;

import java.util.Locale;
import java.util.stream.Collectors;

public class PlantPrinter {
    public void print(StringBuilder sb, Plant plant) {
        print(sb, plant, "");
    }

    public void print(StringBuilder sb, Plant plant, String indent) {
        sb.append(String.format("%sName: %s\n", indent, plant.name()));
        appendIfNotEmpty(sb, indent, "Latin Name", plant.latinName());
        appendIfNotEmpty(sb, indent, "Article #", plant.articleNumber());

        if (plant.type() != PlantType.Flower) {
            sb.append(String.format("%sType: %s\n", indent, plant.type()));
        }

        if (!plant.bloomPeriod().months().isEmpty()) {
            String months = plant.bloomPeriod().months().stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            sb.append(String.format("%sBlooms: %s\n", indent, months));
        }

        sb.append(String.format(Locale.ROOT, "%sMax Height: %.1fm\n", indent, plant.maxHeight()));
        sb.append(String.format("%sSoil: %s\n", indent, plant.soil()));
        sb.append(String.format("%sLight: %s\n", indent, plant.light()));

        if (!plant.properties().isEmpty()) {
            sb.append(String.format("%sProperties:\n", indent));
            new java.util.TreeMap<>(plant.properties()).forEach((key, value) ->
                    sb.append(String.format("%s  - %s: %s\n", indent, key, value))
            );
        }
    }

    private void appendIfNotEmpty(StringBuilder sb, String indent, String label, String value) {
        if (value != null && !value.isEmpty()) {
            sb.append(String.format("%s%s: %s\n", indent, label, value));
        }
    }
}
