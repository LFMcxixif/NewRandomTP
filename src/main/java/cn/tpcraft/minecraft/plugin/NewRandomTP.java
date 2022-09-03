package cn.tpcraft.minecraft.plugin;

import cn.tpcraft.minecraft.plugin.Lib.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class NewRandomTP extends JavaPlugin {

    /* 插件变量 */
    public static Plugin Plugin;
    public static String Prefix = "&7&l[&6&lNRTP&7&l] &c&l>> &r";
    public static FileConfiguration Message;

    /* Plugin loading */
    @Override
    public void onEnable() {
        new Metrics(this, 13998);
        LoadConfig();
        LoadCommand();
        getLogger().info("========================================");
        getLogger().info("The plugin has been loaded successfully!");
        getLogger().info("Plugin source code:");
        getLogger().info("https://github.com/LFMcxixif/NewRandomTP");
        getLogger().info("========================================");
    }

    /* Plugin uninstall */
    @Override
    public void onDisable() {
        getLogger().info("========================================");
        getLogger().info("The plugin has been successfully uninstalled!");
        getLogger().info("========================================");
    }

    /* Load configuration file */
    private void LoadConfig() {
        saveDefaultConfig();
        saveResource("message/message_zh-CN.yml", false);
        saveResource("message/message_zh-TW.yml", false);
        saveResource("message/message_en-US.yml", false);

        Plugin = NewRandomTP.getProvidingPlugin(NewRandomTP.class);

        String Language = Plugin.getConfig().getString("Language");
        File MessageFile = new File(getDataFolder() + "/message", "message_" + Language + ".yml");
        Message = YamlConfiguration.loadConfiguration(MessageFile);
    }

    /* Load command */
    private void LoadCommand() {
        getCommand("nrtp").setExecutor(new MainCommand());
    }
}
