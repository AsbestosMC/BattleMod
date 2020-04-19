package borges.mixin;

import borges.utility.VeinMiner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
	@Inject(at = @At("HEAD"), method = "afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/item/ItemStack;)V")
	void onBreak(World world, PlayerEntity player, BlockPos initialPosition, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci) {
		if (world.isClient) return;

		LootContext.Builder builder = (new LootContext.Builder((ServerWorld) world)).setRandom(world.random).put(LootContextParameters.POSITION, initialPosition).put(LootContextParameters.TOOL, stack).putNullable(LootContextParameters.THIS_ENTITY, player).putNullable(LootContextParameters.BLOCK_ENTITY, blockEntity);

		VeinMiner.tryVeinmine((predicateState) -> {
			return predicateState.matches(BlockTags.LOGS);
		}, (predicateState) -> {
			return predicateState.getBlock() == state.getBlock();
		}, (predicateWorld, predicatePlayer, predicateStack) -> {
			predicateStack.damage(1, predicateWorld.random, (ServerPlayerEntity) predicatePlayer);
		}, (predicateWorld, predicatePlayer, predicatePosition) -> {
			ItemScatterer.spawn(predicateWorld, predicatePosition, DefaultedList.copyOf(ItemStack.EMPTY, predicateWorld.getBlockState(predicatePosition).getDroppedStacks(builder).toArray(new ItemStack[0])));
			predicateWorld.removeBlock(predicatePosition, false);
		}, world, player, initialPosition, state, stack);

		VeinMiner.tryVeinmine((predicateState) -> {
			return predicateState.getBlock() instanceof OreBlock || predicateState.getBlock() instanceof RedstoneOreBlock;
		}, (predicateState) -> {
			return predicateState.getBlock() == state.getBlock();
		}, (predicateWorld, predicatePlayer, predicateStack) -> {
			predicateStack.damage(1, predicateWorld.random, (ServerPlayerEntity) predicatePlayer);
		}, (predicateWorld, predicatePlayer, predicatePosition) -> {
			ItemScatterer.spawn(predicateWorld, predicatePosition, DefaultedList.copyOf(ItemStack.EMPTY, predicateWorld.getBlockState(predicatePosition).getDroppedStacks(builder).toArray(new ItemStack[0])));
			predicateWorld.removeBlock(predicatePosition, false);
		}, world, player, initialPosition, state, stack);

		player.inventory.markDirty();

		if (state.getBlock() instanceof OreBlock || state.getBlock() instanceof RedstoneOreBlock) {

		}
	}
}
