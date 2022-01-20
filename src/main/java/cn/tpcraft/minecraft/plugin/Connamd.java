package cn.tpcraft.minecraft.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

import static net.md_5.bungee.api.ChatColor.translateAlternateColorCodes;
import static org.bukkit.Bukkit.getLogger;

public class Connamd implements CommandExecutor {

    //冷却时间
    public static int Cooldown = NewRandomTP.Config.getConfig().getInt("Cooldown");
    //可用世界
    public static List<String> World = NewRandomTP.Config.getConfig().getStringList("World");
    //最小传送范围
    public static int MinTeleportX = NewRandomTP.Config.getConfig().getInt("MinTeleportX");
    public static int MinTeleportZ = NewRandomTP.Config.getConfig().getInt("MinTeleportZ");
    //最大传送范围
    public static int MaxTeleportX = NewRandomTP.Config.getConfig().getInt("MaxTeleportX");
    public static int MaxTeleportZ = NewRandomTP.Config.getConfig().getInt("MaxTeleportZ");
    //冷却列表
    public static Map<UUID, Long> CooldownList = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {
        //检查执行指令
        if (Sender instanceof Player) {
            //玩家

            //获取到玩家
            Player Player = (Player) Sender;

            Player_Command(Player, Args);
        } else {
            //控制台

            Console_Command(Args);
        }
        return false;
    }

    public boolean Player_Command(Player Player, String[] Args) {
        //检查指令参数
        if (Args.length == 0) {
            //无参数

            //检查当前世界是否可用
            if (!World.contains(Player.getWorld().getName())) {
                //当前世界不可用

                //发送信息给玩家
                Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("UnavailableWorld")));

                return false;
            }

            //检查是否拥有跳过传送冷却权限
            if (!Player.hasPermission("newrandomtp.cooldown")) {
                //不拥有权限

                //检查玩家是否在传送冷却列表
                if (CooldownList.get(Player.getUniqueId()) != null) {
                    //玩家在传送冷却列表

                    //检查冷却时间
                    if (!(CooldownList.get(Player.getUniqueId()) - System.currentTimeMillis() / 1000 <= 0)) {
                        //未达到冷却时间

                        //发送信息给玩家
                        Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("CoolingTips").replace("{Cooldown}", ChatColor.GOLD + String.valueOf(CooldownList.get(Player.getUniqueId()) - System.currentTimeMillis() / 1000)  + ChatColor.RESET)));

                        return false;
                    }
                }
            }

            //计算传送坐标
            int TeleportX = (new Random().nextInt(MaxTeleportX - MinTeleportX + 1) + MinTeleportX) + (int) Player.getLocation().getX();
            int TeleportZ = (new Random().nextInt(MaxTeleportZ - MinTeleportZ + 1) + MinTeleportZ) + (int) Player.getLocation().getZ();
            int _TeleportX = ((- new Random().nextInt(MaxTeleportX - MinTeleportX + 1)) - MinTeleportX) + (int) Player.getLocation().getX();
            int _TeleportZ = ((- new Random().nextInt(MaxTeleportZ - MinTeleportZ) + 1) - MinTeleportZ) + (int) Player.getLocation().getZ();

            //构造随机坐标
            int TeleportY = 0;
            Location Location = null;
            switch (new Random().nextInt(4)) {
                case 0:
                    //计算地面方块高度
                    TeleportY = Player.getWorld().getHighestBlockAt(TeleportX, TeleportZ).getY();
                    //构造坐标函数
                    Location = new Location(Player.getWorld(), TeleportX, TeleportY, TeleportZ);
                    break;
                case 1:
                    //计算地面方块高度
                    TeleportY = Player.getWorld().getHighestBlockAt(TeleportX, _TeleportZ).getY();
                    //构造坐标函数
                    Location = new Location(Player.getWorld(), TeleportX, TeleportY, _TeleportZ);
                    break;
                case 2:
                    //计算地面方块高度
                    TeleportY = Player.getWorld().getHighestBlockAt(_TeleportX, TeleportZ).getY();
                    //构造坐标函数
                    Location = new Location(Player.getWorld(), _TeleportX, TeleportY, TeleportZ);
                    break;
                case 3:
                    //计算地面方块高度
                    TeleportY = Player.getWorld().getHighestBlockAt(_TeleportX, _TeleportZ).getY();
                    //构造坐标函数
                    Location = new Location(Player.getWorld(), _TeleportX, TeleportY, _TeleportZ);
                    break;
            }

            //传送玩家
            Player.teleport(Location);
            //替换发送的信息
            String Message = NewRandomTP.Message.getString("Teleport");
            Message = Message.replace("{X}", String.valueOf(TeleportX));
            Message = Message.replace("{Y}", String.valueOf(TeleportY));
            Message = Message.replace("{Z}", String.valueOf(TeleportZ));
            //发送信息给玩家
            Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', Message));

            //检查玩家是否拥有跳过传送冷却权限
            if (!Player.hasPermission("newrandomtp.cooldown")) {
                //不拥有

                //添加玩家到集合
                CooldownList.put(Player.getUniqueId(), (System.currentTimeMillis() / 1000) + Cooldown);
            }
        } else if (Args.length == 1) {
            //有参数

            //检查参数内容
            switch (Args[0]) {
                //查看配置文件
                case "info":
                    //历遍可用世界
                    String AvailableWorld = "";
                    for (Integer I = 0; World.size() > I; I++) {
                        AvailableWorld += World.get(I) + " ";
                    }

                    //发送信息给玩家
                    Player.sendMessage(NewRandomTP.Prefix + "====================");
                    Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("Cooldown").replace("{Second}", String.valueOf(Cooldown))));
                    Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("AvailableWorld").replace("{World}", AvailableWorld)));
                    Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("MinTeleportRangeX").replace("{X}", String.valueOf(MinTeleportX))));
                    Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("MinTeleportRangeZ").replace("{Z}", String.valueOf(MinTeleportZ))));
                    Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("MaxTeleportRangeX").replace("{X}", String.valueOf(MaxTeleportX))));
                    Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("MaxTeleportRangeZ").replace("{Z}", String.valueOf(MaxTeleportZ))));
                    Player.sendMessage(NewRandomTP.Prefix + "====================");
                    break;

                //重载配置文件
                case "reload":
                    //检查玩家是否有插件管理权限
                    if (!Player.hasPermission("newrandomtp.cooldown")) {
                        //不拥有

                        Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("NoPermission")));

                        return false;
                    }

                    //重载配置文件
                    NewRandomTP.Config.reloadConfig();
                    //读取配置文件
                    NewRandomTP.Config = NewRandomTP.getProvidingPlugin(NewRandomTP.class);
                    NewRandomTP.Prefix = translateAlternateColorCodes('&', NewRandomTP.Config.getConfig().getString("Prefix"));
                    //冷却时间
                    Cooldown = NewRandomTP.Config.getConfig().getInt("Cooldown");
                    //可用世界
                    World = NewRandomTP.Config.getConfig().getStringList("World");
                    //最小传送范围
                    MinTeleportX = NewRandomTP.Config.getConfig().getInt("MinTeleportX");
                    MinTeleportZ = NewRandomTP.Config.getConfig().getInt("MinTeleportZ");
                    //最大传送范围
                    MaxTeleportX = NewRandomTP.Config.getConfig().getInt("MaxTeleportX");
                    MaxTeleportZ = NewRandomTP.Config.getConfig().getInt("MaxTeleportZ");
                    //语言
                    if (NewRandomTP.Config.getConfig().getString("Language").equals("EN")) {
                        File MessageFile = new File(NewRandomTP.Config.getDataFolder() + "/message", "message_EN.yml");
                        NewRandomTP.Message = YamlConfiguration.loadConfiguration(MessageFile);
                    } else if (NewRandomTP.Config.getConfig().getString("Language").equals("CN")) {
                        File MessageFile = new File(NewRandomTP.Config.getDataFolder() + "/message", "message_CN.yml");
                        NewRandomTP.Message = YamlConfiguration.loadConfiguration(MessageFile);
                    }
                    //发送信息给玩家
                    Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("ReloadConfig")));
                    break;

                //参数错误
                default:

                    //发送信息给玩家
                    Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("ParameterError")));
                    break;
            }
        } else {
            //参数错误

            //发送信息给玩家
            Player.sendMessage(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("ParameterError")));
        }

        return false;
    }

    public void Console_Command(String[] Args) {
        //检查指令参数
        if (Args.length == 0) {
            //无参数

            //发送信息给控制台
            getLogger().info(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("ConsoleUnavailable")));
        } else if (Args.length == 1) {
            //有参数

            //检查参数内容
            switch (Args[0]) {
                //查看配置文件
                case "info":
                    //历遍可用世界
                    String AvailableWorld = "";
                    for (Integer I = 0; World.size() > I; I++) {
                        AvailableWorld += World.get(I) + " ";
                    }

                    //发送信息给控制台
                    getLogger().info(NewRandomTP.Prefix + "====================");
                    getLogger().info(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("Cooldown").replace("{Second}", String.valueOf(Cooldown))));
                    getLogger().info(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("AvailableWorld").replace("{World}", AvailableWorld)));
                    getLogger().info(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("MinTeleportRangeX").replace("{X}", String.valueOf(MinTeleportX))));
                    getLogger().info(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("MinTeleportRangeZ").replace("{Z}", String.valueOf(MinTeleportZ))));
                    getLogger().info(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("MaxTeleportRangeX").replace("{X}", String.valueOf(MaxTeleportX))));
                    getLogger().info(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("MaxTeleportRangeZ").replace("{Z}", String.valueOf(MaxTeleportZ))));
                    getLogger().info(NewRandomTP.Prefix + "====================");
                    break;

                //重载配置文件
                case "reload":
                    //重载配置文件
                    NewRandomTP.Config.reloadConfig();
                    //读取配置文件
                    NewRandomTP.Config = NewRandomTP.getProvidingPlugin(NewRandomTP.class);
                    NewRandomTP.Prefix = translateAlternateColorCodes('&', NewRandomTP.Config.getConfig().getString("Prefix"));
                    //冷却时间
                    Cooldown = NewRandomTP.Config.getConfig().getInt("Cooldown");
                    //可用世界
                    World = NewRandomTP.Config.getConfig().getStringList("World");
                    //最小传送范围
                    MinTeleportX = NewRandomTP.Config.getConfig().getInt("MinTeleportX");
                    MinTeleportZ = NewRandomTP.Config.getConfig().getInt("MinTeleportZ");
                    //最大传送范围
                    MaxTeleportX = NewRandomTP.Config.getConfig().getInt("MaxTeleportX");
                    MaxTeleportZ = NewRandomTP.Config.getConfig().getInt("MaxTeleportZ");
                    //语言
                    if (NewRandomTP.Config.getConfig().getString("Language").equals("EN")) {
                        File MessageFile = new File(NewRandomTP.Config.getDataFolder() + "/message", "message_EN.yml");
                        NewRandomTP.Message = YamlConfiguration.loadConfiguration(MessageFile);
                    } else if (NewRandomTP.Config.getConfig().getString("Language").equals("CN")) {
                        File MessageFile = new File(NewRandomTP.Config.getDataFolder() + "/message", "message_CN.yml");
                        NewRandomTP.Message = YamlConfiguration.loadConfiguration(MessageFile);
                    }
                    //发送信息给控制台
                    getLogger().info(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("ReloadConfig")));
                    break;

                //参数错误
                default:

                    //发送信息给控制台
                    getLogger().info(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("ParameterError")));
                    break;
            }
        } else {
            //参数错误

            //发送信息给控制台
            getLogger().info(NewRandomTP.Prefix + translateAlternateColorCodes('&', NewRandomTP.Message.getString("ParameterError")));
        }
    }
}
