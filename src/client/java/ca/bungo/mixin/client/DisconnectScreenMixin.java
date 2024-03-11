package ca.bungo.mixin.client;

import ca.bungo.BotBungoClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Mixin(DisconnectedScreen.class)
public class DisconnectScreenMixin extends Screen {

    public DisconnectScreenMixin(Text title){
        super(title);
    }

    @Unique
    private ButtonWidget reconnectButton, cancelButton, backButton;
    @Unique
    private boolean shouldReconnect = true;

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci){
        backButton = BotBungoClient.findBackButton(this)
                .orElseThrow(() -> new NoSuchElementException("Couldn't find the back button on the disconnect screen"));
        reconnectButton = ButtonWidget.builder(
                        Text.of("Reconnect"),
                        btn -> BotBungoClient.schedule(() -> MinecraftClient.getInstance().execute(this::manualReconnect), 100, TimeUnit.MILLISECONDS))
                .dimensions(0, 0, 0, 20).build();

        reconnectButton.setX(backButton.getX());
        reconnectButton.setY(backButton.getX());

        if(shouldReconnect){
            reconnectButton.setWidth(backButton.getWidth() - backButton.getHeight() - 4);

            cancelButton = ButtonWidget.builder(
                            Text.literal("X")
                                    .styled(s -> s.withColor(Formatting.RED)),
                            btn -> cancelCountdown())
                    .dimensions(
                            backButton.getX() + backButton.getWidth() - backButton.getHeight(),
                            backButton.getY(),
                            backButton.getHeight(),
                            backButton.getHeight())
                    .build();
            addDrawableChild(cancelButton);
        }else {
            reconnectButton.setWidth(backButton.getWidth());
        }
        addDrawableChild(reconnectButton);
        backButton.setY(backButton.getY() + backButton.getHeight() + 4);

        if(shouldReconnect){
            BotBungoClient.getInstance().startCoutdown(this::countdownCallback);
        }
    }

    @Unique
    private void manualReconnect(){
        BotBungoClient.getInstance().cancelCountdown();
        BotBungoClient.getInstance().reconnect();
    }

    @Unique
    private void cancelCountdown(){
        BotBungoClient.getInstance().cancelCountdown();
        remove(cancelButton);
        shouldReconnect = false;
        reconnectButton.active = true;
        reconnectButton.setMessage(Text.of("Reconnect"));
        reconnectButton.setWidth(backButton.getWidth());
    }

    @Unique
    private void countdownCallback(int seconds) {
        if (seconds < 0) {
            // indicates that we're out of attempts
            reconnectButton.setMessage(Text.of("Reconnection-Failed"));
            reconnectButton.active = false;
        } else {
            reconnectButton.setMessage(Text.of("Reconnecting in " + seconds + " seconds!"));
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256 && shouldReconnect) {
            cancelCountdown();
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

}
