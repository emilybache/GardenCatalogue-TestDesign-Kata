package org.sammancoaching;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GardenCatalogueManager {
    private final List<Plant> plants;

    public GardenCatalogueManager() {
        this.plants = new ArrayList<>();
    }

    public GardenCatalogueManager(List<Plant> plants) {
        this.plants = new ArrayList<>(plants);
    }

    public void addPlant(Plant plant) {
        this.plants.add(plant);
    }

    public int getPlantCount() {
        return plants.size();
    }

    public List<Plant> getPlants() {
        return new ArrayList<>(plants);
    }

    public Plant getPlantByName(String name) {
        return plants.stream()
                .filter(p -> p.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    public List<Plant> getPlantsForCondition(SoilCondition soil, LightCondition light) {
        return plants.stream()
                .filter(p -> (p.soil() == soil || p.soil() == SoilCondition.Any) && p.light() == light)
                .toList();
    }

    public List<Plant> planBed(Month month, double maxHeight) {
        return plants.stream()
                .filter(p -> p.type() == PlantType.Flower &&
                        p.bloomPeriod().bloomsIn(month) &&
                        p.maxHeight() <= maxHeight)
                .toList();
    }

    public List<Plant> planHedge(double minHeight) {
        return plants.stream()
                .filter(p -> (p.type() == PlantType.Bush || p.type() == PlantType.Tree) &&
                        p.maxHeight() >= minHeight)
                .toList();
    }
}
