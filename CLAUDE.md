# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew lint                   # Run lint checks
./gradlew test                   # Run unit tests
./gradlew connectedAndroidTest   # Run instrumented tests on connected device/emulator
```

Uses Gradle with Kotlin DSL and a version catalog at `gradle/libs.versions.toml`. AGP 9.2.1, target SDK 36, min SDK 24, Java 11.

The build requires `JAVA_HOME` to be set. Android Studio's bundled JDK is at a path like `<studio>/jbr`.

## Architecture

Single-Activity app (`MainActivity`) with bottom navigation switching between five fragments. Uses **ViewModel → Repository → LocalDataSource** layered architecture to prepare for future cloud backend swap.

### Dependency wiring

`ServiceLocator` (singleton) holds all Repository and MockAiService instances. `AppViewModelFactory` creates ViewModels with their dependencies. Fragments obtain ViewModels via `ViewModelProvider(this, AppViewModelFactory())`.

### Package structure

- **`model/`** — Data classes: `TodoItem`, `ChatMessage`, `KnowledgePoint`, `RadarDimension`, `ConceptNode`, `QuestionStep`, `PracticeSession`, `UserProfile`
- **`repository/`** — Interfaces: `TodoRepository`, `ChatRepository`, `PracticeRepository`, `ProgressRepository`. Define contracts that can be swapped for cloud implementations later.
- **`data/local/`** — In-memory implementations: `LocalTodoRepository`, `LocalChatRepository`, `LocalPracticeRepository`, `LocalProgressRepository`
- **`service/`** — `MockAiService`: keyword-matched chat replies, template-based question generation, practice diagnosis generation. No real AI/API calls.
- **`viewmodel/`** — `DashboardViewModel`, `CourseViewModel`, `ChatViewModel` (uses coroutines for async AI reply), `LabViewModel` (uses coroutines for generation simulation), `GraphViewModel`, `ProfileViewModel`
- **`adapter/`** — RecyclerView adapters: `TodoAdapter` (uses `ListAdapter` + `DiffUtil`), `ChatMessageAdapter` (uses `getItemViewType` for user/AI bubble layout switching, `updateMessages()` for data refresh)
- **`view/`** — Four custom `View` subclasses that draw with `Canvas`:
  - `MasteryGaugeView` — circular progress arc with percentage text
  - `RadarChartView` — multi-axis radar/spider chart with configurable dimensions
  - `KnowledgeGraphView` — node-edge concept graph (mastery-driven coloring, locked/unlocked states)
  - `DrawingCanvasView` — freehand drawing with dot-grid background, supports clear
- **`ui/dashboard/`** — Dashboard tab: greeting, mastery gauge bound to todo completion %, inline todo list with add/toggle/delete, focus timer
- **`ui/course/`** — Course tab: multi-step answer input, drawing canvas, submit triggers local AI diagnosis (score + suggestions)
- **`ui/lab/`** — Lab tab: AI variant-exercise generation with loading/content/empty states, dynamically updates question/hint/analysis
- **`ui/graph/`** — Graph tab: hosts `KnowledgeGraphView` and `RadarChartView`, data from `GraphViewModel`
- **`ui/profile/`** — Profile tab: static display with ViewModel ready for future dynamic updates
- **`ui/chat/`** — `ChatBottomSheet` (extends `BottomSheetDialogFragment`): AI mentor chat with keyword-matched local replies (800-1200ms simulated delay) and quick-action chips

### Theme & Resources

Material 3 DayNight theme with a blue primary palette (`#0035C5`). All colors defined as named resources in `colors.xml`. Layouts are XML-based (`fragment_*.xml`, `dialog_chat.xml`, `item_*.xml`). Drawable XMLs for shapes (cards, buttons, badges, chips). Vector drawable icons (`ic_*.xml`).

### Navigation

`MainActivity` creates a new Fragment instance on each bottom-nav tap and replaces `R.id.fragment_container`. No `Navigation` component or back-stack — just `FragmentTransaction.replace()`. The FAB triggers `ChatBottomSheet`. Cross-tab navigation happens by setting `bottom_nav.selectedItemId`.
