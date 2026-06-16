package xyz.axiumyu.paperDialogDsl

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin

class PaperDialogDSL : JavaPlugin() {
    companion object{
        val mm = MiniMessage.miniMessage()
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(RouteCleanUp, this)
    }

}
