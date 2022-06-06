package com.puradox.mouserateframerate;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class MouserateFramerate implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(Configuration.class, GsonConfigSerializer::new);
    }
}
