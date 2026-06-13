package xyz.axiumyu.playerDisplay

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin

class PlayerDisplay : JavaPlugin() {
    companion object{
        val mm = MiniMessage.miniMessage()
    }

    override fun onEnable() {
        // Plugin startup logic
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
