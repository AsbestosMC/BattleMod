package battlemod.mixin;

import battlemod.BattleMod;
import battlemod.accessor.SlotAccessor;
import battlemod.utility.StackManager;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Container.class)
public abstract class ContainerMixin {
	@Shadow @Final public List<Slot> slots;

	@Shadow public abstract Slot getSlot(int index);

	@Shadow public abstract void sendContentUpdates();

	@Inject(at = @At("HEAD"), cancellable = true, method = "Lnet/minecraft/container/Container;onSlotClick(IILnet/minecraft/container/SlotActionType;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/item/ItemStack;")
	void onAction(int slotId, int clickData, SlotActionType actionType, PlayerEntity playerEntity, CallbackInfoReturnable<ItemStack> callbackInformationReturnable) {
		if (actionType == SlotActionType.PICKUP_ALL) {
			Inventory inventory = getSlot(slotId).inventory;
			int invSlot = ((SlotAccessor) getSlot(slotId)).getNumber();

			if (inventory instanceof PlayerInventory) {
				if (invSlot < 9) {
					BasicInventory basicInventory = new BasicInventory(9);
					for (int i = 0; i < 9 && i < inventory.getInvSize(); ++i) {
						basicInventory.setInvStack(i, inventory.getInvStack(i));
					}
					StackManager.sort(basicInventory);
					for (int i = 0; i < 9 && i < basicInventory.getInvSize(); ++i) {
						inventory.setInvStack(i, basicInventory.getInvStack(i));
					}
				} else if (invSlot < 36) {
					BasicInventory basicInventory = new BasicInventory(27);
					for (int i = 9; i < 36 && i < inventory.getInvSize(); ++i) {
						basicInventory.setInvStack(i - 9, inventory.getInvStack(i));
					}
					StackManager.sort(basicInventory);
					for (int i = 9; i < 36 && i < basicInventory.getInvSize() + 9; ++i) {
						inventory.setInvStack(i, basicInventory.getInvStack(i - 9));
					}
				}
			} else {
				StackManager.sort(inventory);
			}

			sendContentUpdates();

			callbackInformationReturnable.setReturnValue(ItemStack.EMPTY);
			callbackInformationReturnable.cancel();
		}
	}

	static {
		BattleMod.LOGGER.log(Level.INFO, ContainerMixin.class.getName() + " Mixin applied.");
	}
}
