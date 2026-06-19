package xyz.axiumyu.paperDialogDsl

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.java.JavaPlugin

class PaperDialogDSL : JavaPlugin() {
    companion object {
        val mm = MiniMessage.miniMessage()
        fun d(s: String) = mm.deserialize(s)
        fun s(c: Component) = mm.serialize(c)
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(RouteCleanUp, this)
    }

}
