package com.puradox.mouserateframerate;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "mouserate-framerate")
public class Configuration implements ConfigData {
    public int ticksUntilFrameDrop = 600; //Defaulted to thirty seconds.
    public int frameDropMaximumFrames = 10;

    public static Configuration getInstance() {
        return AutoConfig.getConfigHolder(Configuration.class).getConfig();
    }
}
