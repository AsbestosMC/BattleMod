package battlemod.feature;

import battlemod.accessor.ChestBlockEntityAccessor;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;
import java.util.function.Function;

public class LootChestFeature extends Feature<DefaultFeatureConfig> {
	public LootChestFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> configDeserializer) {
		super(configDeserializer);
	}

	@Override
	public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, BlockPos position, DefaultFeatureConfig config) {
		BlockPos surfacePosition = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, position);
		if (world.getBlockState(surfacePosition.offset(Direction.DOWN)).getBlock() instanceof FluidBlock) {
			return false;
		}
		world.setBlockState(surfacePosition, Blocks.CHEST.getDefaultState(), 1);
		ChestBlockEntityAccessor accessor = (ChestBlockEntityAccessor) world.getBlockEntity(surfacePosition);
		accessor.setLootChest(true);
		return true;
	}
}
