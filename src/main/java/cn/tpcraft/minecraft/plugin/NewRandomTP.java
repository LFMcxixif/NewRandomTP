package cn.tpcraft.minecraft.plugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import static net.md_5.bungee.api.ChatColor.translateAlternateColorCodes;

public final class NewRandomTP extends JavaPlugin {

    //插件
    public static Plugin Config;
    //语言
    public static FileConfiguration Message;
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
        getLogger().info("The plugin is loaded!");
        getLogger().info("https://github.com/LFMcxixif/NewRandomTP");
    }

    /*
     * 插件卸载事件
     */
    @Override
    public void onDisable() {
        getLogger().info("The plugin is uninstalled!");
        getLogger().info("https://github.com/LFMcxixif/NewRandomTP");
    }

    /*
     * 加载配置文件
     */
    public void LoadConfig() {
        saveDefaultConfig();
        saveResource("message/message_CN.yml", false);
        saveResource("message/message_EN.yml", false);
        Config = NewRandomTP.getProvidingPlugin(NewRandomTP.class);
        Prefix = translateAlternateColorCodes('&', Config.getConfig().getString("Prefix"));
        if (Config.getConfig().getString("Language").equals("EN")) {
            File MessageFile = new File(getDataFolder() + "/message", "message_EN.yml");
            Message = YamlConfiguration.loadConfiguration(MessageFile);
        } else if (Config.getConfig().getString("Language").equals("CN")) {
            File MessageFile = new File(getDataFolder() + "/message", "message_CN.yml");
            Message = YamlConfiguration.loadConfiguration(MessageFile);
        }
    }

    /*
     * 加载命令
     */
    public void LoadCommand() {
        getCommand("tpr").setExecutor(new Connamd());
    }
}
