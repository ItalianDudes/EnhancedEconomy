package it.italiandudes.enhancedeconomy.exceptions.modules;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;

@SuppressWarnings("unused")
public class ModuleAlreadyLoadedException extends ModuleException {
    public ModuleAlreadyLoadedException(String message) {
        super(message);
    }
    public ModuleAlreadyLoadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
