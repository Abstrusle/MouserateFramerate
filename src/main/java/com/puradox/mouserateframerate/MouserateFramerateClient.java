package com.puradox.mouserateframerate;

import com.puradox.mouserateframerate.config.Configuration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
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
            AtomicReference<Double> defaultFPS = new AtomicReference<>(Option.FRAMERATE_LIMIT.get(client.options));
            ClientLifecycleEvents.CLIENT_STOPPING.register(client1 -> { //Reset framerate to original value on game close.
                ticksSinceAction=0;
                Option.FRAMERATE_LIMIT.set(client.options, defaultFPS.get());
            });
            ClientTickEvents.END_CLIENT_TICK.register(client1 -> { //Every tick
                if (client.player!=null) {
                    if(Option.FRAMERATE_LIMIT.get(client.options)!=defaultFPS.get() && Option.FRAMERATE_LIMIT.get(client.options)!=config.frameDropMaximumFrames) {
                        defaultFPS.set(Option.FRAMERATE_LIMIT.get(client.options));
                    }
                    if (client.player.input.jumping || client.player.hurtTime > 0 || client.player.input.getMovementInput().length()>0 || client.mouse.getX()!=prevMouseX || client.mouse.getY()!=prevMouseY) {
                        prevMouseX=client.mouse.getX();
                        prevMouseY=client.mouse.getY();
                        ticksSinceAction = 0;
                        Option.FRAMERATE_LIMIT.set(client.options, defaultFPS.get());
                    } else if (ticksSinceAction<config.ticksUntilFrameDrop) {
                        ticksSinceAction++;
                    } else {
                        Option.FRAMERATE_LIMIT.set(client.options, config.frameDropMaximumFrames);
                    }
                }
            });
        });

    }

    public static void saveConfig () {
        config.saveConfig(new File(FabricLoader.getInstance().getConfigDir().toFile(), "mouserate-framerate.json"));
    }
}
