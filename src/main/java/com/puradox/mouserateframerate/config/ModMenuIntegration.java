package com.puradox.mouserateframerate.config;

import com.puradox.mouserateframerate.MouserateFramerateClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    static Configuration config;

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            config = MouserateFramerateClient.getConfig();
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("title.mouserate-framerate.config"))
                    .setSavingRunnable(MouserateFramerateClient::saveConfig);
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            ConfigCategory main = builder.getOrCreateCategory(Text.translatable("category.mouserate-framerate.main"));

            main.addEntry(entryBuilder.startIntField(Text.translatable("option.mouserate-framerate.frame_drop_maximum_frames"),
                            config.frameDropMaximumFrames)
                    .setDefaultValue(20)
                    .setTooltip(Text.translatable("option.mouserate-framerate.frame_drop_maximum_frames.tooltip"))
                    .setSaveConsumer(newValue -> config.frameDropMaximumFrames = (int) Math.floor(((double)newValue)/10)*10)
                    .build());
            main.addEntry(entryBuilder.startIntField(Text.translatable("option.mouserate-framerate.ticks_until_frame_drop"),
                            config.ticksUntilFrameDrop)
                    .setDefaultValue(300)
                    .setTooltip(Text.translatable("option.mouserate-framerate.ticks_until_frame_drop.tooltip"))
                    .setSaveConsumer(newValue -> config.ticksUntilFrameDrop = newValue)
                    .build());

            ConfigCategory activities = builder.getOrCreateCategory(Text.translatable("category.mouserate-framerate.activities"));

            activities.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.mouserate-framerate.use_mouse_activity"), config.useMouseActivity)
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("option.mouserate-framerate.use_mouse_activity.tooltip"))
                    .setSaveConsumer(newValue -> config.useMouseActivity = newValue)
                    .build());
            activities.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.mouserate-framerate.use_movement_activity"), config.useMovementActivity)
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("option.mouserate-framerate.use_movement_activity.tooltip"))
                    .setSaveConsumer(newValue -> config.useMovementActivity = newValue)
                    .build());
            activities.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.mouserate-framerate.use_hurt_activity"), config.useHurtActivity)
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("option.mouserate-framerate.use_hurt_activity.tooltip"))
                    .setSaveConsumer(newValue -> config.useHurtActivity = newValue)
                    .build());
            activities.addEntry(entryBuilder.startBooleanToggle(Text.translatable("option.mouserate-framerate.use_hand_swing_activity"), config.useHandSwingActivity)
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("option.mouserate-framerate.use_mouse_activity.tooltip"))
                    .setSaveConsumer(newValue -> config.useHandSwingActivity = newValue)
                    .build());

            return builder.build();
        };
    }
}
