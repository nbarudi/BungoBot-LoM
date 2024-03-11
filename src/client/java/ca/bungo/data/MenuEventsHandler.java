package ca.bungo.data;

import ca.bungo.BotBungo;
import ca.bungo.data.screens.BotBungoScreen;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class MenuEventsHandler {

    public static void initEvents(){
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) ->{
            if(!(screen instanceof GameMenuScreen gameMenuScreen)) return;

            Screens.getButtons(gameMenuScreen).add(ButtonWidget.builder(Text.of("BotBungo Menu"), button ->{
                MinecraftClient.getInstance().setScreen(new BotBungoScreen());
            }).build());


        });
    }

}
