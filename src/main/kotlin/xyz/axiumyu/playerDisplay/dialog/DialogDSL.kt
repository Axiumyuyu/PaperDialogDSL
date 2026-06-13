package xyz.axiumyu.playerDisplay.dialog

import io.papermc.paper.dialog.Dialog
import io.papermc.paper.dialog.DialogResponseView
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import io.papermc.paper.registry.data.dialog.body.ItemDialogBody
import io.papermc.paper.registry.data.dialog.input.DialogInput
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput
import io.papermc.paper.registry.data.dialog.input.TextDialogInput
import io.papermc.paper.registry.data.dialog.type.ConfirmationType
import io.papermc.paper.registry.data.dialog.type.DialogType
import io.papermc.paper.registry.data.dialog.type.DialogType.confirmation
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.inventory.ItemStack
import xyz.axiumyu.playerDisplay.PlayerDisplay.Companion.mm
import xyz.axiumyu.playerDisplay.dialog.dsl.DialogBaseScope
import xyz.axiumyu.playerDisplay.dialog.dsl.MultiActionBuilder


fun MultiAction(block: MultiActionBuilder.() -> Unit): DialogType {
    val builder = MultiActionBuilder().apply(block)
    return DialogType.multiAction(builder.buttons).build()
}

fun Component.plainText() = PlainTextComponentSerializer.plainText().serialize(this)

fun BuildDialog(block: DialogRegistryEntry.Builder.() -> Unit): Dialog {
    return Dialog.create { it.empty().apply(block) }
}

fun DialogRegistryEntry.Builder.DialogContent(title: Component, block: DialogBaseScope.() -> Unit) {
    // 1. 初始化收集器并执行你的 DSL 闭包
    val scope = DialogBaseScope().apply(block)

    // 2. 创建原生的 Base Builder
    val baseBuilder = DialogBase.builder(title)

    // 3. 只有当列表不为空时，才将它们注入给 Paper API
    if (scope.bodyList.isNotEmpty()) {
        baseBuilder.body(scope.bodyList)
    }
    if (scope.inputList.isNotEmpty()) {
        baseBuilder.inputs(scope.inputList)
    }

    // 4. 组装并应用
    this.base(baseBuilder.build())
}

// --- 3. 类型与按钮构建器 ---
// 修复：签名改为返回 DialogType，并主动注入
fun DialogRegistryEntry.Builder.DialogType(typeProvider: () -> DialogType) {
    this.type(typeProvider())
}


fun Confirmation(yes: ActionButton, no: ActionButton): ConfirmationType {
    return confirmation(yes, no)
}

fun ActionButton(label: Component, hover: Component, width: Int, action: DialogAction? = null): ActionButton {
    return ActionButton.create(label, hover, width, action)
}

fun ActionButton(
    label: Component,
    hover: Component,
    width: Int,
    action: (view: DialogResponseView, audience: Audience) -> Unit
): ActionButton {
    return ActionButton.create(
        label, hover, width, DialogAction.customClick(
            action,
            ClickCallback.Options.builder().lifetime(ClickCallback.DEFAULT_LIFETIME)
                .uses(1).build()
        )
    )

}

// example
val newDialog: DialogRegistryEntry.Builder.() -> Unit = {
    DialogContent(mm.deserialize("Title")) {

        NumRangeInput("test2", mm.deserialize(""), 0f to 100f)
        BoolInput("test", mm.deserialize(""))
        TextInput("test3", mm.deserialize(""))
    }
    DialogType {

        Confirmation(
            ActionButton(
                mm.deserialize("1 left"),
                mm.deserialize("2 left"),
                100
            ),
            ActionButton(
                mm.deserialize("1 right"),
                mm.deserialize("2 right"),
                100
            ) { view, audience ->
                val test2 = view.getFloat("test2") ?: return@ActionButton
            }
        )
    }
}
