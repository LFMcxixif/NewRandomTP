package cn.tpcraft.minecraft.plugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.ChatColor.translateAlternateColorCodes;
import static org.bukkit.Material.*;
import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
import static org.bukkit.Sound.ENTITY_PLAYER_LEVELUP;

public class MainCommand implements CommandExecutor {

    /* Teleport Cooldown List */
    private static Map<UUID, Long> CooldownList = new HashMap<>();
    private static int Timer;
    private static Location Location;

    @Override
    public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {
        /* Check command executor */
        if (Sender instanceof Player) {
            //player

            PlayerCommand((Player) Sender, Args);
        } else {
            //console

            ConsoleCommand(Args);
        }
        return false;
    }

    /* Console */
    private boolean ConsoleCommand(String[] Args) {
        /* Check command parameters */
        if (Args.length == 0) {
            //no parameters

            getLogger().info(
                    translateAlternateColorCodes('&',
                            NewRandomTP.Prefix + NewRandomTP.Message.getString("ConsoleUnavailable")
                    )
            );
        } else if (Args.length == 1) {
            //a parameter

            /* Judging parameter content */
            switch (Args[0]) {
                case "reload":
                    //reload configuration file

                    NewRandomTP.Plugin.reloadConfig();
                    NewRandomTP.Plugin = NewRandomTP.getProvidingPlugin(NewRandomTP.class);

                    String Language = NewRandomTP.Plugin.getConfig().getString("Language");
                    File MessageFile = new File(NewRandomTP.Plugin.getDataFolder() + "/message", "message_" + Language + ".yml");
                    NewRandomTP.Message = YamlConfiguration.loadConfiguration(MessageFile);

                    getLogger().info(
                            translateAlternateColorCodes('&',
                                    NewRandomTP.Prefix + NewRandomTP.Message.getString("ReloadConfig")
                            )
                    );
                    break;
                default:
                    //Other parameters

                    getLogger().info(
                            translateAlternateColorCodes('&',
                                    NewRandomTP.Prefix + NewRandomTP.Message.getString("ParameterError")
                            )
                    );
                    break;
            }
        } else {
            //Other parameters

            getLogger().info(
                    translateAlternateColorCodes('&',
                            NewRandomTP.Prefix + NewRandomTP.Message.getString("ParameterError")
                    )
            );
        }

        return false;
    }

    /* Player */
    private boolean PlayerCommand(Player Player, String[] Args) {
        /* Check command parameters */
        if (Args.length == 0) {
            //no parameters

            /* Check if the current world is available */
            if (!NewRandomTP.Plugin.getConfig().getStringList("World").contains(Player.getWorld().getName())) {
                //The current world is unavailable

                Player.sendMessage(
                        translateAlternateColorCodes('&',
                                NewRandomTP.Prefix + NewRandomTP.Message.getString("UnavailableWorld")
                        )
                );

                return false;
            }

            /* Check if you have permission to skip teleport cooldown */
            if (!Player.hasPermission("newrandomtp.cooldown")) {
                //Permission denied

                /* Check if player is teleporting cooldown list */
                if (CooldownList.get(Player.getUniqueId()) != null) {
                    //Player is teleporting cooldown list

                    //Check cooldown
                    if (!(CooldownList.get(Player.getUniqueId()) - System.currentTimeMillis() / 1000 <= 0)) {
                        //during the cool down period

                        Player.sendMessage(
                                translateAlternateColorCodes('&',
                                        NewRandomTP.Prefix + NewRandomTP.Message.getString("CoolingTips").replace("{Cooldown}", String.valueOf(CooldownList.get(Player.getUniqueId()) - System.currentTimeMillis() / 1000))
                                )
                        );

                        return false;
                    }
                }
            }

            Player.sendMessage(
                    translateAlternateColorCodes('&',
                            NewRandomTP.Prefix + NewRandomTP.Message.getString("CalculateCoordinates")
                    )
            );

            int MinTeleportX = NewRandomTP.Plugin.getConfig().getInt("MinTeleportX");
            int MinTeleportZ = NewRandomTP.Plugin.getConfig().getInt("MinTeleportZ");
            int MaxTeleportX = NewRandomTP.Plugin.getConfig().getInt("MaxTeleportX");
            int MaxTeleportZ = NewRandomTP.Plugin.getConfig().getInt("MaxTeleportZ");

            /* construction coordinates */
            int TeleportY;
            int TeleportX = (new Random().nextInt(MaxTeleportX - MinTeleportX + 1) + MinTeleportX) + (int) Player.getLocation().getX();
            int TeleportZ = (new Random().nextInt(MaxTeleportZ - MinTeleportZ + 1) + MinTeleportZ) + (int) Player.getLocation().getZ();
            int _TeleportX = ((- new Random().nextInt(MaxTeleportX - MinTeleportX + 1)) - MinTeleportX) + (int) Player.getLocation().getX();
            int _TeleportZ = ((- new Random().nextInt(MaxTeleportZ - MinTeleportZ + 1)) - MinTeleportZ) + (int) Player.getLocation().getZ();

            /* Randomly choose the delivery range */
            switch (new Random().nextInt(4)) {
                case 0:
                    TeleportY = Player.getWorld().getHighestBlockAt(TeleportX, TeleportZ).getY();
                    Location = new Location(Player.getWorld(), TeleportX + 0.5, TeleportY + 2, TeleportZ + 0.5);
                    break;
                case 1:
                    TeleportY = Player.getWorld().getHighestBlockAt(TeleportX, _TeleportZ).getY();
                    Location = new Location(Player.getWorld(), TeleportX + 0.5, TeleportY + 2, _TeleportZ - 0.5);
                    break;
                case 2:
                    TeleportY = Player.getWorld().getHighestBlockAt(_TeleportX, TeleportZ).getY();
                    Location = new Location(Player.getWorld(), _TeleportX - 0.5, TeleportY + 2, TeleportZ + 0.5);
                    break;
                case 3:
                    TeleportY = Player.getWorld().getHighestBlockAt(_TeleportX, _TeleportZ).getY();
                    Location = new Location(Player.getWorld(), _TeleportX - 0.5, TeleportY + 2, _TeleportZ - 0.5);
                    break;
            }

            Timer = NewRandomTP.Plugin.getConfig().getInt("Delay");
            new BukkitRunnable() {
                @Override
                public void run() {
                    /* play sound */
                    Player.playSound(Player.getLocation(), ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0F);

                    Player.sendTitle(
                            translateAlternateColorCodes('&',
                                    translateAlternateColorCodes('&',
                                            NewRandomTP.Message.getString("ReadyTeleportTitle")
                                    )
                            ),
                            translateAlternateColorCodes('&',
                                    translateAlternateColorCodes('&',
                                            NewRandomTP.Message.getString("ReadyTeleportSubTitle").replace("{Timer}", String.valueOf(Timer))
                                    )
                            ),
                            10,
                            20,
                            20
                    );

                    if (Timer == 0) {
                        /* teleport player */
                        Player.teleport(Location);

                        Player.sendTitle(
                                translateAlternateColorCodes('&',
                                        NewRandomTP.Message.getString("TeleportTitle")
                                ),
                                translateAlternateColorCodes('&',
                                        NewRandomTP.Message.getString("TeleportSubTitle").replace("{X}", String.valueOf(Location.getX())).replace("{Y}", String.valueOf(Location.getY())).replace("{Z}", String.valueOf(Location.getZ()))
                                ),
                                10,
                                60,
                                20
                        );

                        Player.sendMessage(
                                translateAlternateColorCodes('&',
                                        NewRandomTP.Prefix + NewRandomTP.Message.getString("TeleportTitle") + " " + NewRandomTP.Message.getString("TeleportSubTitle").replace("{X}", String.valueOf(Location.getX())).replace("{Y}", String.valueOf(Location.getY())).replace("{Z}", String.valueOf(Location.getZ()))
                                )
                        );

                        /* play sound */
                        Player.playSound(Location, ENTITY_PLAYER_LEVELUP, 1F, 0F);

                        /* spawn particle */
                        Player.getWorld().spawnParticle(Particle.SUSPENDED, Location.getX(), Location.getY() + 1, Location.getZ(), 50);

                        this.cancel();
                    }

                    Timer--;
                }
            }.runTaskTimer(NewRandomTP.getPlugin(NewRandomTP.class), 0, 20);

            /* Check if you have permission to skip teleport cooldown */
            if (!Player.hasPermission("newrandomtp.cooldown")) {
                //Permission denied

                /* Add player to cooldown list */
                CooldownList.put(Player.getUniqueId(), (System.currentTimeMillis() / 1000) + NewRandomTP.Plugin.getConfig().getInt("Cooldown"));
            }
        } else if (Args.length == 1) {
            //a parameter

            /* Judging parameter content */
            switch (Args[0]) {
                case "reload":
                    //reload configuration file

                    /* Check if player has permission */
                    if (Player.hasPermission("newrandomtp.admin")) {
                        //have permission

                        NewRandomTP.Plugin.reloadConfig();
                        NewRandomTP.Plugin = NewRandomTP.getProvidingPlugin(NewRandomTP.class);

                        String Language = NewRandomTP.Plugin.getConfig().getString("Language");
                        File MessageFile = new File(NewRandomTP.Plugin.getDataFolder() + "/message", "message_" + Language + ".yml");
                        NewRandomTP.Message = YamlConfiguration.loadConfiguration(MessageFile);

                        Player.sendMessage(
                                translateAlternateColorCodes('&',
                                        NewRandomTP.Prefix + NewRandomTP.Message.getString("ReloadConfig")
                                )
                        );
                    } else {
                        //Permission denied

                        Player.sendMessage(
                                translateAlternateColorCodes('&',
                                        NewRandomTP.Prefix + NewRandomTP.Message.getString("PermissionDenied")
                                )
                        );
                    }
                    break;
                default:
                    //Other parameters

                    Player.sendMessage(
                            translateAlternateColorCodes('&',
                                    NewRandomTP.Prefix + NewRandomTP.Message.getString("ParameterError")
                            )
                    );
                    break;
            }
        } else {
            //Other parameters

            Player.sendMessage(
                    translateAlternateColorCodes('&',
                            NewRandomTP.Prefix + NewRandomTP.Message.getString("ParameterError")
                    )
            );
        }

        return false;
    }
}
