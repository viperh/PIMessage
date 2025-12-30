package org.me.qviperh.pIMessage.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.me.qviperh.pIMessage.PIMessage
import java.io.File

class ConfigManager(val plugin: PIMessage) {
    lateinit var config: FileConfiguration

    lateinit var format: String

    fun init() {
        val configFile = File(plugin.dataFolder, "config.yml")
        if (!configFile.exists()) {
            plugin.dataFolder.mkdirs()
            plugin.saveDefaultConfig()
        }

        config = YamlConfiguration.loadConfiguration(configFile)
        format = config.getString("format") ?: "[#%id%] %message%"
    }

}