package xyz.axiumyu.playerDisplay

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import org.bukkit.NamespacedKey
import xyz.axiumyu.playerDisplay.dialog.dialog2
import xyz.axiumyu.playerDisplay.dialog.dsl.registerDialog
import xyz.axiumyu.playerDisplay.dialog.newDialog

internal class PlayerDisplayBootstrap : PluginBootstrap {

    override fun bootstrap(context: BootstrapContext) {
        val registrar = context.lifecycleManager
        registrar.registerDialog(NamespacedKey("playertd","test"), newDialog)
        registrar.registerDialog(NamespacedKey("playertd", "test2"), dialog2)
    }
}
