# BottomNavigationView 图标/文字显示异常修复说明

## 问题现象

`BottomNavigationView` 的按钮组件（图标 + 文字标签）显示异常：

- 早期：按钮完全不可见，只能看到白色导航栏背景
- 修复 padding 后：图标只露出约 1/3，文字标签完全消失
- 导航栏背景本身完整，白色弧形区域正常显示

## 根因分析

**双重 insets padding 导致内容区域被压缩到只剩 ~24dp。**

代码位置：`app/src/main/java/com/example/demo/MainActivity.kt`

```kotlin
ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
    insets  // ← 致命问题：原始 insets 原样返回，没有消费
}
```

### 问题机制（分步拆解）

1. **根布局消费了 systemBars insets**
   - `R.id.main`（ConstraintLayout）获取 `systemBars.bottom`（系统导航栏高度，约 20~40dp）
   - 根布局设置了 `paddingBottom = systemBars.bottom`
   - 这一步把 `BottomNavigationView` 推到了系统手势导航栏上方，确保不被系统 scrim 遮挡

2. **insets 没有被消费，继续向下传递**
   - `return insets` 返回的是**原始的、完整的** `WindowInsetsCompat`
   - Android 的 insets 分发链会继续把同一个 `systemBars.bottom` 传递给所有子 View

3. **BottomNavigationView 再次收到同样的 insets**
   - Material 3（`com.google.android.material:material:1.14.0`）的 `BottomNavigationView` 内部自动注册了 insets 处理逻辑
   - 当它收到 `navigationBars.bottom` inset 时，会**自动把它转化为自己的 `paddingBottom`**

4. **内容区域被极度压缩**
   - `BottomNavigationView` 外部高度：64dp
   - 内部自动添加的 `paddingBottom`：~40dp（系统导航栏高度）
   - 留给图标 + 文字标签的实际空间：64 - 40 = **~24dp**
   - Material 3 NavigationBarItem（图标 24dp + 文字 12sp + 间距 + active indicator）需要约 44-48dp 的垂直空间
   - 空间不足时，item 内容被内部裁剪机制吞掉，所以只看得见图标下半截，文字完全消失

### 为什么不是"位置不对"

`BottomNavigationView` 在屏幕上的**绝对位置是对的**——它在系统导航栏上方。白色导航栏背景也完整显示。

问题不是"导航栏放错了地方"，而是导航栏**内部的内容区域**被 Material 3 自动添加的 insets padding 吃掉了。背景 drawable 绘制在整个 64dp 区域内，而内容（icon + label）被挤到了仅剩的 24dp 空间里。

## 修复方案

### 核心原则

在根布局消费 systemBars insets 后，**必须告诉 insets 分发链这些 insets 已经被消费完毕**，否则子 View（特别是 Material 3 组件）会重复处理。

### 修复代码

修改 `app/src/main/java/com/example/demo/MainActivity.kt`：

```kotlin
ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
    WindowInsetsCompat.CONSUMED  // 关键：消费掉 insets，阻止子 View 重复处理
}
```

或更精细地只消费 systemBars 类型：

```kotlin
ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
    insets.inset(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
}
```

### 推荐用 `WindowInsetsCompat.CONSUMED`

在这个项目的简单布局场景下，`CONSUMED` 是最简洁有效的方案。它明确表示：

> "所有类型的 insets 都已经在当前 View（根布局）层面处理完毕，子 View 无需再处理。"

如果项目未来有更复杂的 insets 需求（如 IME、DisplayCutout 等），可以改用 `inset()` 缩小剩余可用区域。

## 验证方法

修复后，BottomNavigationView 应该：

- 导航栏背景（浅紫色 `surface_variant`）完整显示在屏幕底部
- 5 个按钮图标完整显示（24dp 图标不裁剪）
- 5 个文字标签完整显示（12sp 文字不溢出）
- 系统手势导航栏的 scrim 不遮挡任何内容
- 底部 padding 正确，内容不会被系统手势条覆盖

## 补充说明：为什么之前越改越糟

在定位这个问题的过程中，曾经尝试过以下修改，它们虽然方向正确，但没有触及真正的根因：

| 修改 | 效果 | 问题 |
|------|------|------|
| 改图标颜色为黑色 | 无效果 | 图标本身被裁剪了，颜色再深也看不见 |
| 改未选中颜色为 `on_surface` | 无效果 | 同上，内容区域不够，颜色不解决问题 |
| 改导航栏背景为 `surface_variant` | 背景可见 | 只能看到背景，内容仍然被裁剪 |
| 删除外部 padding | 图标露出一截 | 只是减少了第一次挤压，insets 双重处理仍然存在 |
| 加 `itemActiveIndicatorStyle="@null"` | 略好 | 节省了 active indicator 空间，但主要根因未解决 |

**唯一需要改的就是 `return insets` → `return WindowInsetsCompat.CONSUMED`。**

## 相关文件

- `app/src/main/java/com/example/demo/MainActivity.kt`
- `app/src/main/res/layout/activity_main.xml`
