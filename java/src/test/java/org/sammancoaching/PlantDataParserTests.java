package org.sammancoaching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class PlantDataParserTests {
    private PlantDataParser dataParser;

    @BeforeEach
    public void setup() {
        BugConfigurations.reset();
        dataParser = new PlantDataParser();
    }

    @Test
    public void testEmptyStreamEmptyList() throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        List<Plant> result = dataParser.parse(stream);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testV1SinglePlantValidResult() throws IOException {
        Plant plant = new Plant("Test Bush", "", "", PlantType.Bush, new BloomPeriod(List.of()), 0.0, SoilCondition.Any, LightCondition.FullSun, new HashMap<>());
        byte[] binaryData = createBinaryData(List.of(plant), 1);
        List<Plant> result = dataParser.parse(new ByteArrayInputStream(binaryData));

        assertEquals(1, result.size());
        assertEquals("Test Bush", result.get(0).name());
        assertEquals(PlantType.Bush, result.get(0).type());
    }

    @Test
    public void testV1TwoPlantsValidResult() throws IOException {
        Plant p1 = new Plant("Flower1", "", "", PlantType.Flower, BloomPeriod.fromRange(Month.May, Month.July), 0.5, SoilCondition.Sandy, LightCondition.FullSun, new HashMap<>());
        Plant p2 = new Plant("Bush1", "", "", PlantType.Bush, new BloomPeriod(List.of(Month.August)), 2.0, SoilCondition.Clay, LightCondition.PartialShade, new HashMap<>());
        
        byte[] binaryData = createBinaryData(List.of(p1, p2), 1);
        List<Plant> result = dataParser.parse(new ByteArrayInputStream(binaryData));

        assertEquals(2, result.size());
        assertEquals("Flower1", result.get(0).name());
        assertEquals(PlantType.Flower, result.get(0).type());
        assertEquals(0.5, result.get(0).maxHeight());
        assertEquals(SoilCondition.Sandy, result.get(0).soil());
        assertEquals(LightCondition.FullSun, result.get(0).light());
        assertEquals(List.of(Month.May, Month.June, Month.July), result.get(0).bloomPeriod().months());

        assertEquals("Bush1", result.get(1).name());
        assertEquals(PlantType.Bush, result.get(1).type());
        assertEquals(2.0, result.get(1).maxHeight());
        assertEquals(SoilCondition.Clay, result.get(1).soil());
        assertEquals(LightCondition.PartialShade, result.get(1).light());
        assertEquals(List.of(Month.August), result.get(1).bloomPeriod().months());
    }

    @Test
    public void testV2TwoPlantsValidResult() throws IOException {
        Plant p1 = new Plant("Lavender", "Lavandula angustifolia", "FLO-001", PlantType.Flower, BloomPeriod.fromRange(Month.June, Month.August), 0.6, SoilCondition.Sandy, LightCondition.FullSun, Map.of("Color", "Purple", "Fragrance", "High"));
        Plant p2 = new Plant("Minimal Plant", "", "", PlantType.Flower, new BloomPeriod(List.of()), 0.0, SoilCondition.Any, LightCondition.FullSun, Map.of());

        byte[] binaryData = createBinaryData(List.of(p1, p2), 2);
        List<Plant> result = dataParser.parse(new ByteArrayInputStream(binaryData));

        assertEquals(2, result.size());
        assertEquals("Lavender", result.get(0).name());
        assertEquals("Lavandula angustifolia", result.get(0).latinName());
        assertEquals("FLO-001", result.get(0).articleNumber());
        assertEquals("Purple", result.get(0).properties().get("Color"));
        assertEquals("High", result.get(0).properties().get("Fragrance"));

        assertEquals("Minimal Plant", result.get(1).name());
        assertEquals("", result.get(1).latinName());
        assertEquals("", result.get(1).articleNumber());
        assertTrue(result.get(1).properties().isEmpty());
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

                for (Map.Entry<String, String> entry : plant.properties().entrySet()) {
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
