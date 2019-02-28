package com.azazellj.buildergenerator;

import com.azazellj.builder.compiler.generated.PlanetObjectBuilder;
import com.azazellj.buildergenerator.data.PersonObject;
import com.azazellj.buildergenerator.data.PlanetObject;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by azazellj on 2/28/19.
 */
public class PlanetObjectTest {
    @Test
    public void checkBuilderValues() {
        PlanetObject satelite = new PlanetObject();
        satelite.name = "Fobos";

        PlanetObject[] planets = new PlanetObject[]{satelite};

        Map<String, Object> map = PlanetObjectBuilder.of()
                .setAge(100)
                .setName("Mars")
                .setAllSatellites(new ArrayList<PlanetObject>())
                .setPlanets(planets)
                .get();

        Assert.assertArrayEquals((PlanetObject[]) map.get("planets"), planets);
        Assert.assertEquals((int) map.get("age"), 100);
        Assert.assertEquals(map.get("all_satellites"), new ArrayList<PersonObject>());
        Assert.assertEquals(map.get("name"), "Mars");
    }
}