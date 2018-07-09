package cn.inject.processor.helper;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.rmi.CORBA.Util;

import cn.inject.processor.contract.CommonClass;
import cn.inject.processor.contract.InjectType;
import cn.inject.processor.utils.Log;
import cn.inject.processor.utils.Utilx;

public class TypeHelper {

    public static Elements mElements;

    public static int changeType(Element element) {
        TypeMirror typeMirror = element.asType();

        //base type
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

        TypeElement serializableType = Utilx.elements.getTypeElement(CommonClass.serializableName);
        TypeElement parcelableType = Utilx.elements.getTypeElement(CommonClass.parcelableName);

        //Serializable object
        if (Utilx.types.isSubtype(element.asType(), serializableType.asType())) {
            return InjectType.SERIALIZABLE.ordinal();
        } else if (Utilx.types.isSubtype(element.asType(), parcelableType.asType())) {
            return InjectType.PARCELABLE.ordinal();
        }


        return InjectType.NONSUPPORT.ordinal();
    }
}
