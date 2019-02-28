package com.azazellj.builder.compiler.generator;

import com.azazellj.builder.annotation.BuilderField;
import com.azazellj.builder.compiler.BuilderClass;
import com.azazellj.builder.compiler.MissingArgumentException;
import com.azazellj.builder.compiler.util.Helper;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class BuilderGenerator {
    private static MethodSpec getBuilderMethod(ClassName builderClassName, Element field) {
        BuilderField annotation = field.getAnnotation(BuilderField.class);

        //  get field name
        String fieldName = field.getSimpleName().toString();
        String nameFromAnnotation = annotation.name();
        if (!com.azazellj.builder.compiler.util.Helper.isEmptyString(nameFromAnnotation)) {
            fieldName = nameFromAnnotation;
        }

        return getBuilderMethod(builderClassName, fieldName, TypeName.get(field.asType()));
    }

    private static MethodSpec getBuilderMethod(Elements elements, ClassName builderClassName, BuilderField annotation) throws MissingArgumentException {
        if (com.azazellj.builder.compiler.util.Helper.isEmptyString(annotation.name())) {
            throw new MissingArgumentException("name in BuilderField is missing");
        }

        String fieldName = annotation.name();

        TypeName typeName;

        try {
            Element field = elements.getTypeElement(annotation.fieldType().getName());
            typeName = TypeName.get(field.asType());
        } catch (MirroredTypeException ex) {
            typeName = TypeName.get(ex.getTypeMirror());
        }

        return getBuilderMethod(builderClassName, fieldName, typeName);
    }


    private static MethodSpec getBuilderMethod(ClassName builderClassName, String fieldName, TypeName type) {
        String variableName = Helper.camelsify(fieldName);
        String methodName = "set" + variableName.substring(0, 1).toUpperCase() + variableName.substring(1);

        return MethodSpec.methodBuilder(methodName)
                .returns(builderClassName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(type, variableName)
                .addStatement("map.put($S, $L)", fieldName, variableName)
                .addStatement("return this")
                .build();
    }

    public static boolean generateClass(Filer filer, Messager messager, Elements elements, Types types, BuilderClass builderClass) {
        String className = builderClass.getGeneratedClassName();

        ClassName builderClassName = ClassName.get(Helper.GENERATED_PACKAGE, className);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        ParameterizedTypeName mapType = ParameterizedTypeName.get(Map.class, String.class, Object.class);
        ParameterizedTypeName hashMapType = ParameterizedTypeName.get(HashMap.class, String.class, Object.class);

        FieldSpec mapFiled = FieldSpec.builder(mapType, "map")
                .addModifiers(Modifier.PRIVATE)
                .initializer("new $T()", hashMapType)
                .build();

        classBuilder.addField(mapFiled);

        MethodSpec instanceMethod = MethodSpec.methodBuilder("of")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(builderClassName)
                .addStatement("return new $T()", builderClassName)
                .build();

        classBuilder.addMethod(instanceMethod);

        /*
         * Generate setters.
         */

        for (Element field : builderClass.getClassFields()) {
            classBuilder.addMethod(getBuilderMethod(builderClassName, field));
        }

        for (BuilderField annotation : builderClass.getBuilderFields()) {
            classBuilder.addMethod(getBuilderMethod(elements, builderClassName, annotation));
        }

        MethodSpec getMethod = MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.PUBLIC)
                .returns(mapType)
                .addStatement("return map")
                .build();

        classBuilder.addMethod(getMethod);
        TypeSpec generatedClass = classBuilder.build();
        JavaFile generatedFile = JavaFile.builder(Helper.GENERATED_PACKAGE, generatedClass).build();

        try {
            generatedFile.writeTo(filer);
            return true;
        } catch (IOException ioEx) {
            messager.printMessage(Diagnostic.Kind.WARNING, ioEx.toString());
            return false;
        }
    }
}
