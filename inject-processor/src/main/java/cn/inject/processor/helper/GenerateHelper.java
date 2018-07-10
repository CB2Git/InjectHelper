package cn.inject.processor.helper;

import com.squareup.javapoet.CodeBlock;

import javax.lang.model.element.VariableElement;

import cn.inject.processor.contract.InjectType;

public class GenerateHelper {

    /**
     * @param variableElement
     * @return
     */
    public static CodeBlock putBundleParameter(VariableElement variableElement) {
        String variableName = variableElement.getSimpleName().toString();
        int variableType = TypeHelper.changeType(variableElement);
        String typeStr;
        if (variableType == InjectType.BYTE.ordinal()) {
            typeStr = "Byte";
        } else if (variableType == InjectType.CHAR.ordinal()) {
            typeStr = "Char";
        } else if (variableType == InjectType.BOOLEAN.ordinal()) {
            typeStr = "Boolean";
        } else if (variableType == InjectType.INT.ordinal()) {
            typeStr = "Int";
        } else if (variableType == InjectType.LONG.ordinal()) {
            typeStr = "Long";
        } else if (variableType == InjectType.FLOAT.ordinal()) {
            typeStr = "Float";
        } else if (variableType == InjectType.DOUBLE.ordinal()) {
            typeStr = "Double";
        } else if (variableType == InjectType.STRING.ordinal()) {
            typeStr = "String";
        } else if (variableType == InjectType.SERIALIZABLE.ordinal()) {
            typeStr = "Serializable";
        } else if (variableType == InjectType.PARCELABLE.ordinal()) {
            typeStr = "Parcelable";
        } else if (variableType == InjectType.BYTE_ARRAY.ordinal()) {
            typeStr = "ByteArray";
        } else {
            return null;
        }
        return CodeBlock.builder()
                .add("bundle.put$L($S,$L)", typeStr, variableName + "_inject_key", variableName)
                .build();
    }


    /**
     * @param variableElement
     * @return
     */
    public static CodeBlock getBundleParameter(VariableElement variableElement) {
        int variableType = TypeHelper.changeType(variableElement);
        String variableName = variableElement.getSimpleName().toString();
        String parentName = variableElement.getEnclosingElement().getSimpleName().toString();
        String typeStr;
        if (variableType == InjectType.BYTE.ordinal()) {
            typeStr = "Byte";
        } else if (variableType == InjectType.CHAR.ordinal()) {
            typeStr = "Char";
        } else if (variableType == InjectType.BOOLEAN.ordinal()) {
            typeStr = "Boolean";
        } else if (variableType == InjectType.INT.ordinal()) {
            typeStr = "Int";
        } else if (variableType == InjectType.LONG.ordinal()) {
            typeStr = "Long";
        } else if (variableType == InjectType.FLOAT.ordinal()) {
            typeStr = "Float";
        } else if (variableType == InjectType.DOUBLE.ordinal()) {
            typeStr = "Double";
        } else if (variableType == InjectType.STRING.ordinal()) {
            typeStr = "String";
        } else if (variableType == InjectType.SERIALIZABLE.ordinal()) {
            typeStr = "Serializable";
            return CodeBlock.builder()
                    .add("$L.$L = bundle.get$L($S)", downName(parentName), variableName, typeStr, variableName + "_inject_key")
                    .build();
        } else if (variableType == InjectType.PARCELABLE.ordinal()) {
            typeStr = "Parcelable";
            return CodeBlock.builder()
                    .add("$L.$L = bundle.get$L($S)", downName(parentName), variableName, typeStr, variableName + "_inject_key")
                    .build();
        } else {
            return null;
        }

        return CodeBlock.builder()
                .add("$L.$L = bundle.get$L($S,$L.$L)", downName(parentName), variableName, typeStr, variableName + "_inject_key", downName(parentName), variableName)
                .build();
    }

    public static String upName(String value) {
        value = value.substring(0, 1).toUpperCase() + value.substring(1);
        return value;
    }

    public static String downName(String value) {
        value = value.substring(0, 1).toLowerCase() + value.substring(1);
        return value;
    }
}
