package org.me.qviperh.pIMessage.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.me.qviperh.pIMessage.database.DatabaseManager

class onJoin(val databaseManager: DatabaseManager) : Listener{
    @EventHandler
    fun onJoinEvent(event: PlayerJoinEvent){
        val player = event.player
        databaseManager.insertId(player.name)
    }
}