package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.accessor.SlotAccessor;
import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Slot.class)
public class SlotMixin implements SlotAccessor {
	@Shadow @Final private int invSlot;

	@Override
	public int getNumber() {
		return this.invSlot;
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, SlotMixin.class.getName() + " Mixin applied.");
	}
}
