package cn.inject.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import cn.inject.annotation.Inject;
import cn.inject.processor.contract.CommonClass;
import cn.inject.processor.helper.GenerateHelper;
import cn.inject.processor.helper.TypeHelper;
import cn.inject.processor.utils.Log;
import cn.inject.processor.utils.Utilx;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"cn.inject.annotation.Inject"})
public class InjectProcessor extends AbstractProcessor {

    private Map<TypeElement, List<VariableElement>> parentAndChild = new HashMap<>();

    //initial some tools
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        Utilx.elements = processingEnvironment.getElementUtils();
        Utilx.filer = processingEnvironment.getFiler();
        Utilx.types = processingEnvironment.getTypeUtils();
        Utilx.messager = processingEnvironment.getMessager();
        TypeHelper.mElements = Utilx.elements;
    }

    //Annotation Processing
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.size() > 0) {
            collection(roundEnvironment);
            generate();
            return true;
        }
        return false;
    }

    /**
     * Collect all annotation information
     *
     * @param roundEnvironment
     */
    private void collection(RoundEnvironment roundEnvironment) {
        //Gets the element with the specified annotation
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Inject.class);
        for (Element element : elements) {
            //This annotation is only valid for Field
            if (element.getKind() == ElementKind.FIELD) {
                TypeElement father = (TypeElement) element.getEnclosingElement();

                if (element.getModifiers().contains(Modifier.PRIVATE)) {
                    Log.e("The  fields CAN NOT BE 'private'!!! please check field ["
                            + element.getSimpleName() + "] in class [" + father.getQualifiedName() + "]");
                }

                //The parent type is Class
                if (parentAndChild.get(father) == null) {
                    parentAndChild.put(father, new ArrayList<VariableElement>());
                }
                List<VariableElement> elementList = parentAndChild.get(father);
                elementList.add((VariableElement) element);
            }
        }
    }

    /**
     * generate code
     */
    private void generate() {

        for (Map.Entry<TypeElement, List<VariableElement>> entry : parentAndChild.entrySet()) {
            TypeMirror typeMirror = entry.getKey().asType();
            //for activity
            if (isActivity(typeMirror)) {
                generateActivityInject(entry);
            }
            //for fragment
            if (isFragment(typeMirror) || isFragmentV4(typeMirror)) {
                generateFragmentInject(entry);
            }
        }
    }


    private void generateActivityInject(Map.Entry<TypeElement, List<VariableElement>> entry) {
        TypeElement father = entry.getKey();
        List<VariableElement> value = entry.getValue();

        MethodSpec.Builder injectSpec = MethodSpec
                .methodBuilder("inject")
                .addStatement("$T intent = new $T(context,$T.class)", CommonClass.intentClass, CommonClass.intentClass, father.asType())
                .returns(CommonClass.intentClass)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        injectSpec.addParameter(CommonClass.contextClass, "context");

        //build Parameter
        for (VariableElement item : value) {
            TypeMirror typeMirror = item.asType();
            injectSpec.addParameter(TypeName.get(typeMirror), item.getSimpleName().toString());
        }

        //build Bundle
        injectSpec.addStatement("$T bundle = new $T()", CommonClass.bundleClass, CommonClass.bundleClass);

        //fill Bundle
        for (VariableElement item : value) {
            CodeBlock codeBlock = GenerateHelper.putBundleParameter(item);
            if (codeBlock == null) {
                String clsName = item.getEnclosingElement().asType().toString();
                String name = item.toString();
                Log.i(item.asType().getKind().toString());
                Log.e(clsName + "#" + name + " Unsupported types: " + item.asType().toString());
            } else {
                injectSpec.addStatement(codeBlock);
            }
        }

        injectSpec.addStatement("intent.putExtras(bundle)");
        injectSpec.addStatement("return intent");

        String activityName = GenerateHelper.downName(father.getSimpleName().toString());
        //get from Bundle
        MethodSpec.Builder autoWire = MethodSpec
                .methodBuilder("autoWire")
                .addParameter(ClassName.get(father.asType()), activityName)
                .addStatement("$T bundle = $L.getIntent().getExtras()", CommonClass.bundleClass, activityName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        autoWire.beginControlFlow("if(bundle != null)");

        //build Parameter
        for (VariableElement item : value) {
            CodeBlock bundleParameter = GenerateHelper.getBundleParameter(item);
            if (bundleParameter == null) {
                String clsName = item.getEnclosingElement().asType().toString();
                String name = item.toString();
                Log.e(clsName + "#" + name + " UN SUPPORT TYPE: " + item.asType().toString());
            } else {
                autoWire.addStatement(bundleParameter);
            }
        }
        autoWire.endControlFlow();


        TypeSpec.Builder injectHelper = TypeSpec.classBuilder(father.getSimpleName().toString() + "Inject")
                .addJavadoc("Inject for {@link $L}", father.getQualifiedName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(injectSpec.build())
                .addMethod(autoWire.build());

        PackageElement packageElement = Utilx.elements.getPackageOf(father);

        JavaFile javaFile = JavaFile.builder(packageElement.toString(), injectHelper.build())
                .addFileComment("create by Inject")
                .build();
        try {
            javaFile.writeTo(Utilx.filer);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(e.getLocalizedMessage());
        }
    }


    private void generateFragmentInject(Map.Entry<TypeElement, List<VariableElement>> entry) {
        TypeElement father = entry.getKey();
        List<VariableElement> value = entry.getValue();

        MethodSpec.Builder injectSpec = MethodSpec
                .methodBuilder("inject")
                .returns(TypeName.get(father.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        String fatherName = GenerateHelper.downName(father.getSimpleName().toString());

        injectSpec.addParameter(TypeName.get(father.asType()), fatherName);
        //build Parameter
        for (VariableElement item : value) {
            TypeMirror typeMirror = item.asType();
            injectSpec.addParameter(TypeName.get(typeMirror), item.getSimpleName().toString());
        }

        //build Bundle
        injectSpec.addStatement("$T bundle = new $T()", CommonClass.bundleClass, CommonClass.bundleClass);

        //fill Bundle
        for (VariableElement item : value) {
            CodeBlock codeBlock = GenerateHelper.putBundleParameter(item);
            if (codeBlock == null) {
                String clsName = item.getEnclosingElement().asType().toString();
                String name = item.toString();
                Log.e(clsName + "#" + name + " UN SUPPORT TYPE: " + item.asType().toString());
            } else {
                injectSpec.addStatement(codeBlock);
            }
        }

        injectSpec.addStatement("$L.setArguments(bundle)", fatherName);
        injectSpec.addStatement("return $L", fatherName);

        String activityName = GenerateHelper.downName(father.getSimpleName().toString());

        //get from Bundle
        MethodSpec.Builder autoWire = MethodSpec
                .methodBuilder("autoWire")
                .addParameter(ClassName.get(father.asType()), activityName)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        autoWire.addStatement("$T bundle = $L.getArguments()", CommonClass.bundleClass, activityName);
        autoWire.beginControlFlow("if(bundle != null)");
        //build Parameter
        for (VariableElement item : value) {
            CodeBlock bundleParameter = GenerateHelper.getBundleParameter(item);
            if (bundleParameter == null) {
                String clsName = item.getEnclosingElement().asType().toString();
                String name = item.toString();
                Log.e(clsName + "#" + name + " UN SUPPORT TYPE: " + item.asType().toString());
            } else {
                autoWire.addStatement(bundleParameter);
            }
        }
        autoWire.endControlFlow();


        TypeSpec.Builder injectHelper = TypeSpec.classBuilder(father.getSimpleName().toString() + "Inject")
                .addJavadoc("Inject for {@link $L}", father.getQualifiedName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(injectSpec.build())
                .addMethod(autoWire.build());

        PackageElement packageElement = Utilx.elements.getPackageOf(father);

        JavaFile javaFile = JavaFile.builder(packageElement.toString(), injectHelper.build())
                .addFileComment("create by Inject")
                .build();
        try {
            javaFile.writeTo(Utilx.filer);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(e.getLocalizedMessage());
        }
    }

    private boolean isActivity(TypeMirror typeMirror) {
        TypeMirror activityTypeMirror = Utilx.elements.getTypeElement("android.app.Activity").asType();
        return Utilx.types.isSubtype(typeMirror, activityTypeMirror);
    }

    private boolean isFragment(TypeMirror typeMirror) {
        TypeMirror activityTypeMirror = Utilx.elements.getTypeElement("android.app.Fragment").asType();
        return Utilx.types.isSubtype(typeMirror, activityTypeMirror);
    }

    private boolean isFragmentV4(TypeMirror typeMirror) {
        TypeMirror activityTypeMirror = Utilx.elements.getTypeElement("android.support.v4.app.Fragment").asType();
        return Utilx.types.isSubtype(typeMirror, activityTypeMirror);
    }
}