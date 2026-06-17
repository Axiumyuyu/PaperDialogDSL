*...少女祈祷中...*

# 对话框 DSL 用法

> **测试源码位于 `xyz.axiumyu.paperDialogDsl.test.DialogTest`**

一般对话框使用 `DialogSetup` 或者 `RootDialogSetup` 创建（用于创建 `RootRoute` 类型的路由时使用，会自动添加一个返回按钮）：

```kotlin
val dialog: BaseDialog = DialogSetup {
    //...
}
```

在 `AtomicRoute` 场景下，必须使用 `AtomicDialogSetup` 创建，此时不允许设置 `canCloseWithEscape()` 方法，会被覆盖为 `false`

```kotlin
val dialog: AtomicDialogBlueprint = AtomicDialogSetup {
    //...
}
```

`BaseDialog` 是一个包装器，`AtomicDialogBlueprint` 是它的 `value class` ~~
，它唯一的作用就是限制在AtomicRoute下仅允许使用该方法创建对话框~~

`BaseDialog` 是还未构建完成的对话框，可直接使用扩展方法注册并在 Bootstrap 阶段注册，如果需要构建为静态对话框实时展示，则需要使用
BaseDialog.build() 方法

## 对话框内容

对话框由两部分组成，上半部分的对话框主体，和下半部分的按钮

对话框主体内可以设置文字，物品，各类输入框

> 由于 Paper 的一个 Bug [Issue #13555](https://github.com/PaperMC/Paper/issues/13555)，若要在 Bootstrap 阶段注册对话框，则不允许在对话框主体内设置物品展示
> 
> 动态构建和展示的对话框无此要求

对话框下半部分只能设置按钮，根据对话框的类型还可以是对话框注册表列表

### 对话框主体

使用 `DialogContent` 声明

```kotlin
val dialog = DialogSetup {
    DialogContent {
        // 文本，物品，输入控件等
    }
}
```

所有组件都可以设置宽度和悬浮文本，所有输入组件都拥有一个 id，初始值

允许使用的组件类型：

- Text，文本组件
- ItemDisplay，物品展示

输入组件，每一个输入组件都拥有一个 id，可以在下方的按钮中通过回调函数获取其值：

- NumRangeInput，浮点数值滑动条，可设定范围，步长等，使用 `getFloat(id: String): Float?` 获取值，当 id 不存在时返回 null
- TextInput，文本输入框，可设定最大字符数，换行策略等，使用 `getText(id: String): String?` 获取值，当 id 不存在时返回 null
- BoolInput，复选框，使用 `getBoolean(id: String): Boolean?` 获取值，当 id 不存在时返回 null

### 对话框类型

使用 `DialogType`声明，有以下几种类型：

- NOTICE: 允许 0/1 个按钮
- CONFIRMATION：允许 2 个按钮
- MULTI_ACTION：允许任意数量的按钮 ~~（只要屏幕放得下）~~ 和一个可选的退出按钮
- DIALOG_LIST：需要传入一个 `RegistrySet<Dialog>`，和一个可选的退出按钮
- SERVER_LINKS：仅允许一个可选的退出按钮

```kotlin
val dialog = DialogSetup {
    DialogContent {
        
    }
    DialogType(UIType.MULTI_ACTION) {
        // 按钮，或者什么都没有...
    }
}
```

### 按钮

Button 及其变种如 NavButton，BackButton，ExitButton 只能在这里使用

除 ExitButton 外，其它按钮都可以用于构建行为

NavButton 适合：

- 当按钮的唯一行为就是“跳转到另一个页面”或者“更新当前页面上的某些内容”时

BackButton 适合：

- 当按钮的唯一行为就是“返回到上一个页面”时

ExitButton 必须作为退出页面的按钮，可以无参调用，它应当承载关闭页面以及处理善后工作的功能，尤其是在 `AtomicRoute` 中。

~~*写累了，歇会*~~
