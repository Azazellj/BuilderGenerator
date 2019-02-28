package com.azazellj.builder.compiler.util;

public class Helper {

    public static String getSetterMethodName(String variableName) {
        return "set" + variableName.substring(0, 1).toUpperCase() + variableName.substring(1);
    }

    public static boolean isNullable(Object object) {
        return object == null;
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmptyString(String s) {
        return s == null || s.length() == 0;
    }

    private static final String INNER_PACKAGE = Helper.class.getPackage().getName();
    public static final String GENERATED_PACKAGE = INNER_PACKAGE.substring(0, INNER_PACKAGE.lastIndexOf(".")) + ".generated";

    public static String camelsify(String in) {
        StringBuilder sb = new StringBuilder();
        boolean capitalizeNext = false;
        for (char c : in.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    sb.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}
