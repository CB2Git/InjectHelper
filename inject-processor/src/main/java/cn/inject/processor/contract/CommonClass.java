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

    public static final String BYTE_OBJ_ARRAY = BYTE + "[]";
    public static final String SHORT_OBJ_ARRAY = SHORT + "[]";
    public static final String INT_OBJ_ARRAY = INT + "[]";
    public static final String LONG_OBJ_ARRAY = LONG + "[]";
    public static final String CHAR_OBJ_ARRAY = CHAR + "[]";
    public static final String FLOAT_OBJ_ARRAY = FLOAT + "[]";
    public static final String DOUBLE_OBJ_ARRAY = DOUBLE + "[]";
    public static final String BOOLEAN_OBJ_ARRAY = BOOLEAN + "[]";
    public static final String STRING_OBJ_ARRAY = STRING + "[]";

    public static final String BOOLEAN_ARRAY = "boolean[]";
    public static final String INT_ARRAY = "int[]";
    public static final String BYTE_ARRAY = "byte[]";
    public static final String SHORT_ARRAY = "short[]";
    public static final String LONG_ARRAY = "long[]";
    public static final String CHAR_ARRAY = "char[]";
    public static final String FLOAT_ARRAY = "float[]";
    public static final String DOUBLE_ARRAY = "double[]";


}
