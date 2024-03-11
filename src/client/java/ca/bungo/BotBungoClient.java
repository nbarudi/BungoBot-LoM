package ca.bungo;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalGetToBlock;
import baritone.api.pathing.goals.GoalStrictDirection;
import baritone.api.utils.BlockOptionalMeta;
import ca.bungo.bungobot.CommandManager;
import ca.bungo.data.MenuEventsHandler;
import ca.bungo.data.TickEventsHandler;
import ca.bungo.data.farmers.AutoSellFish;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.math.Box;
import org.apache.http.impl.conn.ConnectionShutdownException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntConsumer;

public class BotBungoClient implements ClientModInitializer {

	private static final ScheduledThreadPoolExecutor EXECUTOR_SERVICE = new ScheduledThreadPoolExecutor(1);
	private final AtomicReference<ScheduledFuture<?>> countdown = new AtomicReference<>(null);
	public static ServerInfo serverInfo;
	private static BotBungoClient instance;

	static {
		EXECUTOR_SERVICE.setRemoveOnCancelPolicy(true);
		EXECUTOR_SERVICE.allowCoreThreadTimeOut(true);
	}

	@Override
	public void onInitializeClient() {
		BotBungo.LOGGER.info("Loaded The Modz!");
		instance = this;

		TickEventsHandler.loadTickEvents();
		MenuEventsHandler.initEvents();
		CommandManager.initCommands();

		ScreenEvents.AFTER_INIT.register(((client, screen, scaledWidth, scaledHeight) -> {
			if(!(screen instanceof GameMenuScreen)) return;

//			Screens.getButtons(screen).add(ButtonWidget.builder(Text.of("Testing Button"), button ->{
//				schedule(AutoSellFish::startBot, 1, TimeUnit.MILLISECONDS);
//			}).position(0, 20).build());


		}));
	}

	public void cancelCountdown(){
		synchronized (countdown) {
			if(countdown.get() == null) return;
			countdown.getAndSet(null).cancel(true);
		}
	}

	public void startCoutdown(final IntConsumer callback){
		int delay = 10;
		countdown(delay, callback);
	}

	public void reconnect(){
		if(serverInfo == null) return;
		ConnectScreen.connect(
				new MultiplayerScreen(new TitleScreen()),
				MinecraftClient.getInstance(),
				ServerAddress.parse(serverInfo.address),
				serverInfo,
				false);
	}

	private void countdown(int seconds, final IntConsumer callback){
		if(seconds == 0){
			MinecraftClient.getInstance().execute(this::reconnect);
			return;
		}
		callback.accept(seconds);
		synchronized (countdown){
			countdown.set(schedule(() -> countdown(seconds-1, callback), 1, TimeUnit.SECONDS));
		}
	}

	public static ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit timeUnit) {
		return EXECUTOR_SERVICE.schedule(command, delay, timeUnit);
	}


	public static BotBungoClient getInstance(){
		return instance;
	}

	public static Optional<ButtonWidget> findBackButton(Screen screen) {
		for (Element element : screen.children()) {
			if (!(element instanceof ButtonWidget button)) continue;

			String translatableKey;
			if (button.getMessage() instanceof TranslatableTextContent translatable) {
				translatableKey = translatable.getKey();
			} else if (button.getMessage().getContent() instanceof TranslatableTextContent translatable) {
				translatableKey = translatable.getKey();
			} else continue;

			// check for gui.back, gui.toMenu, gui.toRealms, gui.toTitle, gui.toWorld (only ones starting with "gui.to")
			if (translatableKey.equals("gui.back") || translatableKey.startsWith("gui.to")) {
				return Optional.of(button);
			}
		}
		return Optional.empty();
	}
}