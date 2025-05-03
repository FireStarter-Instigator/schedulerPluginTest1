package net.runelite.client.plugins.microbot.chickenJockey;

import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.player.Rs2Player;

import java.util.concurrent.TimeUnit;

import static net.runelite.client.plugins.microbot.util.walker.Rs2Walker.walkTo;

public class ChickenScript extends Script {

    public static boolean test = false;
    public final WorldArea CHICKEN_AREA = new WorldArea(2944, 3816, 15, 16, 0);
    public final WorldPoint CHICKEN_POINT = new WorldPoint(3210, 9898, 0);

    public boolean run(ChickenConfig config) {
        Microbot.enableAutoRunOn = false;
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn()) return;
                if (!super.run()) return;
                long startTime = System.currentTimeMillis();

                if (!CHICKEN_AREA.contains(Rs2Player.getWorldLocation())) {
                   walkTo(CHICKEN_POINT);
                } else {
                    if (!Rs2Player.isInCombat()) {
                        Rs2Npc.attack("Chicken");}
                }

                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;
                System.out.println("Total time for loop " + totalTime);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
        return true;
    }
    
    @Override
    public void shutdown() {
        super.shutdown();
    }
}