package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.accessor.ChestBlockEntityAccessor;
import battlemod.utility.FormattingManager;
import battlemod.utility.LootManager;
import battlemod.utility.VeinMiner;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(Block.class)
public class BlockMixin {
	@Shadow @Final protected float hardness;

	@Inject(at = @At("HEAD"), cancellable = true, method = "afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V")
	void beforeBreak(World world, PlayerEntity player, BlockPos initialPosition, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo callbackInformation) {
		if (world.isClient) return;

		if (state.getBlock() == Blocks.CHEST) {
			if (((ChestBlockEntityAccessor) blockEntity).isLootChest()) {
				FireworkEntity fireworkEntity;
				try {
					fireworkEntity = new FireworkEntity(world, initialPosition.getX(), initialPosition.getY() + 1, initialPosition.getZ(), LootManager.FIREWORKS.get(FormattingManager.NAMES.get(player.getScoreboardTeam().getName().toLowerCase())));
				} catch (Exception exception) {
					fireworkEntity = new FireworkEntity(world, initialPosition.getX(), initialPosition.getY() + 1, initialPosition.getZ(), LootManager.FIREWORKS.get(Formatting.WHITE));
				}

				world.spawnEntity(fireworkEntity);

				for (int i = 0; i < Math.max(1, world.random.nextInt(3)); ++i) {
					int index = world.random.nextInt(LootManager.WHITELIST.size());
					Iterator<ItemStack[]> stackIterator = LootManager.WHITELIST.iterator();
					for (int k = 0; k < index; ++k) {
						stackIterator.next();
					}

					for (ItemStack randomStack : stackIterator.next()) {
						ItemScatterer.spawn(world, initialPosition, DefaultedList.copyOf(ItemStack.EMPTY, randomStack));
					}
				}
				callbackInformation.cancel();
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V")
	void onBreak(World world, PlayerEntity player, BlockPos initialPosition, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo callbackInformation) {
		if (world.isClient) return;

		if (state.getBlock() == Blocks.CHEST) {
			if (((ChestBlockEntityAccessor) blockEntity).isLootChest()) {
				for (int i = 0; i < world.random.nextInt(8); ++i) {
					ItemScatterer.spawn(world, initialPosition, DefaultedList.copyOf(ItemStack.EMPTY, new ItemStack(Registry.ITEM.getRandom(world.random))));
				}

				return;
			}
		}

		stack = player.getStackInHand(player.getActiveHand());

		LootContext.Builder builder = (new LootContext.Builder((ServerWorld) world)).setRandom(world.random).put(LootContextParameters.POSITION, initialPosition).put(LootContextParameters.TOOL, stack).putNullable(LootContextParameters.THIS_ENTITY, player).putNullable(LootContextParameters.BLOCK_ENTITY, blockEntity);

		VeinMiner.tryVeinMine((predicateState) -> {
			return predicateState.matches(BlockTags.LOGS);
		}, (predicateState) -> {
			return predicateState.getBlock() == state.getBlock();
		}, (predicateWorld, predicatePlayer, predicateStack) -> {
			predicateStack.damage(1, predicatePlayer, entity -> entity.sendToolBreakStatus(entity.getActiveHand()));
		}, (predicateWorld, predicatePlayer, predicatePosition) -> {
			ItemScatterer.spawn(predicateWorld, predicatePosition, DefaultedList.copyOf(ItemStack.EMPTY, predicateWorld.getBlockState(predicatePosition).getDroppedStacks(builder).toArray(new ItemStack[0])));
			predicateWorld.removeBlock(predicatePosition, false);
		}, world, player, initialPosition, state, stack);

		VeinMiner.tryVeinMine((predicateState) -> {
			return predicateState.getBlock() instanceof OreBlock || predicateState.getBlock() instanceof RedstoneOreBlock;
		}, (predicateState) -> {
			return predicateState.getBlock() == state.getBlock();
		}, (predicateWorld, predicatePlayer, predicateStack) -> {
			predicateStack.damage(1, predicatePlayer, entity -> entity.sendToolBreakStatus(entity.getActiveHand()));
		}, (predicateWorld, predicatePlayer, predicatePosition) -> {
			ItemScatterer.spawn(predicateWorld, predicatePosition, DefaultedList.copyOf(ItemStack.EMPTY, predicateWorld.getBlockState(predicatePosition).getDroppedStacks(builder).toArray(new ItemStack[0])));
			predicateWorld.removeBlock(predicatePosition, false);
		}, world, player, initialPosition, state, stack);
	}

	/**
	 * @author vini2003
	 */
	@Deprecated
	@Overwrite
	public float getHardness(BlockState state, BlockView world, BlockPos position) {
		if (world.getBlockEntity(position) instanceof ChestBlockEntityAccessor) {
			ChestBlockEntityAccessor accessor = (ChestBlockEntityAccessor) world.getBlockEntity(position);
			if (accessor.isLootChest()) return 0;
		}
		return this.hardness;
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, BlockMixin.class.getName() + " Mixin applied.");
	}
}
