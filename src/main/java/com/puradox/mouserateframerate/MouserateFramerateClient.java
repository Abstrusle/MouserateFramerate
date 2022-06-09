package com.puradox.mouserateframerate;

import com.puradox.mouserateframerate.config.Configuration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Environment(EnvType.CLIENT)
public class MouserateFramerateClient implements ClientModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("MouserateFramerate");
    public static Configuration config;
    static int ticksSinceAction = 0;
    static double prevMouseX = 0;
    static double prevMouseY = 0;


    @Override
    public void onInitializeClient() {
        config = Configuration.loadConfig(new File(FabricLoader.getInstance().getConfigDir().toFile(), "mouserate-framerate.json"));
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> { //For attaining the original FPS.
            GameOptions options = MinecraftClient.getInstance().options;
            AtomicReference<Integer> defaultFPS = new AtomicReference<>(options.getMaxFps().getValue());
            int roundedFrames = (int) Math.floor(((double)config.frameDropMaximumFrames)/10)*10;
            ClientLifecycleEvents.CLIENT_STOPPING.register(client1 -> { //Reset framerate to original value on game close.
                ticksSinceAction=0;
                options.getMaxFps().setValue(defaultFPS.get());
            });
            ClientTickEvents.END_CLIENT_TICK.register(client1 -> { //Every tick
                if (client.player!=null) {
                    if(!Objects.equals(options.getMaxFps().getValue(), defaultFPS.get()) && (options.getMaxFps().getValue()!=roundedFrames)) {
                        defaultFPS.set(options.getMaxFps().getValue());
                    }
                    if (((config.useMouseActivity) && (client.mouse.getX()!=prevMouseX || client.mouse.getY()!=prevMouseY))
                            || ((config.useMovementActivity) && (client.player.input.getMovementInput().length()>0 || client.player.input.jumping || client.player.isDescending()))
                            || ((config.useHurtActivity) && (client.player.hurtTime > 0))
                            || ((config.useHandSwingActivity) && (client.player.handSwinging || client.player.isUsingItem()))) {
                        prevMouseX=client.mouse.getX();
                        prevMouseY=client.mouse.getY();
                        ticksSinceAction = 0;
                        options.getMaxFps().setValue(defaultFPS.get());
                    } else if (ticksSinceAction<config.ticksUntilFrameDrop) {
                        ticksSinceAction++;
                    } else {
                        options.getMaxFps().setValue(config.frameDropMaximumFrames);
                    }
                }
            });
        });

    }

    public static void saveConfig () {
        config.saveConfig(new File(FabricLoader.getInstance().getConfigDir().toFile(), "mouserate-framerate.json"));
    }

    public static Configuration getConfig() {
        return config;
    }
}
