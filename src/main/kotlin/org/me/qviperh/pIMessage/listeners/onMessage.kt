package org.me.qviperh.pIMessage.listeners

import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.me.qviperh.pIMessage.config.ConfigManager
import org.me.qviperh.pIMessage.database.DatabaseManager
import org.me.qviperh.pIMessage.utils.formatString

class onMessage(val databaseManager: DatabaseManager, val configManager: ConfigManager): Listener {
    @EventHandler
    fun onMessage(event: AsyncChatEvent) {
        val player = event.player
        var id = databaseManager.getId(player.name)
        // Double check
        if(id == -1) {
            id = databaseManager.getId(player.name)
        }

        val original = event.message() as TextComponent


        var finalMessage = configManager.format

        finalMessage = finalMessage.replace("%id%", id.toString(), false)


        finalMessage = finalMessage.replace("%message%", original.content(), false)


        event.message(
            Component.text(
                formatString(finalMessage)
            )
        )
    }
}