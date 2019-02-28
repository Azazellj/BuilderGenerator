package com.azazellj.builder.compiler;

/**
 * Created by azazellj on 2/18/19.
 */
public class MissingArgumentException extends IllegalArgumentException {
    public MissingArgumentException(String s) {
        super(s);
    }
}
