package com.azazellj.buildergenerator;


import com.azazellj.builder.annotation.Builder;
import com.azazellj.builder.annotation.BuilderField;

import java.util.List;

@Builder
public class Person {
    @BuilderField
    private String name;
    @BuilderField
    private int age;
    @BuilderField
    private List<Person> children_in_family;
    @BuilderField
    private Person[] parents;
}
