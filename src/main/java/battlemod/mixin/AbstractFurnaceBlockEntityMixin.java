package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.accessor.AbstractFurnaceBlockEntityAccessor;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin implements AbstractFurnaceBlockEntityAccessor {
	@Shadow public abstract void tick();

	@Unique
	int boost = 0;

	@Unique
	long lastBoost = System.nanoTime();

	@Override
	public int getBoost() {
		return boost;
	}

	@Override
	public void setBoost(int boost) {
		this.boost = boost;
	}

	@Inject(at = @At("RETURN"), method = "tick()V")
	void onTick(CallbackInfo callbackInformation) {
		if (boost > 0 && System.nanoTime() - lastBoost > 500000) {
			lastBoost = System.nanoTime();
			--boost;
			tick();
		}
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, AbstractFurnaceBlockEntityMixin.class.getName() + " Mixin applied.");
	}
}
