package org.me.qviperh.pIMessage

import org.bukkit.plugin.java.JavaPlugin
import org.me.qviperh.pIMessage.config.ConfigManager
import org.me.qviperh.pIMessage.database.DatabaseManager
import org.me.qviperh.pIMessage.listeners.onJoin
import org.me.qviperh.pIMessage.listeners.onMessage
import org.me.qviperh.pIMessage.utils.broadcastCreditsToAll

class PIMessage : JavaPlugin() {

    lateinit var databaseManager: DatabaseManager
    lateinit var configManager: ConfigManager

    override fun onEnable() {
        databaseManager = DatabaseManager(this)
        databaseManager.init()
        configManager = ConfigManager(this)
        configManager.init()

        registerEvents()

        this.logger.info("Events registered!")
        this.logger.info("Credits: ")
        this.logger.info("qViperH (discord: viperasuu")

        this.server.scheduler.runTaskLater(this, Runnable {
            broadcastCreditsToAll(this)
        }, 12000)

    }

    fun registerEvents(){
        server.pluginManager.registerEvents(
            onJoin(databaseManager),
            this
        )

        server.pluginManager.registerEvents(
            onMessage(databaseManager, configManager),
            this
        )
    }


    override fun onDisable() {
        // Plugin shutdown logic
    }

    override fun reloadConfig() {
        configManager = ConfigManager(this)
        configManager.init()
    }
}
