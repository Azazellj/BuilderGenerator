package com.azazellj.buildergenerator;

import com.azazellj.builder.compiler.generated.PersonObjectBuilder;
import com.azazellj.buildergenerator.data.PersonObject;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by azazellj on 2/28/19.
 */
public class PersonObjectTest {
    @Test
    public void checkBuilderAnnotatedFields() {
        PersonObject dad = new PersonObject();
        dad.name = "Dad";

        PersonObject[] parents = new PersonObject[]{dad};

        Map<String, Object> map = PersonObjectBuilder.of()
                .setAge(10)
                .setName("Tim")
                .setChildrenInFamily(new ArrayList<PersonObject>())
                .setParents(parents)
                .get();

        Assert.assertArrayEquals((PersonObject[]) map.get("parents"), parents);
        Assert.assertEquals((int) map.get("age"), 10);
        Assert.assertEquals(map.get("children_in_family"), new ArrayList<PersonObject>());
        Assert.assertEquals(map.get("name"), "Tim");
    }
}