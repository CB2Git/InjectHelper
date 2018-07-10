package cn.inject.processor.helper;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.rmi.CORBA.Util;

import cn.inject.annotation.Inject;
import cn.inject.processor.contract.CommonClass;
import cn.inject.processor.contract.InjectType;
import cn.inject.processor.utils.Log;
import cn.inject.processor.utils.Utilx;

public class TypeHelper {

    public static Elements mElements;

    public static int changeType(Element element) {
        TypeMirror typeMirror = element.asType();

        //Base type
        if (typeMirror.getKind().isPrimitive()) {
            return typeMirror.getKind().ordinal();
        }

        //Object
        if (CommonClass.BYTE.equals(typeMirror.toString())) {
            return InjectType.BYTE.ordinal();
        } else if (CommonClass.CHAR.equals(typeMirror.toString())) {
            return InjectType.CHAR.ordinal();
        } else if (CommonClass.BOOLEAN.equals(typeMirror.toString())) {
            return InjectType.BOOLEAN.ordinal();
        } else if (CommonClass.SHORT.equals(typeMirror.toString())) {
            return InjectType.SHORT.ordinal();
        } else if (CommonClass.INT.equals(typeMirror.toString())) {
            return InjectType.INT.ordinal();
        } else if (CommonClass.LONG.equals(typeMirror.toString())) {
            return InjectType.LONG.ordinal();
        } else if (CommonClass.FLOAT.equals(typeMirror.toString())) {
            return InjectType.FLOAT.ordinal();
        } else if (CommonClass.DOUBLE.equals(typeMirror.toString())) {
            return InjectType.DOUBLE.ordinal();
        } else if (CommonClass.STRING.equals(typeMirror.toString())) {
            return InjectType.STRING.ordinal();
        }


        //Array Object
        if (CommonClass.BOOLEAN_OBJ_ARRAY.equals(typeMirror.toString()) || CommonClass.BOOLEAN_ARRAY.equals(typeMirror.toString())) {
            return InjectType.BOOLEAN_ARRAY.ordinal();
        } else if (CommonClass.INT_OBJ_ARRAY.equals(typeMirror.toString()) || CommonClass.INT_ARRAY.equals(typeMirror.toString())) {
            return InjectType.INT_ARRAY.ordinal();
        } else if (CommonClass.CHAR_OBJ_ARRAY.equals(typeMirror.toString()) || CommonClass.CHAR_ARRAY.equals(typeMirror.toString())) {
            return InjectType.CHAR_ARRAY.ordinal();
        } else if (CommonClass.SHORT_OBJ_ARRAY.equals(typeMirror.toString()) || CommonClass.SHORT_ARRAY.equals(typeMirror.toString())) {
            return InjectType.SHORT_ARRAY.ordinal();
        } else if (CommonClass.BYTE_OBJ_ARRAY.equals(typeMirror.toString()) || CommonClass.BYTE_ARRAY.equals(typeMirror.toString())) {
            return InjectType.BYTE_ARRAY.ordinal();
        } else if (CommonClass.LONG_OBJ_ARRAY.equals(typeMirror.toString()) || CommonClass.LONG_ARRAY.equals(typeMirror.toString())) {
            return InjectType.LONG_ARRAY.ordinal();
        } else if (CommonClass.FLOAT_OBJ_ARRAY.equals(typeMirror.toString()) || CommonClass.FLOAT_ARRAY.equals(typeMirror.toString())) {
            return InjectType.FLOAT_ARRAY.ordinal();
        } else if (CommonClass.DOUBLE_OBJ_ARRAY.equals(typeMirror.toString()) || CommonClass.DOUBLE_ARRAY.equals(typeMirror.toString())) {
            return InjectType.DOUBLE_ARRAY.ordinal();
        } else if (CommonClass.STRING_OBJ_ARRAY.equals(typeMirror.toString())) {
            return InjectType.STRING_ARRAY.ordinal();
        }


        //Serializable object
        TypeElement serializableType = Utilx.elements.getTypeElement(CommonClass.serializableName);
        TypeElement parcelableType = Utilx.elements.getTypeElement(CommonClass.parcelableName);
        if (Utilx.types.isSubtype(element.asType(), serializableType.asType())) {
            return InjectType.SERIALIZABLE.ordinal();
        } else if (Utilx.types.isSubtype(element.asType(), parcelableType.asType())) {
            return InjectType.PARCELABLE.ordinal();
        }

        return InjectType.NONSUPPORT.ordinal();
    }
}
