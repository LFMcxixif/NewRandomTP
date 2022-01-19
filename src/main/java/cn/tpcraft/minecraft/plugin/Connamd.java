package cn.tpcraft.minecraft.plugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        //初始化玩家
        Player Player = null;

        //检查执行指令
        if (Sender instanceof Player) {
            //玩家

            //获取到玩家
            Player = (Player) Sender;

            //检查指令参数
            if (Args.length == 0) {
                //无参数

                //检查当前世界是否可用
                if (World.contains(Player.getWorld().getName())) {
                    //可用

                    //计算传送坐标
                    int TeleportX = (new Random().nextInt(MaxTeleportX - MinTeleportX + 1) + MinTeleportX) + (int) Player.getLocation().getX();
                    int TeleportZ = (new Random().nextInt(MaxTeleportZ - MinTeleportZ + 1) + MinTeleportZ) + (int) Player.getLocation().getZ();
                    int _TeleportX = ((- new Random().nextInt(MaxTeleportX - MinTeleportX + 1)) - MinTeleportX) + (int) Player.getLocation().getX();
                    int _TeleportZ = ((- new Random().nextInt(MaxTeleportZ - MinTeleportZ) + 1) - MinTeleportZ) + (int) Player.getLocation().getZ();

                    //随机算法
                    int Random = new Random().nextInt(4);

                    //构造坐标
                    int TeleportY = 0;
                    Location Location = null;
                    switch (Random) {
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

                    //检查跳过冷却权限
                    if (Sender.hasPermission("randomtp.cooldown")) {
                        //拥有权限

                        //传送玩家
                        Player.teleport(Location);
                        //发送信息给玩家
                        Player.sendMessage(NewRandomTP.Prefix + "随机传送到 X:" + TeleportX + ", Y:" + TeleportY + ", Z:" + TeleportZ);
                    } else {
                        //没有权限

                        //检查是否存在冷却列表
                        if (CooldownList.containsKey(Player.getUniqueId())) {
                            //存在

                            //检查是否到冷却时间
                            if (CooldownList.get(Player.getUniqueId()) - System.currentTimeMillis() / 1000 <= 0) {
                                //达到冷却时间

                                //删除玩家原有集合
                                CooldownList.remove(Player.getUniqueId());
                                //添加玩家到集合
                                CooldownList.put(Player.getUniqueId(), (System.currentTimeMillis() / 1000) + Cooldown);
                                //传送玩家
                                Player.teleport(Location);
                                //发送信息给玩家
                                Player.sendMessage(NewRandomTP.Prefix + "随机传送到 X:" + TeleportX + ", Y:" + TeleportY + ", Z:" + TeleportZ);
                            } else {
                                //未达到冷却时间

                                //发送信息给玩家
                                Player.sendMessage(NewRandomTP.Prefix + "你还有 " + ChatColor.YELLOW + (CooldownList.get(Player.getUniqueId()) - System.currentTimeMillis() / 1000) + ChatColor.RESET + " 秒才能使用随机传送");
                            }
                        } else {
                            //不存在

                            //添加玩家到集合
                            CooldownList.put(Player.getUniqueId(), (System.currentTimeMillis() / 1000) + Cooldown);
                            //传送玩家
                            Player.teleport(Location);
                            //发送信息给玩家
                            Player.sendMessage(NewRandomTP.Prefix + "随机传送到 X:" + TeleportX + ", Y:" + TeleportY + ", Z:" + TeleportZ);
                        }
                    }
                } else {
                    //不可用

                    //发送信息给玩家
                    Player.sendMessage(NewRandomTP.Prefix + "当前世界随机传送不可用");
                }
            } else if (Args.length == 1) {
                //有参数

                //检查参数内容
                if (Args[0].equals("info")) {
                    //查看配置文件

                    //历遍可用世界
                    String AvailableWorld = "";
                    for (Integer I = 0; World.size() > I; I++) {
                        AvailableWorld += World.get(I) + " ";
                    }
                    //发送信息给玩家
                    Player.sendMessage(NewRandomTP.Prefix + "====================");
                    Player.sendMessage(NewRandomTP.Prefix + "冷却时间 " + ChatColor.GOLD + Cooldown + ChatColor.RESET + " 秒");
                    Player.sendMessage(NewRandomTP.Prefix + "可用世界 " + ChatColor.GOLD + AvailableWorld);
                    Player.sendMessage(NewRandomTP.Prefix + "最小传送范围X " + ChatColor.GOLD + MinTeleportX);
                    Player.sendMessage(NewRandomTP.Prefix + "最小传送范围Z " + ChatColor.GOLD + MinTeleportZ);
                    Player.sendMessage(NewRandomTP.Prefix + "最大传送范围X " + ChatColor.GOLD + MaxTeleportX);
                    Player.sendMessage(NewRandomTP.Prefix + "最大传送范围Z " + ChatColor.GOLD + MaxTeleportZ);
                    Player.sendMessage(NewRandomTP.Prefix + "====================");
                } else if (Args[0].equals("reload")) {
                    //重载配置文件参数

                    //检查权限
                    if (Sender.hasPermission("randomtp.admin")) {
                        //拥有权限

                        //重载配置文件
                        NewRandomTP.Config.reloadConfig();
                        NewRandomTP.Prefix = translateAlternateColorCodes('&', NewRandomTP.Config.getConfig().getString("Prefix"));
                        //读取配置文件
                        NewRandomTP.Config = NewRandomTP.getProvidingPlugin(NewRandomTP.class);
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
                        //发送信息给玩家
                        Player.sendMessage(NewRandomTP.Prefix + "配置文件重载成功");
                    } else {
                        //权限不足

                        //发送信息给玩家
                        Player.sendMessage(NewRandomTP.Prefix + "权限不足");
                    }
                } else {
                    //其他参数内容

                    //发送信息给玩家
                    Player.sendMessage(NewRandomTP.Prefix + "参数错误");
                }
            } else {
                //参数错误

                //发送信息给玩家
                Player.sendMessage(NewRandomTP.Prefix + "参数错误");
            }
        } else {
            //控制台

            //检查指令参数
            if (Args.length == 0) {
                //无参数

                //发送信息给控制台
                getLogger().info(NewRandomTP.Prefix + "控制台不能使用随机传送");
            } else if (Args.length == 1 && Args[0].equals("reload")) {
                //重载配置文件

                //重载配置文件
                NewRandomTP.Config.reloadConfig();
                NewRandomTP.Prefix = translateAlternateColorCodes('&', NewRandomTP.Config.getConfig().getString("Prefix"));
                //读取配置文件
                NewRandomTP.Config = NewRandomTP.getProvidingPlugin(NewRandomTP.class);
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
                //发送信息给控制台
                getLogger().info(NewRandomTP.Prefix + "配置文件重载成功");
            } else if (Args.length == 1 && Args[0].equals("info")) {
                //查看配置文件

                //历遍可用世界
                String AvailableWorld = "";
                for (Integer I = 0; World.size() > I; I++) {
                    AvailableWorld += World.get(I) + " ";
                }
                //输出控制台
                getLogger().info(NewRandomTP.Prefix + "====================");
                getLogger().info(NewRandomTP.Prefix + "冷却时间 " + ChatColor.GOLD + Cooldown + ChatColor.RESET + " 秒");
                getLogger().info(NewRandomTP.Prefix + "可用世界 " + ChatColor.GOLD + AvailableWorld);
                getLogger().info(NewRandomTP.Prefix + "最小传送范围X " + ChatColor.GOLD + MinTeleportX);
                getLogger().info(NewRandomTP.Prefix + "最小传送范围Z " + ChatColor.GOLD + MinTeleportZ);
                getLogger().info(NewRandomTP.Prefix + "最大传送范围X " + ChatColor.GOLD + MaxTeleportX);
                getLogger().info(NewRandomTP.Prefix + "最大传送范围Z " + ChatColor.GOLD + MaxTeleportZ);
                getLogger().info(NewRandomTP.Prefix + "====================");
            } else {
                //参数错误

                //发送信息给控制台
                getLogger().info(NewRandomTP.Prefix + "参数错误");
            }
            return false;
        }
        return false;
    }
}
