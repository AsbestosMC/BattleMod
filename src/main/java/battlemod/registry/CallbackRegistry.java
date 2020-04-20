package battlemod.registry;

import battlemod.BattleMod;
import battlemod.accessor.PlayerEntityAccessor;
import battlemod.utility.FormattingManager;
import battlemod.utility.TeamManager;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.Heightmap;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.stream.Collectors;

public class CallbackRegistry {
	private static final Random random = new Random();

	private static HashMap<String, Boolean> PLAYER_CACHE = new HashMap<>();

	public static boolean TEAM_INITIALIZED = false;

	public static boolean WARNING_DISPATCHED = false;

	public static ArrayList<String> TEAMS = new ArrayList<>();

	private static long TIME = 78000;

	public static void initialize() {
		ServerTickCallback.EVENT.register(callback -> {
			ArrayList<ServerPlayerEntity> playerList = (ArrayList<ServerPlayerEntity>) callback.getPlayerManager().getPlayerList();

			if (TIME == 78000) {
				int spawnX = random.nextInt(32000000);
				int spawnZ = random.nextInt(32000000);

				int spawnY = 256;

				int borderX = spawnX + (random.nextBoolean() ? -random.nextInt(512) : random.nextInt(512));
				int borderY = spawnY + (random.nextBoolean() ? -random.nextInt(512) : random.nextInt(512));

				int borderS = Math.max(384, random.nextInt(2048));

				callback.getCommandManager().execute(callback.getCommandSource(), "/setworldspawn " + spawnX + ", " + spawnY + ", " + spawnZ);
				callback.getCommandManager().execute(callback.getCommandSource(), "/worldborder set " + borderS);
				callback.getCommandManager().execute(callback.getCommandSource(), "/worldborder center " + borderX + " " + borderY);

				BattleMod.LOGGER.log(Level.INFO, "World spawn has been set to " + spawnX + ", " + spawnY + ", " + spawnZ + ".");
				BattleMod.LOGGER.log(Level.INFO, "World border size has been set to " + borderS + " blocks.");
				BattleMod.LOGGER.log(Level.INFO, "World border center has been set to " + borderX + ", " + borderY);

				playerList.forEach(player -> {
					int randomX = spawnX + (random.nextBoolean() ? random.nextInt(32) : -random.nextInt(32));
					int randomZ = spawnY + (random.nextBoolean() ? random.nextInt(32) : -random.nextInt(32));

					int randomY = 256;

					((PlayerEntityAccessor) player).setBattleFall(true);
					player.inventory.clear();
					player.interactionManager.setGameMode(GameMode.SURVIVAL);

					player.teleport(randomX, randomY, randomZ);
				});

			} else if (TIME == 72000) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/worldborder set 8 3600");
				callback.getCommandManager().execute(callback.getCommandSource(), "/worldborder set warning distance 64");
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in one hour!");

				BattleMod.LOGGER.log(Level.INFO, "World border reduction size set to 8, shrinking during 3600 seconds.");
				BattleMod.LOGGER.log(Level.INFO, "World border warning size set to 64 blocks.");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched one-hour warning.");
			} else if (TIME == 36000) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in thirty minutes!");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched thirty-minute warning.");
			} else if (TIME == 18000) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in fifteen minutes!");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched fifteen-minute warning.");
			} else if (TIME == 3600) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in five minute!");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched five-minute warning.");
			} else if (TIME == 1200) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in one minute!");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched one-minute warning.");
			} else if (TIME == 600) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in thirty seconds!");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched thirty-second warning.");
			} else if (TIME > 300) {
				TIME = 300;
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in fifteen seconds!");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched fifteen-second warning.");
			} else if (TIME == 100) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in five seconds!");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched five-second warning.");
			} else if (TIME == 60) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in three seconds!");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched three-second warning.");
			} else if (TIME == 40) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in two seconds!");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched two-second warning.");
			} else if (TIME == 20) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle will end in one second!");
				BattleMod.LOGGER.log(Level.INFO, "Dispatched one-second warning.");
			} else if (TIME <= 20 && TIME >= -100) {
				ArrayList<Map.Entry<String, HashMap<ServerPlayerEntity, Boolean>>> teamsAlive = new ArrayList<>();
				TeamManager.TEAM_STATUS.entrySet().forEach(entry -> {
					if (entry.getValue().values().stream().anyMatch(Boolean::booleanValue)) {
						teamsAlive.add(entry);
					}
				});
				if (teamsAlive.size() == 1) {
					String playersWon = teamsAlive.get(0).getValue().keySet().stream().map(informationPlayer -> (informationPlayer.interactionManager.getGameMode() == GameMode.SPECTATOR ? "\\u00A7c" : "\\u00A7a") + informationPlayer.getName().asFormattedString()).collect(Collectors.joining(","));
					callback.getCommandManager().execute(callback.getCommandSource(), "/title @a times 20 600 20");
					callback.getCommandManager().execute(callback.getCommandSource(), "/title @a title " + "\"\\u00A76 The game has finished!\"");
					callback.getCommandManager().execute(callback.getCommandSource(), "/title @a subtitle \"Winners: " + playersWon + "\"");
					BattleMod.LOGGER.log(Level.INFO, "Battle finalized with last team alive composed of " + playersWon + ".");

					TIME = -101;
				}
				if (!WARNING_DISPATCHED) {
					callback.getCommandManager().execute(callback.getCommandSource(), "/say You have five minutes to fight to death, or everyone loses!");

					WARNING_DISPATCHED = true;
				}
			} else if (TIME < -100) {
				callback.getCommandManager().execute(callback.getCommandSource(), "/say The battle has ended, with no winners!");
				BattleMod.LOGGER.log(Level.INFO, "Battle finished with no winners, multiple teams alive!");

				TIME = 78000;

				PLAYER_CACHE.clear();

				TeamManager.TEAM_STATUS.clear();

				TEAM_INITIALIZED = false;

				return;
			}

			--TIME;

			if (!TEAM_INITIALIZED) {
				for (String name : FormattingManager.NAMES.keySet()) {
					String teamName = String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
					String addCommand = "/team add " + teamName + " \"" + teamName + "\"";
					String addColor = "/team modify color " + teamName + " \"" + name + "\"";

					TEAMS.add(teamName);

					callback.getCommandManager().execute(callback.getCommandSource(), addCommand);
					callback.getCommandManager().execute(callback.getCommandSource(), addColor);

					TeamManager.TEAM_STATUS.put(teamName, new HashMap<>());

					BattleMod.LOGGER.log(Level.INFO, addCommand);
					BattleMod.LOGGER.log(Level.INFO, addColor);
				}

				TEAM_INITIALIZED = true;
			}


			playerList.forEach(player -> {
				if (player.getScoreboardTeam() == null) {
					int index = Math.max(1, random.nextInt(Math.max(1, playerList.size() / 2)));
					callback.getCommandManager().execute(callback.getCommandSource(), "/team join " + TEAMS.get(index) + " " + player.getGameProfile().getName());

					TeamManager.TEAM_STATUS.get(player.getScoreboardTeam().getName()).put(player, true);
				}

				if (!PLAYER_CACHE.containsKey(player.getName().asString()) && player.getScoreboardTeam() != null) {
					Formatting formatting;
					try {
						formatting = FormattingManager.NAMES.get(player.getScoreboardTeam().getName().toLowerCase());
					} catch (Exception exception) {
						formatting = Formatting.WHITE;
					}

					ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);
					ItemStack chestplate = new ItemStack(Items.LEATHER_CHESTPLATE);
					ItemStack leggings = new ItemStack(Items.LEATHER_LEGGINGS);
					ItemStack boots = new ItemStack(Items.LEATHER_BOOTS);

					((DyeableArmorItem) helmet.getItem()).setColor(helmet, FormattingManager.COLORS.get(formatting));
					((DyeableArmorItem) chestplate.getItem()).setColor(chestplate, FormattingManager.COLORS.get(formatting));
					((DyeableArmorItem) leggings.getItem()).setColor(leggings, FormattingManager.COLORS.get(formatting));
					((DyeableArmorItem) boots.getItem()).setColor(boots, FormattingManager.COLORS.get(formatting));

					player.giveItemStack(helmet);
					player.giveItemStack(chestplate);
					player.giveItemStack(leggings);
					player.giveItemStack(boots);

					player.giveItemStack(new ItemStack(Items.IRON_PICKAXE));
					player.giveItemStack(new ItemStack(Items.IRON_AXE));
					player.giveItemStack(new ItemStack(Items.IRON_SWORD));
					player.giveItemStack(new ItemStack(Items.GOLDEN_CARROT, 8));

					PLAYER_CACHE.put(player.getName().asString(), true);
				}

				if (!player.isAlive() && player.getScoreboardTeam() != null && TIME <= 72000) {
					TeamManager.TEAM_STATUS.get(player.getScoreboardTeam().getName()).put(player, false);
					callback.getCommandManager().execute(callback.getCommandSource(), "/gamemode spectator " + player.getGameProfile().getName());
				}

				if (player.interactionManager.getGameMode() == GameMode.SPECTATOR) {
					if (TeamManager.TEAM_STATUS.get(player.getScoreboardTeam().getName()).keySet().stream().noneMatch(member -> player.getBlockPos().getSquaredDistance(player.getPos(), false) > 20)) {
						final int[] nearestDistance = new int[] { Integer.MAX_VALUE };
						final BlockPos[] nearestPos = new BlockPos[] { new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE) };

						TeamManager.TEAM_STATUS.get(player.getScoreboardTeam().getName()).keySet().forEach(member -> {
							int memberDistance = (int) player.getBlockPos().getSquaredDistance(member.getPos(), false);

							if (memberDistance < nearestDistance[0]) {
								nearestDistance[0] = memberDistance;
								nearestPos[0] = member.getBlockPos();
							}
						});

						if (nearestDistance[0] == 0) return;

						player.teleport(nearestPos[0].getX(), nearestPos[0].getY(), nearestPos[0].getZ());

						BattleMod.LOGGER.log(Level.INFO, "Teleported player " + player.getGameProfile().getName() + " to nearest neighbour (distance of " + nearestDistance[0] + " blocks) at " + nearestPos[0].getX() + ", " + nearestPos[0].getY() + ", " + nearestPos[0].getZ());
					}
				}
			});
		});
	}
}
