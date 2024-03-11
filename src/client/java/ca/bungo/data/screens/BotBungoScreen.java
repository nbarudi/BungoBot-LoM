package ca.bungo.data.screens;

import ca.bungo.bungobot.CommandManager;
import ca.bungo.data.BotBungoSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BotBungoScreen extends Screen {
    public BotBungoScreen() {
        super(Text.literal("BotBungo Screen"));
    }

    @Override
    protected void init() {
        ButtonWidget healthManage = ButtonWidget.builder(Text.literal("Manage Health: " + (BotBungoSettings.careSprintStat ? "On" : "Off")), button -> {
            BotBungoSettings.careSprintStat = !BotBungoSettings.careSprintStat;
            button.setMessage(Text.literal("Manage Health: " + (BotBungoSettings.careSprintStat ? "On" : "Off")));
            if(MinecraftClient.getInstance().player == null) return;

            MinecraftClient.getInstance().player.sendMessage(BotBungoSettings.careSprintStat ?
                    Text.literal("Enabled health management!").styled(s -> s.withColor(Formatting.GREEN)) :
                    Text.literal("Disabled health management").styled(s -> s.withColor(Formatting.RED)));
        }).tooltip(Tooltip.of(Text.literal("Have the bot Auto-Sit / Stand when pathing").styled(s->s.withColor(Formatting.AQUA))))
                .dimensions(this.width/2-50, 40, 100, 20).build();

        ButtonWidget bungoBot = ButtonWidget.builder(Text.literal("Bungo Bot: " + (BotBungoSettings.enableBungoBot ? "On" : "Off")), button -> {
                    BotBungoSettings.enableBungoBot = !BotBungoSettings.enableBungoBot;
                    button.setMessage(Text.literal("Bungo Bot: " + (BotBungoSettings.enableBungoBot ? "On" : "Off")));
                    if(MinecraftClient.getInstance().player == null) return;

                    MinecraftClient.getInstance().player.sendMessage(BotBungoSettings.enableBungoBot ?
                            Text.literal("Enabled Bungo Bot!").styled(s -> s.withColor(Formatting.GREEN)) :
                            Text.literal("Disabled Bungo Bot").styled(s -> s.withColor(Formatting.RED)));
                }).tooltip(Tooltip.of(Text.literal("Have the bot follow commands and interact!").styled(s->s.withColor(Formatting.AQUA))))
                .dimensions(this.width/2-50, 60, 100, 20).build();

        ButtonWidget reinitCommands = ButtonWidget.builder(Text.literal("Re-Init Command"), button -> {
            if (MinecraftClient.getInstance().player == null) return;
            CommandManager.initCommands();
            MinecraftClient.getInstance().player.sendMessage(Text.literal("Reloading Bot Bungo Commands!").styled(s -> s.withColor(Formatting.GREEN)));
        }).dimensions(this.width/2-50, 80, 100, 20).build();

        addDrawableChild(healthManage);
        addDrawableChild(bungoBot);
        addDrawableChild(reinitCommands);
    }
}
