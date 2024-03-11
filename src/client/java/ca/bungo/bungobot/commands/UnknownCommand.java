package ca.bungo.bungobot.commands;

import ca.bungo.bungobot.Command;

public class UnknownCommand extends Command {
    public UnknownCommand() {
        super("unknown");
    }

    @Override
    public boolean execute(String sender, String[] args) {
        String message = String.format(
                "Sorry %s! I'm not sure how to execute your command! Did you type it correctly?",
                sender);

        sendChatMessage(message);
        return true;
    }
}
