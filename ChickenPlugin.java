package net.runelite.client.plugins.microbot.chickenJockey;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.pluginscheduler.api.SchedulablePlugin;
import net.runelite.client.plugins.microbot.pluginscheduler.condition.logical.LogicalCondition;
import net.runelite.client.plugins.microbot.pluginscheduler.event.PluginScheduleEntrySoftStopEvent;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.awt.*;

@PluginDescriptor(
        name = PluginDescriptor.Default + "ChickenKillerExample",
        description = "Scheduler Chicken Killer Example",
        tags = {"example", "microbot", "chicken", "Scheduler"},
        enabledByDefault = false
)
@Slf4j
public class ChickenPlugin extends Plugin implements SchedulablePlugin {
    @Inject
    private ChickenConfig config;
    @Provides
    ChickenConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ChickenConfig.class);
    }

    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ChickenOverlay chickenOverlay;

    @Inject
    ChickenScript chickenScript;


    @Override
    protected void startUp() throws AWTException {
        if (overlayManager != null) {
            overlayManager.add(chickenOverlay);
        }
        chickenScript.run(config);
    }
    
    @Override
    public LogicalCondition getStopCondition() {
        return null;
    }

    @Override
    @Subscribe
    public void onPluginScheduleEntrySoftStopEvent(PluginScheduleEntrySoftStopEvent event) {

        if (event.getPlugin() == this) {                      
            // Schedule the stop operation on the client thread
            Microbot.getClientThread().invokeLater(() -> {
                try {                    
                    Microbot.getPluginManager().setPluginEnabled(this, false);
                    Microbot.getPluginManager().stopPlugin(this);
                } catch (Exception e) {
                    log.error("Error stopping plugin", e);
                }
            });
        }
    }

    protected void shutDown() {
        chickenScript.shutdown();
        overlayManager.remove(chickenOverlay);
    }

}
