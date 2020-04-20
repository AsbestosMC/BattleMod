package battlemod.utility;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.*;

public class StackManager {
	public static void initialize() {
	}

	public static void sort(Inventory inventory) {
		TreeMap<String, ArrayList<ItemStack>> byType=  new TreeMap<>();

		for (int i = 0; i < inventory.getInvSize(); ++i) {
			ItemStack stack = inventory.getInvStack(i);

			if (!byType.containsKey(stack.getName().asString()) && !stack.isEmpty()) {
				byType.put(stack.getName().asString(), new ArrayList<>());
			}

			if (!stack.isEmpty()) {
				byType.get(stack.getName().asString()).add(stack.copy());
			}

			inventory.setInvStack(i, ItemStack.EMPTY);
		}

		int i = 0;
		for (Map.Entry<String, ArrayList<ItemStack>> type : byType.entrySet()) {
			ArrayList<ItemStack> stacks = type.getValue();
			boolean finished = false;
			for (int k = 0; k < 128 && !finished; ++k) {
				for (int l = 0; l < stacks.size(); ++l) {
					boolean moved = false;
					if (l < stacks.size() - 1) {
						ItemStack cur = stacks.get(l);
						ItemStack nex = stacks.get(l + 1);

						if (cur.getCount() < nex.getCount()) {
							stacks.set(l + 1, cur);
							stacks.set(l, nex);
							moved = true;
						}
					}
					if (l == stacks.size() - 1 && !moved) {
						finished = true;
					}
				}
			}

			for (ItemStack stack : stacks) {
				inventory.setInvStack(i, stack);
				++i;
			}
		}
	}
}
