package com.azazellj.builder.compiler;

import com.azazellj.builder.annotation.Builder;
import com.azazellj.builder.compiler.generator.BuilderGenerator;
import com.google.auto.service.AutoService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.google.common.base.Preconditions.checkNotNull;

@AutoService(Processor.class)
public class CompilerProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elements;
    private Types types;

    private Map<String, BuilderClass> builderClasses = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        filer = env.getFiler();
        messager = env.getMessager();
        elements = env.getElementUtils();
        types = env.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        findAnnotatedClasses(env);

        generateBuilderClasses();

        return false;
    }

    private void generateBuilderClasses() {
        for (BuilderClass builderClass : builderClasses.values()) {
            if (!BuilderGenerator.generateClass(filer, messager, elements, types, builderClass)) {
                break;
            }
        }
    }

    private void findAnnotatedClasses(RoundEnvironment env) {
        Set<? extends Element> elements = env.getElementsAnnotatedWith(Builder.class);

        for (Element annotatedElement : elements) {
            registerAnnotatedClass(annotatedElement);
        }
    }

    private void registerAnnotatedClass(Element element) {
        BuilderClass annotatedClass = new BuilderClass(element);
        String elementName = elementName(element);
        builderClasses.put(elementName, annotatedClass);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Builder.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // HELP METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    String elementName(Element element) {
        return checkNotNull(element).getSimpleName().toString();
    }
}
