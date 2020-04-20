package battlemod.utility;

import battlemod.BattleMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class LootManager {
	public static void initialize() {
	}

	private static final Random random = new Random();

	public static HashSet<ItemStack[]> WHITELIST = new HashSet<ItemStack[]>() {{
		add(new ItemStack[] { new ItemStack(Items.COAL, 6) });
		add(new ItemStack[] { new ItemStack(Items.IRON_INGOT, 6) });
		add(new ItemStack[] { new ItemStack(Items.GOLD_INGOT, 6) });
		add(new ItemStack[] { new ItemStack(Items.DIAMOND, 3) });
		add(new ItemStack[] { new ItemStack(Items.EMERALD, 3) });
		add(new ItemStack[] { new ItemStack(Items.COBBLESTONE, 16) });
		add(new ItemStack[] { new ItemStack(Items.DIORITE, 16) });
		add(new ItemStack[] { new ItemStack(Items.GRANITE, 16) });
		add(new ItemStack[] { new ItemStack(Items.ANDESITE, 16) });
		add(new ItemStack[] { new ItemStack(Items.OAK_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.DARK_OAK_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.BIRCH_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.SPRUCE_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.JUNGLE_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.ACACIA_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.STRIPPED_OAK_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.STRIPPED_DARK_OAK_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.STRIPPED_BIRCH_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.STRIPPED_SPRUCE_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.STRIPPED_JUNGLE_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.STRIPPED_ACACIA_LOG, 16) });
		add(new ItemStack[] { new ItemStack(Items.OAK_PLANKS, 64) });
		add(new ItemStack[] { new ItemStack(Items.DARK_OAK_PLANKS, 64) });
		add(new ItemStack[] { new ItemStack(Items.BIRCH_PLANKS, 64) });
		add(new ItemStack[] { new ItemStack(Items.SPRUCE_PLANKS, 64) });
		add(new ItemStack[] { new ItemStack(Items.JUNGLE_PLANKS, 64) });
		add(new ItemStack[] { new ItemStack(Items.ACACIA_PLANKS, 64) });
		add(new ItemStack[] { new ItemStack(Items.TNT, 3), new ItemStack(Items.FLINT_AND_STEEL, 1) });
		add(new ItemStack[] { new ItemStack(Items.OBSIDIAN, 4) });
		add(new ItemStack[] { new ItemStack(Items.ENCHANTING_TABLE, 1) });
		add(new ItemStack[] { new ItemStack(Items.BOOKSHELF, 8)} );
	}};

	private static String BASE_FIREWORK_TAG_STRING = "{Fireworks:{Flight:4,Explosions:[{Type:1,Flicker:1,Trail:1,Colors:[I;COLOUR]}]}}";

	public static HashMap<Formatting, ItemStack> FIREWORKS = new HashMap<>();

	static {
		AtomicInteger totalPotions = new AtomicInteger();
		Registry.POTION.forEach(potion -> {
			if (random.nextBoolean()) {
				WHITELIST.add(new ItemStack[] {PotionUtil.setPotion(new ItemStack(Items.POTION), potion)} );
				WHITELIST.add(new ItemStack[] {PotionUtil.setPotion(new ItemStack(Items.SPLASH_POTION), potion)} );
				WHITELIST.add(new ItemStack[] {PotionUtil.setPotion(new ItemStack(Items.LINGERING_POTION), potion)} );
				totalPotions.addAndGet(3);
			}
		});

		AtomicInteger totalEnchantments = new AtomicInteger();
		Registry.ENCHANTMENT.forEach(enchantment -> {
			if (random.nextBoolean()) {
				for (int i = enchantment.getMinimumLevel(); i < enchantment.getMaximumLevel(); ++i) {
					ItemStack bookStack = new ItemStack(Items.ENCHANTED_BOOK);
					int finalI = i;
					EnchantmentHelper.set(new HashMap<Enchantment, Integer>() {{
						put(enchantment, finalI);
					}}, bookStack);
					WHITELIST.add(new ItemStack[] { bookStack } );
					totalEnchantments.addAndGet(1);
				}
			}
		});

		int totalFireworks = 0;
		for (Formatting formatting : Formatting.values()) {
			if (formatting.isColor()) {
				try {
					int colorValue = FormattingManager.COLORS.get(formatting);
					String fireworkTagString = BASE_FIREWORK_TAG_STRING.replace("COLOUR", String.valueOf(colorValue));
					ItemStack fireworkStack = new ItemStack(Items.FIREWORK_ROCKET);
					fireworkStack.setTag(StringNbtReader.parse(fireworkTagString));
					FIREWORKS.put(formatting, fireworkStack);
					totalFireworks += 1;
				} catch (Exception ignored) {
				}
			}
		}

		BattleMod.LOGGER.log(Level.INFO, "Initialized loot chest firework effect variations: " + totalFireworks + " total!");
		BattleMod.LOGGER.log(Level.INFO, "Initialized loot chest enchantment book loot variations: " + totalEnchantments + " total!");
		BattleMod.LOGGER.log(Level.INFO, "Initialized loot chest potion loot variations: " + totalPotions + " total!");
		BattleMod.LOGGER.log(Level.INFO, "Initialized loot chest whitelist: " + WHITELIST.size() + " total!");
	}
}
