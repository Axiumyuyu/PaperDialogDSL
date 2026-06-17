package xyz.axiumyu.paperDialogDsl.test

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import org.bukkit.NamespacedKey
import xyz.axiumyu.paperDialogDsl.PaperDialogDSL.Companion.mm
import xyz.axiumyu.paperDialogDsl.dialog.DialogSetup
import xyz.axiumyu.paperDialogDsl.dialog.dsl.UIType
import xyz.axiumyu.paperDialogDsl.dialog.dsl.validate
import xyz.axiumyu.paperDialogDsl.route.*

// ==========================================
// 1. 物品可视化编辑器：主菜单
// ==========================================
object ItemEditorMenu : RootRoute {
    override fun render(context: DialogRouteContext) = AutoRootSetup(
        context = context,
        title = mm.deserialize("物品可视化编辑器")
    ) {
        "锻造史诗长剑" to EditItemBasicRoute("netherite_sword")
        "打造挖矿神器" to EditItemBasicRoute("netherite_pickaxe")
    }
}

// ==========================================
// 2. 步骤 1/3：基础信息配置
// ==========================================
data class EditItemBasicRoute(
    val itemType: String,
    val currentName: String = "",
    val errorMsg: String? = null
) : Route {

    override fun render(context: DialogRouteContext) = DialogSetup {

        DialogContent(mm.deserialize("步骤 1/3：基础配置")) {
            if (errorMsg != null) {
                Text(mm.deserialize("<red>⚠ 校验失败: $errorMsg"))
            } else {
                Text(mm.deserialize("<gray>请输入你期望的自定义名称："))
            }

            Text(mm.deserialize("正在编辑: <aqua>${itemType}"))
            TextInput("input_name", mm.deserialize("物品名称"))
        }

        DialogType(UIType.CONFIRMATION) {
            Button(mm.deserialize("<gold>下一步"), mm.deserialize("配置附魔属性"), 100) { view, _ ->
                validate({
                    context.replace(this@EditItemBasicRoute.copy(errorMsg = it))
                }) {
                    val input = view.getText("input_name").withError("未知输入框名称")
                    input.isNotEmpty().withError("名称不能为空")
                    context.navigate(EditItemEnchantsRoute(itemType, input))
                }
            }

            Button(mm.deserialize("取消"), mm.deserialize("返回主菜单"), 100) { _, _ ->
                context.goBack()
            }
        }
    }
}

// ==========================================
// 3. 步骤 2/3：动态附魔配置 (Fail-Fast 强校验)
// ==========================================
data class EditItemEnchantsRoute(
    val itemType: String,
    val validatedName: String,
    val currentEnchants: Map<String, Int> = emptyMap(),
    val errorMsg: String? = null
) : Route {

    override fun render(context: DialogRouteContext) = DialogSetup {

        DialogContent(mm.deserialize("步骤 2/3：附魔与属性")) {
            Text(mm.deserialize("当前编辑: <gold>$validatedName"))
            if (errorMsg != null) Text(mm.deserialize("<red>⚠ $errorMsg"))
            Text(mm.deserialize("<gray>将等级滑至 0 并在下次刷新时自动移除。"))

            for ((enchName, level) in currentEnchants) {
                val safeInputId = "ench_${enchName.replace(":", "_")}"

                // 使用修改后的 DSL 方法，直接传入 Component 名称，并带有默认值，这样数字和标签都能正常显示
                NumRangeInput(
                    id = safeInputId,
                    name = mm.deserialize("<aqua>✦ $enchName"),
                    range = 0f to 10f,
                    init = level.toFloat(),
                    step = 1f
                )
            }

            Text(mm.deserialize("<yellow>--- 新增附魔 ---"))
            TextInput("new_enchant_name", mm.deserialize("输入命名空间ID(如 sharpness)"))
        }

        DialogType(UIType.MULTI_ACTION, columns = 2) {

            Button(mm.deserialize("<green>➕ 添加/刷新"), mm.deserialize("保存滑块更改"), 100) { view, _ ->

                // 1. 收集旧数据
                val updatedEnchants = currentEnchants.mapValues { (enchName, _) ->
                    val safeInputId = "ench_${enchName.replace(":", "_")}"
                    view.getFloat(safeInputId)?.toInt() ?: 0
                }.filterValues { it > 0 }.toMutableMap()

                validate({
                    context.replace(
                        this@EditItemEnchantsRoute.copy(
                            currentEnchants = updatedEnchants,
                            errorMsg = it
                        )
                    )
                }) {
                    val newEnchantInput = view.getText("new_enchant_name")?.trim()?.lowercase() ?: ""
                    val pureName = newEnchantInput.split(":")
                    val key =
                        if (pureName.size == 1) {
                            NamespacedKey.minecraft(pureName[0])
                        } else {
                            NamespacedKey(pureName[0], pureName[1])
                        }.withError("附魔名称含有非法字符")

                    RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)[key].withError("未知附魔")
                    updatedEnchants[newEnchantInput] = 1
                    context.replace(
                        this@EditItemEnchantsRoute.copy(
                            currentEnchants = updatedEnchants,
                            errorMsg = null
                        )
                    )
                }
            }

            Button(mm.deserialize("<gold>下一步"), mm.deserialize("最后确认"), 100) { view, _ ->
                val finalEnchants = currentEnchants.mapValues { (enchName, _) ->
                    val safeInputId = "ench_${enchName.replace(":", "_")}"
                    view.getFloat(safeInputId)?.toInt() ?: 0
                }.filterValues { it > 0 }

                context.navigate(EditItemAdvancedRoute(itemType, validatedName, finalEnchants))
            }

            Button(mm.deserialize("上一步"), mm.deserialize("修改名字"), 100) { _, _ -> context.goBack() }
        }
    }
}

// ==========================================
// 4. 步骤 3/3：最终检阅与物理成型
// ==========================================
data class EditItemAdvancedRoute(
    val itemType: String,
    val validatedName: String,
    val finalEnchants: Map<String, Int>,
    val hasGlint: Boolean = false
) : Route {

    override fun render(context: DialogRouteContext) = DialogSetup {

        DialogContent(mm.deserialize("步骤 3/3：最终检阅")) {
            Text(mm.deserialize("即将生成: <gold>$validatedName"))
            Text(mm.deserialize("包含 <aqua>${finalEnchants.size}</aqua> 个附魔"))
        }

        DialogType(UIType.MULTI_ACTION, columns = 2) {

            Button(
                mm.deserialize(if (hasGlint) "光效: <green>开" else "光效: <gray>关"),
                mm.deserialize(""),
                100
            ) { _, _ ->
                context.replace(this@EditItemAdvancedRoute.copy(hasGlint = !hasGlint))
            }

            Button(mm.deserialize("<green>✔ 锻造并给予"), mm.deserialize("不可撤销"), 100) { _, _ ->
                val item = RegistryAccess.registryAccess().getRegistry(RegistryKey.ITEM)[NamespacedKey.minecraft(itemType)]!!.createItemStack(1)
                item.setData(DataComponentTypes.CUSTOM_NAME, mm.deserialize("<gold>$validatedName"))
                if (hasGlint) item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)

                if (finalEnchants.isNotEmpty()) {
                    val enchantBuilder = ItemEnchantments.itemEnchantments()
                    finalEnchants.forEach { (enchName, level) ->
                        val pureName = enchName.split(":")
                        val key = if (pureName.size == 1) {
                            NamespacedKey.minecraft(pureName[0])
                        } else {
                            NamespacedKey(pureName[0], pureName[1])
                        }

                        val enchant = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)[key]
                        if (enchant != null) {
                            enchantBuilder.add(enchant, level)
                        }
                    }
                    item.setData(DataComponentTypes.ENCHANTMENTS, enchantBuilder.build())
                }

                context.player.inventory.addItem(item)

                DialogRouter.clearHistory(context.player)
                context.player.closeInventory()
                context.player.sendMessage(mm.deserialize("<green>叮！装备打造完成！"))
            }

            Button(mm.deserialize("上一步"), mm.deserialize("修改附魔"), 100) { _, _ -> context.goBack() }
        }
    }
}
