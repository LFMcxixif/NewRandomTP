package cn.tpcraft.minecraft.plugin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static net.md_5.bungee.api.ChatColor.translateAlternateColorCodes;

public final class NewRandomTP extends JavaPlugin {

    //插件
    public static Plugin Config;
    //前缀
    public static String Prefix;

    /*
     * 插件加载事件
     */
    @Override
    public void onEnable() {
        new Metrics(this, 13998);
        LoadConfig();
        LoadCommand();
        getLogger().info("插件已加载！");
    }

    /*
     * 插件卸载事件
     */
    @Override
    public void onDisable() {
        getLogger().info("插件已卸载！");
    }

    /*
     * 加载配置文件
     */
    public void LoadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File File = new File(getDataFolder(), "config.yml");
        if (!(File.exists())) {
            saveDefaultConfig();
        }
        reloadConfig();
        Config = NewRandomTP.getProvidingPlugin(NewRandomTP.class);
        Prefix = translateAlternateColorCodes('&', Config.getConfig().getString("Prefix"));
    }

    /*
     * 加载命令
     */
    public void LoadCommand() {
        getCommand("tpr").setExecutor(new Connamd());
    }
}
