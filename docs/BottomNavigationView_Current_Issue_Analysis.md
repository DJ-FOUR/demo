# BottomNavigationView 当前残留问题分析与修复建议

## 当前现象

截图显示底部导航栏仍然异常：

- 5 个导航项的图标基本可见。
- 文字标签只露出一部分，部分被底部裁切。
- 导航栏背景可见，位置大体在系统手势导航栏上方。
- 问题已经不是最初那种“图标只露 1/3、文字完全消失”的状态，而是导航项内容高度不足/垂直空间被压缩。

## 当前代码状态

相关文件：

- `app/src/main/java/com/example/demo/MainActivity.kt`
- `app/src/main/res/layout/activity_main.xml`
- `app/src/main/res/values/dimens.xml`

`MainActivity.kt` 当前已经改成：

```kotlin
ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
    WindowInsetsCompat.CONSUMED
}
```

这说明上一轮的“双重 insets padding”问题已经被处理。现在不应再把主要原因归结为 `return insets`。

`activity_main.xml` 当前底部导航栏：

```xml
<com.google.android.material.bottomnavigation.BottomNavigationView
    android:id="@+id/bottom_nav"
    android:layout_width="0dp"
    android:layout_height="@dimen/bottom_nav_height"
    android:background="@drawable/bg_bottom_nav"
    android:elevation="8dp"
    android:clipChildren="false"
    android:clipToPadding="false"
    app:itemIconTint="@color/bottom_nav_tint"
    app:itemTextColor="@color/bottom_nav_tint"
    app:labelVisibilityMode="labeled"
    app:layout_constraintBottom_toBottomOf="parent"
    app:itemActiveIndicatorStyle="@null"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:menu="@menu/bottom_nav_menu" />
```

`dimens.xml` 当前为：

```xml
<dimen name="bottom_nav_height">64dp</dimen>
```

## 根因判断

### 1. 64dp 高度不适合当前 Material3 BottomNavigationView

项目依赖：

```toml
material = "1.14.0"
```

从本地 `material-1.14.0.aar` 可确认 Material3 bottom navigation 相关默认尺寸包括：

```xml
<dimen name="m3_bottom_nav_min_height">80dp</dimen>
<dimen name="m3_bottom_nav_item_padding_top">12dp</dimen>
<dimen name="m3_bottom_nav_item_padding_bottom">16dp</dimen>
<dimen name="design_bottom_navigation_icon_size">24dp</dimen>
<dimen name="design_bottom_navigation_text_size">12sp</dimen>
<dimen name="design_bottom_navigation_active_text_size">14sp</dimen>
```

也就是说，Material3 的 BottomNavigationView 更接近 80dp 的容器预期，而项目强行给了 64dp。

在 5 个 item 全部显示 label 的情况下：

- 图标需要 24dp。
- label 至少需要 12sp/14sp 的高度。
- Material 内部还有顶部、底部、label padding。
- 当前还设置了 `app:labelVisibilityMode="labeled"`，所有文字始终显示。

因此 64dp 容器偏矮，文字标签容易被挤压到下边缘并被裁切。

### 2. Material 的 bottom navigation item 内容默认偏上/有内部 padding，不是简单居中

Material AAR 中 `res/layout/design_bottom_navigation_item.xml` 的 item 内容结构有这些特征：

```xml
<LinearLayout
    android:id="@id/navigation_bar_item_content_container"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="top|center_horizontal"
    android:layout_marginTop="@dimen/design_bottom_navigation_margin"
    android:orientation="vertical">
```

label group 还有：

```xml
android:paddingBottom="@dimen/m3_bottom_nav_item_padding_bottom"
```

所以它不是把 icon + label 在 64dp 里简单垂直居中。容器高度不足时，底部文字更容易被裁掉。

### 3. Insets 问题已经从“主因”变成“需要保持正确”的前置条件

当前 `WindowInsetsCompat.CONSUMED` 是对的，应保留。否则会回到上一版问题：根布局和 BottomNavigationView 都处理底部 system bar inset，导致内容进一步被压缩。

但现在即使消费了 insets，`BottomNavigationView` 自身 64dp 的高度仍不足以承载 Material3 labeled item。

## 推荐修复方案

### 首选方案：把底部导航高度改为 80dp

修改 `app/src/main/res/values/dimens.xml`：

```xml
<dimen name="bottom_nav_height">80dp</dimen>
```

这是最小、最符合 Material3 默认预期的修复。

同时保留 `MainActivity.kt` 中的：

```kotlin
WindowInsetsCompat.CONSUMED
```

不要改回 `insets`。

### 建议同时补充这些 XML 属性

在 `BottomNavigationView` 上显式声明常用尺寸，避免不同 Material 版本或主题 overlay 影响布局：

```xml
app:itemIconSize="24dp"
app:itemPaddingTop="8dp"
app:itemPaddingBottom="8dp"
```

完整示例：

```xml
<com.google.android.material.bottomnavigation.BottomNavigationView
    android:id="@+id/bottom_nav"
    android:layout_width="0dp"
    android:layout_height="@dimen/bottom_nav_height"
    android:background="@drawable/bg_bottom_nav"
    android:elevation="8dp"
    android:clipChildren="false"
    android:clipToPadding="false"
    app:itemIconSize="24dp"
    app:itemPaddingTop="8dp"
    app:itemPaddingBottom="8dp"
    app:itemIconTint="@color/bottom_nav_tint"
    app:itemTextColor="@color/bottom_nav_tint"
    app:labelVisibilityMode="labeled"
    app:itemActiveIndicatorStyle="@null"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:menu="@menu/bottom_nav_menu" />
```

如果 `itemPaddingTop/itemPaddingBottom` 在当前 Material 版本 XML 中不可用或编译报错，就只改高度到 80dp，先验证显示。

## 可选方案

### 方案 A：保留 64dp，但隐藏未选中文字

如果设计上必须保持 64dp 高度，可以把：

```xml
app:labelVisibilityMode="labeled"
```

改为：

```xml
app:labelVisibilityMode="selected"
```

这样只有选中项显示文字，其他项只显示 icon，垂直压力会小很多。

缺点：这会改变当前 UI 语义和视觉效果，不如 80dp 稳妥。

### 方案 B：使用自定义底部导航布局

如果想完全还原截图设计，而不是使用 Material 默认 bottom nav 行为，可以不用 `BottomNavigationView`，改成一个自定义 `LinearLayout`/`ConstraintLayout`：

- 5 个均分 item。
- 每个 item 内部自己控制 icon、label、选中态颜色。
- 外层容器高度 64dp 或 72dp。
- 自己处理点击切换 Fragment。

缺点：工作量更大，失去 Material 组件自带的 selected state/menu/badge/ripple 行为。

## 不推荐的修复方向

不要只继续调整这些内容：

- 改图标颜色。
- 改文字颜色。
- 改 `clipToPadding`。
- 继续反复切换 `WindowInsetsCompat.CONSUMED` 和 `insets`。
- 只改 `itemActiveIndicatorStyle="@null"`。

这些都不能解决当前的核心问题：`64dp` 高度不足以稳定容纳 Material3 labeled BottomNavigationView item。

## 验证标准

修复后应满足：

- 5 个图标完整显示。
- 5 个文字标签完整显示，不能被底部裁切。
- BottomNavigationView 背景仍在系统手势导航栏上方。
- 页面内容不被 BottomNavigationView 遮挡。
- 底部系统手势条不覆盖导航文字。

## 给接手 AI 的简短结论

上一版 insets 修复已经生效，现在的残留问题主要是 `BottomNavigationView` 高度太小。

优先修改：

```xml
<!-- app/src/main/res/values/dimens.xml -->
<dimen name="bottom_nav_height">80dp</dimen>
```

保留：

```kotlin
WindowInsetsCompat.CONSUMED
```

必要时再在 `BottomNavigationView` 上增加：

```xml
app:itemIconSize="24dp"
app:itemPaddingTop="8dp"
app:itemPaddingBottom="8dp"
```

