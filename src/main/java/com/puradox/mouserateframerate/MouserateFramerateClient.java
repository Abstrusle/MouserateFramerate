package com.puradox.mouserateframerate;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.Option;

import java.util.concurrent.atomic.AtomicReference;

@Environment(EnvType.CLIENT)
public class MouserateFramerateClient implements ClientModInitializer {
    static int ticksSinceAction = 0;
    static double prevMouseX = 0;
    static double prevMouseY = 0;

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> { //For attaining the original FPS.
            AtomicReference<Double> defaultFPS = new AtomicReference<>(Option.FRAMERATE_LIMIT.get(client.options));
            ClientLifecycleEvents.CLIENT_STOPPING.register(client1 -> { //Reset framerate to original value on game close.
                Option.FRAMERATE_LIMIT.set(client.options, defaultFPS.get());
            });

            ClientTickEvents.END_CLIENT_TICK.register(client1 -> { //Every tick
                if (client.player!=null) {
                    if(Option.FRAMERATE_LIMIT.get(client.options)!=defaultFPS.get() && Option.FRAMERATE_LIMIT.get(client.options)!=10) {
                        defaultFPS.set(Option.FRAMERATE_LIMIT.get(client.options));
                    }
                    if (client.player.hurtTime > 0 || client.player.input.getMovementInput().length()>0 || client.mouse.getX()!=prevMouseX || client.mouse.getY()!=prevMouseY) {
                        prevMouseX=client.mouse.getX();
                        prevMouseY=client.mouse.getY();
                        ticksSinceAction = 0;
                        Option.FRAMERATE_LIMIT.set(client.options, defaultFPS.get());
                    } else if (ticksSinceAction<Configuration.getInstance().ticksUntilFrameDrop) {
                        ticksSinceAction++;
                    } else {
                        Option.FRAMERATE_LIMIT.set(client.options, Configuration.getInstance().frameDropMaximumFrames);
                    }
                }
            });
        });

    }
}
