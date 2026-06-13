package xyz.axiumyu.playerDisplay.dialog.dsl

import io.papermc.paper.dialog.DialogResponseView
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.action.DialogAction
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import xyz.axiumyu.playerDisplay.dialog.ActionButton

class MultiActionBuilder {
    val buttons = mutableListOf<ActionButton>()

    // 直接复用你之前在外部写好的 ActionButton 顶层函数，保持逻辑复用
    fun Button(label: Component, hover: Component, width: Int, action: DialogAction? = null) {
        buttons.add(ActionButton(label, hover, width, action))
    }

    // 同样复用你写的带有回调的 ActionButton
    fun Button(
        label: Component,
        hover: Component,
        width: Int,
        action: (view: DialogResponseView, audience: Audience) -> Unit
    ) {
        buttons.add(ActionButton(label, hover, width, action))
    }
}