package battlemod.utility;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.function.Predicate;

public class VeinMiner {
	public static void tryVeinMine(Predicate<BlockState> acceptPredicate, Predicate<BlockState> continuePredicate, TriConsumer<World, PlayerEntity, ItemStack> afterAction, TriConsumer<World, PlayerEntity, BlockPos> forEachPosition, World world, PlayerEntity player, BlockPos initialPosition, BlockState state, ItemStack stack) {
		HashSet<BlockPos> cache = new HashSet<>();

		if (acceptPredicate.test(state) && player.isSneaking() && stack.getItem().canMine(state, world, initialPosition, player)) {
			ArrayDeque<BlockPos> positions = new ArrayDeque<>();
			positions.add(initialPosition);
			cache.add(initialPosition);

			while (!positions.isEmpty() && !stack.isEmpty() && cache.size() < 512) {
				BlockPos position = positions.getLast();
				positions.removeLast();

				for (Direction direction : Direction.values()) {
					BlockPos offsetPosition = position.offset(direction);

					if (cache.contains(offsetPosition)) continue;

					if (continuePredicate.test(world.getBlockState(offsetPosition))) {
						positions.add(offsetPosition);
					}
				}

				afterAction.accept(world, player, stack);
				forEachPosition.accept(world, player, position);
			}
		}
	}
}
