package it.italiandudes.enhancedeconomy.exceptions.modules;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;

@SuppressWarnings("unused")
public class ModuleNotLoadedException extends ModuleException {
    public ModuleNotLoadedException(String message) {
        super(message);
    }
    public ModuleNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
