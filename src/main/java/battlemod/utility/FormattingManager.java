package battlemod.utility;

import net.minecraft.util.Formatting;

import java.util.HashMap;

public class FormattingManager {
	public static final HashMap<Formatting, Integer> COLORS = new HashMap<Formatting, Integer>() {{
			put(Formatting.BLACK, 0);
			put(Formatting.DARK_BLUE, 170);
			put(Formatting.DARK_GREEN, 43520);
			put(Formatting.DARK_AQUA, 43690);
			put(Formatting.DARK_RED, 11141120);
			put(Formatting.DARK_PURPLE, 11141290);
			put(Formatting.GOLD, 16755200);
			put(Formatting.GRAY, 11184810);
			put(Formatting.DARK_GRAY, 5592405);
			put(Formatting.BLUE, 5592575);
			put(Formatting.GREEN, 5635925);
			put(Formatting.AQUA, 5636095);
			put(Formatting.RED, 16733525);
			put(Formatting.LIGHT_PURPLE, 16733695);
			put(Formatting.YELLOW, 16777045);
			put(Formatting.WHITE, 16777215);
	}};

	public static final HashMap<String, Formatting> NAMES = new HashMap<String, Formatting>() {{
		put("black", Formatting.BLACK);
		put("dark_blue", Formatting.DARK_BLUE);
		put("dark_green", Formatting.DARK_GREEN);
		put("dark_aqua", Formatting.DARK_AQUA);
		put("dark_red", Formatting.DARK_RED);
		put("dark_purple", Formatting.DARK_PURPLE);
		put("gold", Formatting.GOLD);
		put("gray", Formatting.GRAY);
		put("dark_gray", Formatting.DARK_GRAY);
		put("blue", Formatting.BLUE);
		put("green", Formatting.GREEN);
		put("aqua", Formatting.AQUA);
		put("red", Formatting.RED);
		put("light_purple", Formatting.LIGHT_PURPLE);
		put("yellow", Formatting.YELLOW);
		put("white", Formatting.WHITE);
	}};

	public static void initialize() {
	}
}
