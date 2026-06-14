package xyz.axiumyu.playerDisplay.dialog.dsl

import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry
import io.papermc.paper.registry.data.dialog.type.DialogType
import io.papermc.paper.registry.set.RegistrySet
import net.kyori.adventure.text.Component

@PaperDialogDsl
class DialogRootScope(private val entryBuilder: DialogRegistryEntry.Builder) {

    /*fun DialogRegistryEntry.Builder.DialogContent(title: Component, block: DialogBaseScope.() -> Unit) {
        // 1. 创建真实的原生 Builder
        val nativeBuilder = DialogBase.builder(title)

        // 2. 创建我们的作用域代理，并包裹原生 Builder
        val scope = DialogBaseScope(nativeBuilder)
        // 3. 执行闭包！
        // 此时闭包内的 this 既有 PlainMessage()，也有原生的 canCloseWithEscape()
        scope.block()

        // 3. 只有当列表不为空时，才将它们注入给 Paper API
        if (scope.bodyList.isNotEmpty()) {
            nativeBuilder.body(scope.bodyList)
        }
        if (scope.inputList.isNotEmpty()) {
            nativeBuilder.inputs(scope.inputList)
        }

        // 4. 组装并应用
        this.base(nativeBuilder.build())
    }

    fun DialogRegistryEntry.Builder.DialogType(
        type: UIType,
        columns: Int = 1, // 供 Multi, List, ServerLinks 使用
        buttonWidth: Int = 200, // 供 List, ServerLinks 使用
        dialogs: RegistrySet<Dialog>? = null, // 供 DialogList 专属使用
        block: DialogTypeScope.() -> Unit = {}
    ) {
        // 1. 初始化作用域并收集所有按钮
        val scope = DialogTypeScope().apply(block)
        val btns = scope.buttons
        val exitBtn = scope.exitButton

        // 2. 路由与动态安全检查
        val paperType = when (type) {
            UIType.NOTICE -> {
                if (btns.isEmpty()) DialogType.notice() else DialogType.notice(btns.first())
            }

            UIType.CONFIRMATION -> {
                // 严谨的运行时校验：如果不小心多写或少写了按钮，直接报错提示
                require(btns.size == 2) { "【UI 错误】CONFIRMATION 必须包含且仅包含 2 个 Button" }
                DialogType.confirmation(btns[0], btns[1])
            }

            UIType.MULTI_ACTION -> {
                DialogType.multiAction(btns, exitBtn, columns)
            }

            UIType.DIALOG_LIST -> {
                requireNotNull(dialogs) { "【UI 错误】DIALOG_LIST 必须通过参数传入 dialogs 集合" }
                DialogType.dialogList(dialogs, exitBtn, columns, buttonWidth)
            }

            UIType.SERVER_LINKS -> {
                DialogType.serverLinks(exitBtn, columns, buttonWidth)
            }
        }

        // 3. 注入底层 Builder
        this.type(paperType)
    }*/

    // 变成成员函数，去掉之前的 DialogRegistryEntry.Builder.
    fun DialogContent(title: Component, block: DialogBaseScope.() -> Unit) {
        // 为了避免变量名冲突，把原生的 base builder 改名叫 baseBuilder
        val baseBuilder = DialogBase.builder(title)
        val scope = DialogBaseScope(baseBuilder)

        scope.block()

        if (scope.bodyList.isNotEmpty()) {
            baseBuilder.body(scope.bodyList)
        }
        if (scope.inputList.isNotEmpty()) {
            baseBuilder.inputs(scope.inputList)
        }

        // 用外部传入的 entryBuilder 替代之前的 this
        entryBuilder.base(baseBuilder.build())
    }

    // 变成成员函数
    fun DialogType(
        type: UIType,
        columns: Int = 1,
        buttonWidth: Int = 200,
        dialogs: RegistrySet<Dialog>? = null,
        block: DialogTypeScope.() -> Unit = {}
    ) {
        val scope = DialogTypeScope().apply(block)
        val btns = scope.buttons
        val exitBtn = scope.exitButton

        val paperType = when (type) {
            UIType.NOTICE -> {
                if (btns.isEmpty()) DialogType.notice() else DialogType.notice(btns.first())
            }
            UIType.CONFIRMATION -> {
                require(btns.size == 2) { "【UI 错误】CONFIRMATION 必须包含且仅包含 2 个 Button" }
                DialogType.confirmation(btns[0], btns[1])
            }
            UIType.MULTI_ACTION -> {
                DialogType.multiAction(btns, exitBtn, columns)
            }
            UIType.DIALOG_LIST -> {
                requireNotNull(dialogs) { "【UI 错误】DIALOG_LIST 必须通过参数传入 dialogs 集合" }
                DialogType.dialogList(dialogs, exitBtn, columns, buttonWidth)
            }
            UIType.SERVER_LINKS -> {
                DialogType.serverLinks(exitBtn, columns, buttonWidth)
            }
        }

        // 用外部传入的 entryBuilder 替代之前的 this
        entryBuilder.type(paperType)
    }
}