package xyz.axiumyu.paperDialogDsl.route

import net.kyori.adventure.text.Component
import xyz.axiumyu.paperDialogDsl.PaperDialogDSL.Companion.mm
import xyz.axiumyu.paperDialogDsl.dialog.dsl.PaperDialogDsl

@PaperDialogDsl
class AutoMenuScope {
    val menuItems = mutableListOf<Pair<Component, RouteBase>>()

    infix fun Component.to(target: RouteBase) {
        menuItems.add(Pair(this, target))
    }

    infix fun String.to(target: RouteBase) {
        menuItems.add(Pair(mm.deserialize(this), target))
    }
}