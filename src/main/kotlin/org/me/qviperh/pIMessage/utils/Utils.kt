package org.me.qviperh.pIMessage.utils

import org.bukkit.ChatColor
import org.me.qviperh.pIMessage.PIMessage


fun broadcastCreditsToAll(plugin: PIMessage) {
    for (user in plugin.server.onlinePlayers) {
        user.sendMessage(formatString("&c[PIMessage Plugin]"))
        user.sendMessage(formatString("&cCredits: "))
        user.sendMessage(formatString("&cqViperH &a(discord: viperasuu)"))
    }
}


fun formatString(str: String) : String {
    return ChatColor.translateAlternateColorCodes('&', str)
}