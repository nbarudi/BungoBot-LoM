package ca.bungo.bungobot.commands;

import ca.bungo.BotBungoClient;
import ca.bungo.bungobot.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OrderCommand extends Command {

    private Map<Integer, Runnable> orders;

    public OrderCommand(String commandName) {
        super(commandName);
        orders = new HashMap<>();
        initOrders();
    }

    @Override
    public boolean execute(String sender, String[] args) {

        if(args.length <= 1){
            sendChatMessage("Please state an order to enact!");
            return true;
        }

        String order = args[1];
        try {
            int numOrder = Integer.parseInt(order);
            if(!orders.containsKey(numOrder)){
                sendChatMessage("Unknown order!");
                return true;
            }
            orders.get(numOrder).run();
        } catch(NumberFormatException e){
            sendChatMessage("Failed to translate order number!");
            return true;
        }

        return true;
    }

    private void initOrders(){

        orders.put(386, ()-> {
            sendChatMessage("As you command...");
            BotBungoClient.schedule(()->{
                sendChatMessage("5");
                BotBungoClient.schedule(()->{
                    sendChatMessage("4");
                    BotBungoClient.schedule(()->{
                        sendChatMessage("3");
                        BotBungoClient.schedule(()->{
                            sendChatMessage("2");
                            BotBungoClient.schedule(()->{
                                sendChatMessage("1");
                                BotBungoClient.schedule(()->{
                                    sendChatMessage("/suicide");
                                }, 1, TimeUnit.SECONDS);
                            }, 1, TimeUnit.SECONDS);
                        }, 1, TimeUnit.SECONDS);
                    }, 1, TimeUnit.SECONDS);
                }, 1, TimeUnit.SECONDS);
            }, 1, TimeUnit.SECONDS);
        });

        orders.put(149, ()-> {
            sendChatMessage("Entering Attack Mode...");
            BotBungoClient.schedule(()->{
                sendChatMessage("/skin https://skins.bungo.ca/public/lom/evil_botgo.png");
                sendChatMessage("/nick Evil BotBungo");
            }, 1, TimeUnit.SECONDS);
        });

    }
}
