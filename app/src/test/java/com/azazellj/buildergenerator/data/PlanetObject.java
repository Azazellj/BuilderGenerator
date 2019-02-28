package com.azazellj.buildergenerator.data;


import com.azazellj.builder.annotation.Builder;
import com.azazellj.builder.annotation.BuilderField;

import java.util.List;

@Builder(
        generatedClassName = "PlanetObjectBuilder",
        fields = {
                @BuilderField(fieldType = String.class, name = "name"),
                @BuilderField(fieldType = int.class, name = "age"),
                @BuilderField(fieldType = List.class, name = "all_satellites"),
                @BuilderField(fieldType = PlanetObject[].class, name = "planets")
        }
)
public class PlanetObject {
    public String name;
    public int age;
    public List<PlanetObject> sateltites;
    public PlanetObject[] nearestPlanets;
}
