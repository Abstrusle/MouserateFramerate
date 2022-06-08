package com.puradox.mouserateframerate.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.puradox.mouserateframerate.MouserateFramerateClient;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Configuration {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public int ticksUntilFrameDrop = 600; //Defaulted to thirty seconds.
    public int frameDropMaximumFrames = 10;
    public static Configuration loadConfig(File file) {
        Configuration config;

        if (file.exists() && file.isFile()) {
            try (
                    FileInputStream fileInputStream = new FileInputStream(file);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
            ) {
                config = GSON.fromJson(bufferedReader, Configuration.class);
            } catch (IOException e) {
                throw new RuntimeException("[HTM] Failed to load config", e);
            }
        } else {
            config = new Configuration();
        }

        config.saveConfig(file);

        return config;
    }

    public void saveConfig(File config) {
        try (
                FileOutputStream stream = new FileOutputStream(config);
                Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)
        ) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            MouserateFramerateClient.LOGGER.error("Failed to save config");
        }
    }
}
