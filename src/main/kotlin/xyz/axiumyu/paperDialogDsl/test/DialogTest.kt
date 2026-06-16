package xyz.axiumyu.paperDialogDsl.test

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.data.dialog.body.DialogBody
import org.bukkit.inventory.ItemType
import xyz.axiumyu.paperDialogDsl.PaperDialogDSL.Companion.mm
import xyz.axiumyu.paperDialogDsl.dialog.BaseDialog
import xyz.axiumyu.paperDialogDsl.dialog.DialogSetup
import xyz.axiumyu.paperDialogDsl.dialog.build
import xyz.axiumyu.paperDialogDsl.dialog.dsl.UIType
import java.time.Duration

// example
val newDialog = DialogSetup {
    DialogContent(mm.deserialize("Title")) {
        canCloseWithEscape(true)
        NumRangeInput("test2", mm.deserialize("<aqua>输入数字"), 0f to 100f, 0f, 1.0f, 300)
        BoolInput("xyz/axiumyu/paperDialogDsl/test", mm.deserialize("勾选<sprite:blocks:block/stone>"))
        TextInput("test3", mm.deserialize("<sprite:\"minecraft:items\":item/porkchop>请输入文本"))
    }
    DialogType(UIType.NOTICE) {
        Button(
            mm.deserialize("1 right"),
            mm.deserialize("2 right"),
            100,
            -1,
            Duration.ofMinutes(5)
        ) { view, audience ->
            val test2 = view.getFloat("test2") ?: return@Button
            audience.sendMessage(mm.deserialize("test2: $test2"))
        }
    }
}

// use .build() to turn this to a dialog that can be opened.
val dialog1 = newDialog.build()

val dialog2: BaseDialog = DialogSetup {

    DialogContent(mm.deserialize("title2")) {

        // if uncomment these lines, you can not register it in bootstrap. See https://github.com/PaperMC/Paper/issues/13555
        val item = ItemType.DIAMOND.createItemStack(1).apply {
            setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
        }
        ItemDisplay(item) {
            showDecorations(true)
            description(DialogBody.plainMessage(mm.deserialize("123"), 20))
        }


        Text(mm.deserialize("this is a test text123123123"))

        BoolInput("bool1", mm.deserialize("设置1"))
        BoolInput("bool2", mm.deserialize("设置2"))
        BoolInput("bool3", mm.deserialize("设置3"))
    }

    DialogType(UIType.MULTI_ACTION) {
        Button(
            mm.deserialize("get bool1"),
            mm.deserialize("hover1"),
            20
        ) { view, audience ->
            audience.sendMessage(mm.deserialize("bool1: ${view.getBoolean("bool1")}"))
        }
        Button(
            mm.deserialize("get bool2"),
            mm.deserialize("hover2"),
            20
        ) { view, audience ->
            audience.sendMessage(mm.deserialize("bool2: ${view.getBoolean("bool2")}"))
        }
        Button(
            mm.deserialize("get bool3"),
            mm.deserialize("hover3"),
            20
        ) { view, audience ->
            audience.sendMessage(mm.deserialize("bool1: ${view.getBoolean("bool3")}"))
        }
        Button(
            mm.deserialize("get bool4"),
            mm.deserialize("hover4"),
            20
        ) { view, audience ->
            audience.sendMessage(mm.deserialize("nope"))
        }
    }
}
