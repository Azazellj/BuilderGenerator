package com.azazellj.builder.compiler;

import com.azazellj.builder.annotation.Builder;
import com.azazellj.builder.annotation.BuilderField;
import com.azazellj.builder.compiler.util.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class BuilderClass {
    private static final String EMPTY = "";

    private Element annotatedElement;
    private String generatedClassName;
    private Element[] classFields;
    private BuilderField[] builderFields;

    public BuilderClass(Element annotatedElement) {
        this.annotatedElement = annotatedElement;

        setGeneratedClassName();
        setFields();
    }

    private void setGeneratedClassName() {
        Builder annotation = Objects.requireNonNull(annotatedElement.getAnnotation(Builder.class));

        generatedClassName = Objects.requireNonNull(annotation.generatedClassName());

        if (EMPTY.equals(generatedClassName)) {
            generatedClassName = annotatedElement.getSimpleName() + "Builder";
        }
    }

    private void setFields() {
        this.builderFields = Objects.requireNonNull(annotatedElement.getAnnotation(Builder.class)).fields();

        getFieldsFromClass();
    }

    private void getFieldsFromClass() {
        List<? extends Element> elements = annotatedElement.getEnclosedElements();
        List<Element> annotatedFields = new ArrayList<>();

        for (Element field : elements) {
            BuilderField annotation = field.getAnnotation(BuilderField.class);

            if (field.getKind() != ElementKind.FIELD || Helper.isNullable(annotation)) continue;

            annotatedFields.add(field);
        }

        this.classFields = annotatedFields.toArray(new Element[0]);
    }

    public String getGeneratedClassName() {
        return generatedClassName;
    }

    public Element[] getClassFields() {
        return classFields;
    }

    public BuilderField[] getBuilderFields() {
        return builderFields;
    }
}