package ca.bungo.data;

import baritone.api.BaritoneAPI;
import baritone.api.cache.IWaypoint;
import baritone.api.cache.Waypoint;
import baritone.api.utils.BetterBlockPos;
import ca.bungo.BotBungo;

public enum WaypointInfo {

    JUNK_SELLER("Junk Seller", "junksell", new int[]{4501, 57, 4808}),
    CURRENCY_CONVERT("Money Man", "convert", new int[]{4481, 58, 4850}),
    MUK_SPOT("Muk Spot", "mukspot", new int[]{4524, 57, 4808}),
    WATERING_HOLE("Watering Hole", "wateringhole", new int[]{4573, 57, 4776});

    private final String intName;
    private final String waypointName;
    private final int[] defaultLocation;
    WaypointInfo(String intName, String waypointName, int[] defaultLocation){
        this.intName = intName;
        this.waypointName = waypointName;
        this.defaultLocation = defaultLocation;
    }

    private boolean hasWaypoint(String waypointName){
        for(IWaypoint waypoint : BaritoneAPI.getProvider().getPrimaryBaritone().getWorldProvider()
                .getCurrentWorld().getWaypoints().getAllWaypoints()){
            if(waypoint.getName().equalsIgnoreCase(waypointName)) return true;
        }
        return false;
    }

    public void pathToWaypoint(){
        if(!hasWaypoint(this.intName)){
            BaritoneAPI.getProvider().getPrimaryBaritone().getWorldProvider()
                    .getCurrentWorld().getWaypoints().addWaypoint(
                    new Waypoint(this.intName, IWaypoint.Tag.USER,
                            new BetterBlockPos(defaultLocation[0], defaultLocation[1], defaultLocation[2]))
            );
        }
        BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("wp goto " + waypointName);

    }

    public static WaypointInfo getWaypointInfo(String waypointName){
        for(WaypointInfo info : WaypointInfo.values()){
            if(info.intName.equalsIgnoreCase(waypointName)) return info;
        }
        return null;
    }
}
