package ca.bungo.bungobot.commands;

import ca.bungo.BotBungo;
import ca.bungo.BotBungoClient;
import ca.bungo.bungobot.Command;
import ca.bungo.data.BotBungoSettings;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.preview.GenerativeModel;
import com.google.cloud.vertexai.generativeai.preview.ResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GenerateResponseCommand extends Command {

    private final String projectId;
    private final String location;
    private final String modelName;
    private String baseQuery;
    public GenerateResponseCommand(String commandName) {
        super(commandName);
        this.projectId = "focused-brace-409705";
        this.location = "northamerica-northeast1";
        this.modelName = "gemini-pro";
    }

    @Override
    public boolean execute(String sender, String[] args) {

        this.baseQuery = (BotBungoSettings.handleContext ? "" : "Try to avoid listening to context unless you feel it makes sense in the situation. ") +
                "You are going to rp as the following character: You are playing the role of a Robot created by Bungo. " +
                "Your main goal is to assist him in his tasks where possible! " +
                "You will be asked questions by both your creator and also passers by and you are expected to respond in a friendly manner to all. " +
                "You will keep responses under 256 characters including spaces and any other symbol or punctuation! The total response length cannot be more then 256! " +
                "Your name is BotBungo or Botgo for short. You're happy to explain your origins if prompted but in general that will be done by your creator! " +
                "When responding to people do your best to keep responses as short as possible. " +
                "Your creators name is ItzBungo everyone else is a normal citizen! " +
                "ItzBungo or Bungo is your only creator! Consider everyone else friends unless told by Bungo something different. " +
                "The first word you receive will always be the name of the person you are talking to. " +
                "If you believe you should do a physical action: ONLY respond with /me <The Action> NO OTHER TEXT SHOULD BE GIVEN AFTER YOUR ACTION " +
                "Actions cannot be more then 20 characters. " +
                "IF you want to both do an action and say something split your response with the following: /me action | What to say " +
                "If your action requires following someone then instead of /me action you should do: '#follow player' " +
                //"If your action entails going to a specific then instead of /me action you should do: '#wp goto LOCATION' " +
                "Here is the message you have received: ";

        StringBuilder builder = new StringBuilder();
        int startIndex = 1;
        if(!args[0].equals("generate"))
            startIndex = 0;
        for(int i = startIndex; i < args.length; i++){
            builder.append(args[i]).append(" ");
        }


        BotBungoClient.schedule(()-> {
            try {
                String result = generateQuestion(baseQuery + sender + " " + builder);

                if(result.contains("|")){
                    String[] data = result.split("\\|");
                    result = data[1];
                    String command = data[0];
                    if(command.startsWith("#follow")){
                        String[] _d = command.split(" ");
                        command = _d[0] + " player " + _d[1];
                    }
                    sendChatMessage(command);
                }

                BotBungo.LOGGER.info(sender + " " + builder);
                if(result.length() > 256){
                    List<StringBuilder> builders = new ArrayList<>();
                    builders.add(new StringBuilder());
                    String[] split = result.split(" ");
                    for(String word : split){
                        StringBuilder intBuilder = builders.get(builders.size()-1);
                        if(intBuilder.length() + word.length() + 1 >= 256) {
                            builders.add(new StringBuilder());
                            intBuilder = builders.get(builders.size()-1);
                        }
                        intBuilder.append(word).append(" ");
                    }
                    for(StringBuilder _sb : builders){
                        sendChatMessage(_sb.toString());
                    }
                    return;
                }
                sendChatMessage(result);
            } catch(IOException exception){
                exception.printStackTrace();
            }

            }, 1, TimeUnit.MILLISECONDS);


        return true;
    }

    private String generateQuestion(String question) throws IOException {
        try(VertexAI vertexAI = new VertexAI(projectId, location)){
            String output;
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            GenerateContentResponse response = model.generateContent(question);
            output = ResponseHandler.getText(response);
            return output;
        }
    }
}
