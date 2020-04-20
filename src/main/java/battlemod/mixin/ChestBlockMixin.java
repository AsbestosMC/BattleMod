package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.accessor.ChestBlockEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public class ChestBlockMixin {
	@Inject(at = @At("HEAD"), cancellable = true, method = "onUse(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;")
	void onUse(BlockState state, World world, BlockPos position, PlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> callbackInformationReturnable) {
		if (world.getBlockEntity(position) instanceof ChestBlockEntityAccessor) {
			ChestBlockEntityAccessor accessor = (ChestBlockEntityAccessor) world.getBlockEntity(position);
			if (accessor.isLootChest()) {
				world.breakBlock(position, false, player);
				state.getBlock().afterBreak(world, player, position, state, (BlockEntity) accessor, ItemStack.EMPTY);
				callbackInformationReturnable.setReturnValue(ActionResult.PASS);
				callbackInformationReturnable.cancel();
			}
		}
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, ChestBlockMixin.class.getName() + " Mixin applied.");
	}
}
