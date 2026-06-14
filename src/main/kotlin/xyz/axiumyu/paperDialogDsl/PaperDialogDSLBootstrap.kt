package xyz.axiumyu.paperDialogDsl

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap
import org.bukkit.NamespacedKey
import xyz.axiumyu.paperDialogDsl.dialog.dialog2
import xyz.axiumyu.paperDialogDsl.dialog.dsl.registerDialog
import xyz.axiumyu.paperDialogDsl.dialog.newDialog

internal class PaperDialogDSLBootstrap : PluginBootstrap {

    override fun bootstrap(context: BootstrapContext) {
        // register BaseDialog in bootstrap here like this
//        val registrar = context.lifecycleManager
//        registrar.registerDialog(NamespacedKey("playertd","test"), newDialog)
//        registrar.registerDialog(NamespacedKey("playertd", "test2"), dialog2)
    }
}
