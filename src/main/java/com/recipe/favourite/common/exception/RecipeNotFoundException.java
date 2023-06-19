package com.recipe.favourite.common.exception;

public class RecipeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RecipeNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public RecipeNotFoundException(String message) {
        super(message);
    }
}
