package cn.inject.processor.utils;

import javax.tools.Diagnostic;

public class Log {

    public static void i(String msg) {
        Utilx.messager.printMessage(Diagnostic.Kind.OTHER, msg);
    }

    public static void w(String msg) {
        Utilx.messager.printMessage(Diagnostic.Kind.WARNING, msg);
    }

    public static void e(String msg) {
        Utilx.messager.printMessage(Diagnostic.Kind.ERROR, msg);
    }
}
