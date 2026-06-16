package xyz.axiumyu.paperDialogDsl.test

import xyz.axiumyu.paperDialogDsl.PaperDialogDSL.Companion.mm
import xyz.axiumyu.paperDialogDsl.dialog.DialogSetup
import xyz.axiumyu.paperDialogDsl.dialog.RootDialogSetup
import xyz.axiumyu.paperDialogDsl.dialog.dsl.UIType
import xyz.axiumyu.paperDialogDsl.route.AutoRootSetup
import xyz.axiumyu.paperDialogDsl.route.DialogRouteContext
import xyz.axiumyu.paperDialogDsl.route.RootRoute
import xyz.axiumyu.paperDialogDsl.route.Route

// ==================
// 页面 1：首页 (无参数，使用 object)
// ==================
object HomeRoute2 : RootRoute {
    override fun render(context: DialogRouteContext) = RootDialogSetup {
        DialogContent(mm.deserialize("主菜单")) {
            Text(mm.deserialize("你好，${context.player.name}"))
        }
        DialogType(UIType.MULTI_ACTION) {
            Button(mm.deserialize("实体列表"), mm.deserialize(""), 100) { _, _ ->
                context.navigate(ListRoute)
            }
            Button(mm.deserialize("EditRoute"), mm.deserialize(""), 100) { _, _ ->
                context.navigate(EditRoute("uuid-1234", "Pig"))
            }
            Button(mm.deserialize("SettingRoute"), mm.deserialize(""), 100) { _, _ ->
                context.navigate(SettingsRoute())
            }

        }
    }
}

object HomeRoute : RootRoute {
    override fun render(context: DialogRouteContext) =
        AutoRootSetup(context, mm.deserialize("主菜单")) {
            "实体列表" to ListRoute
            "编辑" to EditRoute("uuid-1234", "Pig")
            "设置" to SettingsRoute()
            "物品" to ItemEditorMenu
        }
}

// ==================
// 页面 2：列表页 (无参数，使用 object)
// ==================
object ListRoute : Route {
    override fun render(context: DialogRouteContext) = DialogSetup {
        DialogContent(mm.deserialize("实体列表"))
        DialogType(UIType.MULTI_ACTION, columns = 2) {
            Button(mm.deserialize("编辑这只猪"), mm.deserialize(""), 100) { _, _ ->
                context.navigate(EditRoute("uuid-1234", "Pig"))
            }
            Button(mm.deserialize("返回"), mm.deserialize(""), 100) { _, _ ->
                context.goBack()
            }
            ExitButton()
        }
    }
}

// ==================
// 页面 3：编辑详情页 (带参数，使用 data class)
// ==================
data class EditRoute(val targetId: String, val entityType: String) : Route {
    override fun render(context: DialogRouteContext) = DialogSetup {
        DialogContent(mm.deserialize("编辑：$entityType")) {
            Text(mm.deserialize("当前正在操作 UUID: $targetId"))
        }
        DialogType(UIType.MULTI_ACTION) {
            Button(mm.deserialize("返回列表"), mm.deserialize(""), 100) { _, _ ->
                context.goBack()
            }
        }
    }
}

// 更新当前页面：定义页面的所有“可变状态”在主构造函数中，并设置默认值
data class SettingsRoute(
    val isNotificationsEnabled: Boolean = true,
    val clickCount: Int = 0
) : Route {

    override fun render(context: DialogRouteContext) = DialogSetup {

        DialogContent(mm.deserialize("个人设置")) {
            Text(mm.deserialize("当前点击次数: <aqua>$clickCount</aqua>"))
            // 根据状态动态渲染图标
            val statusIcon = if (isNotificationsEnabled) "<green>开启" else "<red>关闭"
            Text(mm.deserialize("消息通知: $statusIcon"))
        }

        DialogType(UIType.MULTI_ACTION, columns = 2) {

            // 按钮 1：动态开关
            Button(mm.deserialize("切换通知"), mm.deserialize(""), 100) { _, _ ->
                // 使用 Kotlin 的 copy() 生成一个只有布尔值取反的新状态
                // 然后用 replace() 替换当前路由，完成界面的“重绘”
                context.replace(this@SettingsRoute.copy(isNotificationsEnabled = !isNotificationsEnabled))
            }

            // 按钮 2：动态计数
            Button(mm.deserialize("+1s"), mm.deserialize(""), 100) { _, _ ->
                context.replace(this@SettingsRoute.copy(clickCount = clickCount + 1))
            }

            // 退出与返回
            Button(mm.deserialize("返回主菜单"), mm.deserialize(""), 100) { _, _ ->
                context.goBack()
            }
        }
    }
}
