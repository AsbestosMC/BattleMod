package battlemod;

import battlemod.registry.CallbackRegistry;
import battlemod.registry.FeatureRegistry;
import battlemod.utility.FormattingManager;
import battlemod.utility.LootManager;
import battlemod.utility.StackManager;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BattleMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("BattleMod");

	@Override
	public void onInitialize() {
		FeatureRegistry.initialize();
		CallbackRegistry.initialize();
		LootManager.initialize();
		StackManager.initialize();
		FormattingManager.initialize();
	}
}
