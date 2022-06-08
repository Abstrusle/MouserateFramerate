package com.puradox.mouserateframerate.config;

import com.puradox.mouserateframerate.MouserateFramerateClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfiglite.api.ConfigScreen;
import net.minecraft.text.TranslatableText;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigScreen screen = ConfigScreen.create(new TranslatableText("text.mouserate-framerate.config.title"), parent);
            screen.add(
                    new TranslatableText("text.mouserate-framerate.config.ticks_until_frame_drop"),
                    MouserateFramerateClient.config.ticksUntilFrameDrop,
                    () -> false,
                    i -> {
                        MouserateFramerateClient.config.ticksUntilFrameDrop = (int) i;
                        MouserateFramerateClient.saveConfig();
                    }
            );
            screen.add(
                    new TranslatableText("text.mouserate-framerate.config.frame_drop_maximum_frames"),
                    MouserateFramerateClient.config.frameDropMaximumFrames,
                    () -> false,
                    i -> {
                        MouserateFramerateClient.config.frameDropMaximumFrames = (int) i;
                        MouserateFramerateClient.saveConfig();
                    }
            );
            return screen.get();
        };
    }
}
