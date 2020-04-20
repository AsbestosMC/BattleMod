package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.accessor.AbstractFurnaceBlockEntityAccessor;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin extends LockableContainerBlockEntity implements AbstractFurnaceBlockEntityAccessor {
	protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}

	@Shadow public abstract void tick();

	@Unique
	int boost = 0;

	@Unique
	boolean lock;

	@Inject(method = "toTag", at = @At("HEAD"))
	private void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
		tag.putInt("boost", this.boost);
	}

	@Inject(method = "fromTag", at = @At("HEAD"))
	private void fromTag(CompoundTag tag, CallbackInfo ci) {
		this.boost = tag.getInt("boost");
	}

	@Override
	public int getBoost() {
		return this.boost;
	}

	@Override
	public void setBoost(int boost) {
		this.boost = boost;
	}

	@Inject(at = @At("RETURN"), method = "tick()V")
	void onTick(CallbackInfo callbackInformation) {
		if(this.world != null && !this.lock) {
			this.lock = true;
			while (--this.boost > 0) {
				this.tick();
			}
			this.lock = false;
		}
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, AbstractFurnaceBlockEntityMixin.class.getName() + " Mixin applied.");
	}
}
