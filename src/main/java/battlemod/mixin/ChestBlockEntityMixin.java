package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.accessor.ChestBlockEntityAccessor;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin implements ChestBlockEntityAccessor {
	@Unique
	boolean isLootChest = false;

	@Override
	public void setLootChest(boolean isLootChest) {
		this.isLootChest = isLootChest;
	}

	@Override
	public boolean isLootChest() {
		return isLootChest;
	}

	@Inject(at = @At("HEAD"), method = "fromTag(Lnet/minecraft/nbt/CompoundTag;)V")
	void onFromTag(CompoundTag tag, CallbackInfo callbackInformation) {
		this.isLootChest = tag.getBoolean("is_loot_chest");
	}

	@Inject(at = @At("HEAD"), method = "toTag(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;")
	void onToTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> callbackInformationReturnable) {
		tag.putBoolean("is_loot_chest", isLootChest);
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, ChestBlockEntityMixin.class.getName() + " Mixin applied.");
	}
}
