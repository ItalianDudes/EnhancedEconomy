package it.italiandudes.enhancedeconomy.util;

import org.bukkit.Bukkit;

import java.util.logging.Logger;

@SuppressWarnings("unused")
public final class ServerLogger {
    public static Logger getLogger() {
        return Bukkit.getLogger();
    }
}
