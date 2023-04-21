package it.italiandudes.enhancedeconomy.util;

import it.italiandudes.enhancedeconomy.EnhancedEconomy;
import it.italiandudes.idl.common.JarHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

@SuppressWarnings("unused")
public final class Localization {

    // Attributes
    private static JSONObject langFile = null;

    // Langs
    public static final class Langs {
        public static final String EN_US = "en-US";
        public static final String IT_IT = "it-IT";
    }

    // Lang Keys
    public static final class Keys {
        public static final String TEST_ENTRY = "test_entry";
    }

    // Localized String Getter
    public static void init(@NotNull final JavaPlugin pluginInstance) throws IOException {
        if (langFile != null) return;
        if (!pluginInstance.getDataFolder().exists()) {
            //noinspection ResultOfMethodCallIgnored
            pluginInstance.getDataFolder().mkdirs();
        }
        JarHandler.copyDirectoryFromJar(new File(EnhancedEconomy.PLUGIN_JAR_PATH), Resource.Path.Localization.LOCALIZATION_DIR, pluginInstance.getDataFolder(), false);
    }
    public static void load(@NotNull final JavaPlugin pluginInstance, final String LOCALIZATION) throws IOException {
        if (LOCALIZATION == null) {
            throw new RuntimeException("Passed LOCALIZATION key as null.");
        }
        if (!pluginInstance.getDataFolder().exists()) {
            throw new RuntimeException("Initialization not done yet, Localization.init() must be called first!");
        }

        File filepath = new File(pluginInstance.getDataFolder().getAbsolutePath()+'/'+Resource.Path.Localization.LOCALIZATION_DIR+LOCALIZATION+".json");
        FileReader fileReader = new FileReader(filepath);

        try {
            langFile = (JSONObject) new JSONParser().parse(fileReader);
            fileReader.close();
        } catch (Exception e) {
            fileReader.close();
            throw new IOException("Can't read lang file \""+filepath.getAbsolutePath()+"\"!");
        }
    }
    @Nullable
    public static String getLocalizedString(final String KEY) {
        if (langFile == null) return null;
        return (String) langFile.get(KEY);
    }
}
