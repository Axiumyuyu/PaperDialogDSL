package xyz.axiumyu.paperDialogDsl.dialog.dsl

import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager
import io.papermc.paper.registry.event.RegistryEvents
import io.papermc.paper.registry.keys.DialogKeys
import org.bukkit.NamespacedKey
import xyz.axiumyu.paperDialogDsl.dialog.BaseDialog

fun LifecycleEventManager<BootstrapContext>.registerDialog(
    namespacedKey: NamespacedKey,
    block: BaseDialog
) {
    registerEventHandler(RegistryEvents.DIALOG.compose().newHandler { event ->
        // 将样板代码彻底封装，直接执行外部传入的 DSL 闭包
        event.registry().register(
            DialogKeys.create(namespacedKey),
            block
        )
    })
}