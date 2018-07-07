package cn.inject.processor.contract;

import com.squareup.javapoet.ClassName;

import org.omg.CORBA.PUBLIC_MEMBER;

public class CommonClass {
    public static final ClassName intentClass = ClassName.get("android.content", "Intent");
    public static final ClassName contextClass = ClassName.get("android.content", "Context");
    public static final ClassName bundleClass = ClassName.get("android.os", "Bundle");

    public static final String activityName = "android.app.Activity";
    public static final String fragmentName = "android.app.Fragment";
    public static final String fragmentV4Name = "android.support.v4.app.Fragment";


    public static final String serializableName = "java.io.Serializable";
    public static final String parcelableName = "android.os.Parcelable";

    // Java type
    private static final String LANG = "java.lang";
    public static final String BYTE = LANG + ".Byte";
    public static final String SHORT = LANG + ".Short";
    public static final String INT = LANG + ".Integer";
    public static final String LONG = LANG + ".Long";
    public static final String CHAR = LANG + ".Character";
    public static final String FLOAT = LANG + ".Float";
    public static final String DOUBLE = LANG + ".Double";
    public static final String BOOLEAN = LANG + ".Boolean";
    public static final String STRING = LANG + ".String";
}
