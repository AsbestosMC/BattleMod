package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.accessor.PlayerEntityAccessor;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityAccessor {
	@Shadow protected abstract void vanishCursedItems();

	@Shadow @Final public PlayerInventory inventory;

	@Unique
	private boolean isBattleFall = false;

	@Inject(at = @At("RETURN"), method = "dropInventory()V")
	void onDeath(CallbackInfo callbackInformation) {
		PlayerEntity entity = (PlayerEntity) (Object) this;

		ItemStack headStack = new ItemStack(Items.PLAYER_HEAD);
		CompoundTag headTag = new CompoundTag();
		headTag.putString("SkullOwner", entity.getGameProfile().getName());
		headStack.setTag(headTag);

		ItemScatterer.spawn(entity.world, entity.getBlockPos(), DefaultedList.copyOf(ItemStack.EMPTY, headStack));
	}

	/**
	 * @author vini2003
	 */
	@Overwrite
	public void dropInventory() {
		PlayerEntity entity = (PlayerEntity) (Object) this;

		BlockPos surfacePosition = entity.world.getTopPosition(Heightmap.Type.WORLD_SURFACE, entity.getBlockPos());

		for (DefaultedList<ItemStack> itemStacks : ImmutableList.of(inventory.main, inventory.armor, inventory.offHand)) {
			for (int i = 0; i < itemStacks.size(); ++i) {
				ItemStack itemStack = itemStacks.get(i);
				if (!itemStack.isEmpty()) {
					Block.dropStack(entity.world, surfacePosition, itemStack);
					itemStacks.set(i, ItemStack.EMPTY);
				}
			}
		}

		this.vanishCursedItems();

		entity.sendMessage(new LiteralText("You did at " + (int) entity.getX() + ", " + (int) entity.getY() + ", " + (int) entity.getZ() + "."));
		entity.sendMessage(new LiteralText("Your items were deposited at " + surfacePosition.getX() + ", " + surfacePosition.getY() + ", " + surfacePosition.getZ() + "."));
	}

	@Override
	public void setBattleFall(boolean isBattleFall) {
		this.isBattleFall = isBattleFall;
	}

	@Override
	public boolean isBattleFall() {
		return isBattleFall;
	}

	@Inject(at = @At("HEAD"), cancellable = true, method = "handleFallDamage(FF)Z")
	void onFall(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Boolean> callbackInformationReturnable) {
		if (isBattleFall()) {
			setBattleFall(false);
			callbackInformationReturnable.setReturnValue(false);
			callbackInformationReturnable.cancel();
		}
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, PlayerEntityMixin.class.getName() + " Mixin applied.");
	}
}
