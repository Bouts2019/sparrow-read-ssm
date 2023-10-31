package com.martini.sparrow.exception;

/**
 * @author martini at 2020/12/14 8:53
 */
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6076064882922276376L;

    public NotFoundException(String message) {
        super(message);
    }
}
