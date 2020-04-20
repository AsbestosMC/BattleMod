package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.accessor.AbstractFurnaceBlockEntityAccessor;
import battlemod.accessor.ItemUtil;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
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

@Mixin(Item.class)
public class ItemMixin {
	@Inject(at = @At("HEAD"), method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;")
	void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> callbackInformationReturnable) {
		BlockHitResult hitResult = (BlockHitResult) ItemUtil.rayTrace(world, user, RayTraceContext.FluidHandling.ANY);

		BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());

		if (blockEntity instanceof AbstractFurnaceBlockEntity) {
			AbstractFurnaceBlockEntityAccessor accessor = ((AbstractFurnaceBlockEntityAccessor) blockEntity);

			if (user.getStackInHand(hand).getItem() == Items.BLAZE_POWDER) {
				accessor.setBoost(accessor.getBoost() + 512);

				if (!world.isClient) {
					user.getStackInHand(hand).decrement(1);
				}
			} else if (user.getStackInHand(hand).getItem() == Items.BONE_MEAL) {
				accessor.setBoost(accessor.getBoost() + 256);

				if (!world.isClient) {
					user.getStackInHand(hand).decrement(1);
				}
			}

		}
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, ItemMixin.class.getName() + " Mixin applied.");
	}
}
