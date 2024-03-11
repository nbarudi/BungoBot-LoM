package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;
import net.minecraft.client.MinecraftClient;

public class IntroductionCommand extends Command {


    public IntroductionCommand() {
        super("introduction");
    }

    @Override
    public boolean execute(String sender, String[] args) {

        assert MinecraftClient.getInstance().player != null;
        String message = String.format(
                "Hi there! My name is %s. I'm a robot created by Bungo! I'm not the greatest at talking but I can do my best!" +
                " If you would like some extra info about how I work and how I was made then you should talk to Bungo! He's is happy to share! :)",
                MinecraftClient.getInstance().player.getName().getString());
        sendChatMessage(message);
        sendChatMessage("I hope you enjoy talking to me and I can't wait to interact in the future!");
        return true;
    }
}
