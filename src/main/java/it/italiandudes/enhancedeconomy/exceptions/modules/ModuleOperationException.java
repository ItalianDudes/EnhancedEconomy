package it.italiandudes.enhancedeconomy.exceptions.modules;

import it.italiandudes.enhancedeconomy.exceptions.ModuleException;

@SuppressWarnings("unused")
public class ModuleOperationException extends ModuleException {
    public ModuleOperationException(String message) {
        super(message);
    }
    public ModuleOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
