package com.azazellj.buildergenerator;


import com.azazellj.builder.annotation.Builder;
import com.azazellj.builder.annotation.BuilderField;

import java.util.List;

@Builder(
        generatedClassName = "PlanetBuilder",
        fields = {
                @BuilderField(fieldType = String.class, name = "name"),
                @BuilderField(fieldType = int.class, name = "age"),
                @BuilderField(fieldType = List.class, name = "all_satellites"),
                @BuilderField(fieldType = Planet[].class, name = "planets")
        }
)
public class Planet {
    private String name;
    private int age;
    private List<Planet> sateltites;
    private Planet[] nearestPlanets;
}
