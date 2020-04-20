package battlemod.utility;

import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class TeamManager {
	public static HashMap<String, HashMap<ServerPlayerEntity, Boolean>> TEAM_STATUS = new HashMap<>();

	public static void initialize() {

	}
}
