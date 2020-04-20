package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.accessor.ItemExtensorAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin {
	@Inject(at = @At("HEAD"), cancellable = true, method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;")
	void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> callbackInformationReturnable) {
		BlockHitResult hitResult = (BlockHitResult) ItemExtensorAccessor.rayTrace(world, user, RayTraceContext.FluidHandling.ANY);

		BlockState state = world.getBlockState(hitResult.getBlockPos());

		if (state.getBlock() instanceof CauldronBlock) {
			int level = state.get(CauldronBlock.LEVEL);

			if (level == 3 && user.getStackInHand(hand).getItem() == Items.LAVA_BUCKET) {
				ItemScatterer.spawn(world, hitResult.getBlockPos(), DefaultedList.copyOf(ItemStack.EMPTY, new ItemStack(Items.OBSIDIAN)));
				user.setStackInHand(hand, new ItemStack(Items.BUCKET));
				world.setBlockState(hitResult.getBlockPos(), state.with(CauldronBlock.LEVEL, 0));
				callbackInformationReturnable.setReturnValue(new TypedActionResult<>(ActionResult.SUCCESS, user.getStackInHand(hand)));
				callbackInformationReturnable.cancel();
			}
		}
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, BucketItemMixin.class.getName() + " Mixin applied.");
	}
}
