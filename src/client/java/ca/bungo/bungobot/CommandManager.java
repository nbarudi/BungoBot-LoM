package ca.bungo.bungobot;

import ca.bungo.BotBungo;
import ca.bungo.BotBungoClient;
import ca.bungo.bungobot.commands.*;
import ca.bungo.data.BotBungoSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {

    public static Map<String, Command> commandList = new HashMap<>();
    public static List<String> whitelistedPlayers = new ArrayList<>();

    private static final Command introduceCommand = new IntroductionCommand();
    private static final Command unknownCommand = new UnknownCommand();

    public static void initCommands(){

        commandList.clear();
        whitelistedPlayers.clear();

        whitelistedPlayers.add("ItzBungo");
        whitelistedPlayers.add("nbarudi");
        whitelistedPlayers.add("Zelereth");
        whitelistedPlayers.add("AWillToLive");
        whitelistedPlayers.add("Rebeii");
        whitelistedPlayers.add("Mythnull");

        commandList.put("waypoint", new WaypointCommand("waypoint"));
        commandList.put("goto", new GotoCommand("goto"));
        commandList.put("sit", new SitCommand("sit"));
        commandList.put("stand", new StandCommand("stand"));
        commandList.put("follow", new FollowCommand("follow"));
        commandList.put("stop", new StopCommand("stop"));
        commandList.put("sell", new SellItemsCommand("sell"));
        commandList.put("status", new StatusCommand("status"));
        commandList.put("toggle", new ToggleCommand("toggle"));
        commandList.put("toggles", new TogglesCommand("toggles"));
        commandList.put("drop", new DropCommand("drop"));
        commandList.put("slot", new SlotCommand("slot"));
        commandList.put("zoom", new ZoomiesCommand("zoom"));
        commandList.put("whitelist", new WhitelistCommand("whitelist"));
        commandList.put("introduce", introduceCommand);
        commandList.put("sniff", new SniffCommand("sniff"));
        commandList.put("run", new RunCommand("run"));
        commandList.put("reinit", new ReInitCommand("reinit"));
        commandList.put("wave", new WaveCommand("wave"));
        commandList.put("watch", new WatchCommand("watch"));
        commandList.put("crouch", new CrouchCommand("crouch"));
        commandList.put("come", new ComeCommand("come"));
        commandList.put("punch", new PunchCommand("punch"));
        commandList.put("commands", new CommandsCommand("commands"));
        commandList.put("sleep", new SleepCommand("sleep"));
        commandList.put("order", new OrderCommand("order"));
        commandList.put("generate", new GenerateResponseCommand("generate"));
    }

    public static void handleChatMessage(Text message, Instant timestamp, CallbackInfo info){
        //BotBungo.LOGGER.info(message.getString());
        if(!BotBungoSettings.enableBungoBot) return;

        String[] _msg = message.getString().split(":");
        if(_msg.length <= 1) return;
        String username = _msg[0];
        StringBuilder builder = new StringBuilder();
        for(int i = 1; i < _msg.length; i++){
            builder.append(_msg[i]).append(":");
        }
        String messageData = builder.substring(1, builder.length()-1);

        assert MinecraftClient.getInstance().player != null;
        if(username.equals(MinecraftClient.getInstance().player.getName().getString())) return;


        if(messageData.toLowerCase().startsWith("bot bungo"))
            messageData = messageData.substring("bot bungo".length());
        else if(messageData.toLowerCase().startsWith("botbungo"))
            messageData = messageData.substring("botbungo".length());
        else if(messageData.toLowerCase().startsWith("botgo"))
            messageData = messageData.substring("botgo".length());
        else
            return;


        String[] data = messageData.split(" ");


        if((data.length == 0 || messageData.length() <= 1) && whitelistedPlayers.contains(username)){
            introduceCommand.execute(username, data);
            return;
        }
        messageData = messageData.substring(1);
        data = messageData.split(" ");

        if(!whitelistedPlayers.contains(username)) {
            if(BotBungoSettings.autoInteractionMode){
                getCommand("generate").execute(username, data);
            }
            return;
        }

        String commandName = data[0];
        Command command = getCommand(commandName);

        if(command == null || !command.execute(username, data)){
            if(BotBungoSettings.autoInteractionMode){
                getCommand("generate").execute(username, data);
            }else{
                unknownCommand.execute(username, data);
            }
            return;
        }

        //ToDo: I dont actually know yet but maybe something?
    }

    private static Command getCommand(String commandName){
        for(Map.Entry<String, Command> commandEntry : commandList.entrySet()){
            if(commandEntry.getKey().equalsIgnoreCase(commandName)){
                return commandEntry.getValue();
            }
        }
        return null;
    }

}
