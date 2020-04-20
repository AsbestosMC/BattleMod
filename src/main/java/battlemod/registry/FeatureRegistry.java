package battlemod.registry;

import battlemod.feature.LootChestFeature;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;

public class FeatureRegistry {
	public static final Feature<DefaultFeatureConfig> LOOT_CHEST = Registry.register(
			Registry.FEATURE,
			new Identifier("battlemod", "loot_chest"),
			new LootChestFeature(DefaultFeatureConfig::deserialize)
	);

	public static void initialize() {
		Registry.BIOME.forEach(biome -> biome.addFeature(
				GenerationStep.Feature.RAW_GENERATION,
				LOOT_CHEST.configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(2)))
		));
	}
}
