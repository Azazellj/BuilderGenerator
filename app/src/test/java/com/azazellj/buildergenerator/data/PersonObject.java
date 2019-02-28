package com.azazellj.buildergenerator.data;


import com.azazellj.builder.annotation.Builder;
import com.azazellj.builder.annotation.BuilderField;

import java.util.List;

@Builder
public class PersonObject {
    @BuilderField
    public String name;
    @BuilderField
    public int age;
    @BuilderField
    public List<PersonObject> children_in_family;
    @BuilderField
    public PersonObject[] parents;
}
