package xyz.axiumyu.paperDialogDsl.dialog.dsl

import io.papermc.paper.dialog.*
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.action.DialogAction
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import java.time.Duration

enum class UIType {
    NOTICE, CONFIRMATION, MULTI_ACTION, DIALOG_LIST, SERVER_LINKS
}

@PaperDialogDsl
class DialogTypeScope {
    val buttons = mutableListOf<ActionButton>()
    var exitButton: ActionButton? = null

    // 常规按钮
    fun Button(
        label: Component, hover: Component, width: Int,
        maxUses: Int = 1, keepTime: Duration = ClickCallback.DEFAULT_LIFETIME,
        action: (view: DialogResponseView, audience: Audience) -> Unit
    ) {
        buttons.add(
            ActionButton.create(
                label, hover, width, DialogAction.customClick(
                    action, ClickCallback.Options.builder().lifetime(keepTime).uses(maxUses).build()
                )
            )
        )
    }

    // 无回调的按钮
    fun Button(label: Component, hover: Component, width: Int, action: DialogAction? = null) {
        buttons.add(ActionButton.create(label, hover, width, action))
    }

    // 专属的退出按钮声明
    fun ExitButton(label: Component, hover: Component, width: Int, action: DialogAction? = null) {
        exitButton = ActionButton.create(label, hover, width, action)
    }
}
