package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.utility.StackManager;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.util.Tickable;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BarrelBlockEntity.class)
public class BarrelBlockEntityMixin implements Tickable {
	@Override
	public void tick() {
		StackManager.sort((BarrelBlockEntity) (Object) this);
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, BarrelBlockEntityMixin.class.getName() + " Mixin applied.");
	}
}
