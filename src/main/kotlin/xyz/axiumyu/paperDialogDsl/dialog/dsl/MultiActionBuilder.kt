package xyz.axiumyu.paperDialogDsl.dialog.dsl

import io.papermc.paper.dialog.DialogResponseView
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.type.DialogType
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import java.time.Duration

@PaperDialogDsl
class MultiActionBuilder {
    val buttons = mutableListOf<ActionButton>()

    // 直接复用你之前在外部写好的 ActionButton 顶层函数，保持逻辑复用
    fun ActionButton(label: Component, hover: Component, width: Int, action: DialogAction? = null) {
        buttons.add(ActionButton.create(label, hover, width, action))
    }

    // 同样复用你写的带有回调的 ActionButton
    fun ActionButton(
        label: Component,
        hover: Component,
        width: Int,
        maxUses: Int = 1,
        keepTime: Duration = ClickCallback.DEFAULT_LIFETIME,
        action: (view: DialogResponseView, audience: Audience) -> Unit
    ) {
        buttons.add(ActionButton.create(
            label, hover, width, DialogAction.customClick(
                action,
                ClickCallback.Options.builder().lifetime(keepTime)
                    .uses(maxUses).build()
            )
        ))
    }
}

fun MultiActionType(block: MultiActionBuilder.() -> Unit): DialogType {
    val builder = MultiActionBuilder().apply(block)
    return DialogType.multiAction(builder.buttons).build()
}