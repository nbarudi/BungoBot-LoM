package ca.bungo.mixin.client;

import ca.bungo.bungobot.CommandManager;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

@Mixin(MessageHandler.class)
public class ChatSystemMixin {

    @Inject(method = "addToChatLog(Lnet/minecraft/text/Text;Ljava/time/Instant;)V", at = @At("HEAD"), cancellable = true)
    public void addToChatLog(Text message, Instant timestamp, CallbackInfo ci){
        CommandManager.handleChatMessage(message, timestamp, ci);
    }

}
