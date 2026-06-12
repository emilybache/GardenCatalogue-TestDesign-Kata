package org.sammancoaching;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlantDataParserTests {
    private PlantDataParser dataParser;
    private PlantPrinter plantPrinter;

    @BeforeEach
    public void setup() {
        BugConfigurations.reset();
        //BugConfigurations.bug5 = true;
        dataParser = new PlantDataParser();
        plantPrinter = new PlantPrinter();
    }

    @Test
    public void EmptyStream_EmptyList() throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);

        List<Plant> result = dataParser.parse(stream);

        Approvals.verify(printScenario("Empty Stream", new byte[0], 2, result));
    }

    @Test
    public void V1_SinglePlant_ValidResult() throws IOException {
        List<Plant> plants = List.of(
                new PlantBuilder().withName("Test Bush").withType(PlantType.Bush).build()
        );
        byte[] binaryData = createBinaryData(plants, 1);

        List<Plant> result = dataParser.parse(new ByteArrayInputStream(binaryData));

        Approvals.verify(printScenario(plants, binaryData, 2, result));
    }

    @Test
    public void V1_TwoPlants_ValidResult() throws IOException {
        List<Plant> plants = List.of(
                new PlantBuilder().withName("Flower1").withBloomPeriod(BloomPeriod.fromRange(Month.May, Month.July)).withMaxHeight(0.5).withSoil(SoilCondition.Sandy).withLight(LightCondition.FullSun).build(),
                new PlantBuilder().withName("Bush1").withType(PlantType.Bush).withBloomPeriod(new BloomPeriod(List.of(Month.August))).withMaxHeight(2.0).withSoil(SoilCondition.Clay).withLight(LightCondition.PartialShade).build()
        );
        byte[] binaryData = createBinaryData(plants, 1);

        List<Plant> result = dataParser.parse(new ByteArrayInputStream(binaryData));

        Approvals.verify(printScenario(plants, binaryData, 2, result));
    }

    @Test
    public void V2_TwoPlants_ValidResult() throws IOException {
        List<Plant> plants = List.of(
                new PlantBuilder()
                        .withName("Lavender")
                        .withLatinName("Lavandula angustifolia")
                        .withArticleNumber("FLO-001")
                        .withBloomPeriod(BloomPeriod.fromRange(Month.June, Month.August))
                        .withMaxHeight(0.6)
                        .withSoil(SoilCondition.Sandy)
                        .withLight(LightCondition.FullSun)
                        .withProperty("Color", "Purple")
                        .withProperty("Fragrance", "High")
                        .build(),
                new PlantBuilder()
                        .withName("Minimal Plant")
                        .build()
        );
        byte[] binaryData = createBinaryData(plants, 2);

        List<Plant> result = dataParser.parse(new ByteArrayInputStream(binaryData));

        Approvals.verify(printScenario(plants, binaryData, 2, result));
    }

    @Test
    public void V2_SinglePlant_ValidResult() throws IOException {
        List<Plant> plants = List.of(
                new PlantBuilder()
                        .withName("Red Rose")
                        .withLatinName("Rosa rubiginosa")
                        .withArticleNumber("ROS-001")
                        .withBloomPeriod(BloomPeriod.fromRange(Month.June, Month.September))
                        .withMaxHeight(1.5)
                        .withSoil(SoilCondition.Loamy)
                        .withLight(LightCondition.FullSun)
                        .withProperty("Color", "Deep Red")
                        .withProperty("Fragrance", "Strong")
                        .withProperty("Thorns", "Yes")
                        .withProperty("Difficulty", "Medium")
                        .build()
        );
        byte[] binaryData = createBinaryData(plants, 2);

        List<Plant> result = dataParser.parse(new ByteArrayInputStream(binaryData));

        Approvals.verify(printScenario(plants, binaryData, 2, result));
    }

    private String printScenario(Object input, byte[] binaryData, int version, List<Plant> result) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INPUT DATA ===\n");
        if (input instanceof String) {
            sb.append(input).append("\n");
        } else if (input instanceof List) {
            List<Plant> plants = (List<Plant>) input;
            for (int i = 0; i < plants.size(); i++) {
                sb.append(String.format("--- Item %d ---\n", i + 1));
                plantPrinter.print(sb, plants.get(i));
            }
        }

        if (binaryData.length > 0) {
            sb.append("\n");
            sb.append(String.format("Binary: %s\n", HexFormat.of().withUpperCase().formatHex(binaryData)));
        }

        sb.append("\n");
        sb.append(String.format("--- Parser Version %d ---\n", version));
        sb.append("\n");

        sb.append("=== OUTPUT PLANTS ===\n");
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

    private byte[] createBinaryData(List<Plant> plants, int version) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (version == 2) {
            out.write("PLNT".getBytes(StandardCharsets.UTF_8));
        }

        for (Plant plant : plants) {
            byte[] nameBytes = new byte[32];
            byte[] actualName = plant.name().getBytes(StandardCharsets.UTF_8);
            System.arraycopy(actualName, 0, nameBytes, 0, Math.min(actualName.length, 32));
            out.write(nameBytes);

            ByteBuffer bb = ByteBuffer.allocate(24).order(ByteOrder.LITTLE_ENDIAN);
            bb.putInt(plant.type().ordinal());
            bb.putDouble(plant.maxHeight());
            bb.putInt(plant.soil().ordinal());
            bb.putInt(plant.light().ordinal());

            int bloomMask = 0;
            for (Month m : plant.bloomPeriod().months()) {
                bloomMask |= (1 << (m.getValue() - 1));
            }
            bb.putInt(bloomMask);
            out.write(bb.array());

            if (version == 2) {
                writeString(out, plant.latinName());
                writeString(out, plant.articleNumber());

                ByteBuffer propCountBuf = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
                propCountBuf.putInt(plant.properties().size());
                out.write(propCountBuf.array());

                List<Map.Entry<String, String>> sortedEntries = plant.properties().entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .toList();

                for (Map.Entry<String, String> entry : sortedEntries) {
                    writeString(out, entry.getKey());
                    writeString(out, entry.getValue());
                }
            }
        }
        return out.toByteArray();
    }

    private void writeString(ByteArrayOutputStream out, String s) throws IOException {
        byte[] data = s.getBytes(StandardCharsets.UTF_8);
        write7BitEncodedInt(out, data.length);
        out.write(data);
    }

    private void write7BitEncodedInt(ByteArrayOutputStream out, int value) {
        while (value >= 0x80) {
            out.write((value & 0x7f) | 0x80);
            value >>= 7;
        }
        out.write(value);
    }
}
