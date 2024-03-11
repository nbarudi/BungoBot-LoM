package ca.bungo.bungobot;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;

public abstract class Command {

    private final String commandName;
    public Command(String commandName){
        this.commandName = commandName;
    }

    public abstract boolean execute(String sender, String[] args);

    public String getCommandName() { return this.commandName; }

    public void sendChatMessage(String message){
        MinecraftClient.getInstance().execute(()->{
            ChatScreen screen = new ChatScreen("");
            Screen prevScreen = MinecraftClient.getInstance().currentScreen;
            MinecraftClient.getInstance().setScreen(screen);
            screen.sendMessage(message, true);
            MinecraftClient.getInstance().setScreen(prevScreen);
        });
    }

}
