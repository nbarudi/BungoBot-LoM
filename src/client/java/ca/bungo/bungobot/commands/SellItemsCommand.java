package ca.bungo.bungobot.commands;

import baritone.api.BaritoneAPI;
import ca.bungo.BotBungoClient;
import ca.bungo.bungobot.Command;
import ca.bungo.data.farmers.AutoSellFish;

import java.util.concurrent.TimeUnit;

public class SellItemsCommand extends Command {
    public SellItemsCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean execute(String sender, String[] args) {

        sendChatMessage("Understood! I will now attempt to sell my items and then I will return here!");
        BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("wp save user dummylocation");
        BotBungoClient.schedule(AutoSellFish::startBot, 1, TimeUnit.SECONDS);

        return true;
    }
}
