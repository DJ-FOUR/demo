package com.example.demo.service

import kotlin.random.Random

class MockAiService {

    // --- Chat reply ---

    fun generateReply(userMessage: String): String {
        val input = userMessage.trim().lowercase()

        for ((keywords, reply) in chatRules) {
            if (keywords.any { input.contains(it) }) return reply
        }
        return fallbackReplies.random()
    }

    private val chatRules = listOf(
        listOf("特征值", "特征向量") to "特征值（eigenvalue）和特征向量（eigenvector）是线性代数中的核心概念。\n\n📌 定义：对于方阵 A，若存在非零向量 v 和标量 λ，使得 Av = λv，则 λ 为特征值，v 为对应的特征向量。\n\n📐 几何意义：矩阵 A 对向量 v 的线性变换，等价于将 v 沿原方向拉伸 λ 倍。\n\n💡 关键性质：\n• 特征值之和 = 矩阵的迹（对角元素之和）\n• 特征值之积 = 矩阵的行列式\n• 不同特征值对应的特征向量线性无关",

        listOf("行列式") to "行列式（determinant）是一个将方阵映射到标量的函数。\n\n📌 几何意义：行列式的绝对值表示线性变换对\"体积\"的缩放比例。\n\n例如 2×2 矩阵，行列式 = ad - bc，表示变换后单位正方形的面积缩放倍数。\n\n💡 关键性质：\n• det(A) = 0 ⇔ 矩阵不可逆\n• det(AB) = det(A)·det(B)\n• det(A⁻¹) = 1/det(A)",

        listOf("矩阵", "逆矩阵") to "矩阵是线性代数的基础工具。\n\n📌 逆矩阵：若 AB = BA = I，则 B 是 A 的逆矩阵，记作 A⁻¹。\n\n💡 常用求逆方法：\n1. 伴随矩阵法：A⁻¹ = adj(A)/det(A)\n2. 初等行变换法：[A|I] → [I|A⁻¹]\n\n⚠️ 注意：只有方阵且行列式不为 0 时才可逆。",

        listOf("线性变换", "线性空间") to "线性变换是保持向量加法和标量乘法不变的映射。\n\n📌 形式定义：T: V → W 是线性变换，当且仅当：\n• T(u + v) = T(u) + T(v)\n• T(c·v) = c·T(v)\n\n💡 核心理解：任何线性变换都可以用矩阵表示。矩阵乘法本质上就是在执行线性变换。",

        listOf("虚拟语气", "英语", "语法") to "虚拟语气（Subjunctive Mood）用于表达假设、愿望、建议等非事实情况。\n\n📌 三种主要类型：\n\n1️⃣ 与现在事实相反：If I were you, I would study harder.\n   结构：If + 过去式, would/could/might + 动词原形\n\n2️⃣ 与过去事实相反：If I had studied, I would have passed.\n   结构：If + had done, would have done\n\n3️⃣ 与将来可能相反：If it should rain, we would stay home.\n   结构：If + should/were to, would + 动词原形\n\n💡 记忆口诀：现在退一步（过去式），过去退两步（had done）",

        listOf("你好", "hello", "hi") to "你好！有什么学习上的问题需要我帮忙吗？无论是数学、英语还是其他学科，尽管问我！",

        listOf("谢谢", "感谢") to "不客气！学习路上有任何问题随时来找我。坚持就是胜利，加油！💪",

        listOf("导数", "微分", "积分") to "微积分是数学分析的基础。\n\n📌 导数：描述函数在某点的变化率\n📌 积分：求曲线下面积或反导数\n\n💡 牛顿-莱布尼茨公式：∫ₐᵇ f'(x)dx = f(b) - f(a)\n\n这是连接微分和积分的桥梁。",

        listOf("概率", "统计") to "概率论是研究随机现象的数学分支。\n\n📌 贝叶斯定理：P(A|B) = P(B|A)·P(A) / P(B)\n\n💡 实际应用：\n• 条件概率：已知某事件发生后，另一事件的概率\n• 全概率公式：通过完备事件组求概率\n• 贝叶斯推断：根据新证据更新概率估计"
    )

    private val fallbackReplies = listOf(
        "这是一个很好的问题！让我从基础概念开始梳理：这个问题涉及的核心知识点需要在理解基本定义的基础上，逐步深入。建议你先回顾相关章节的基础概念，然后我们再一起探讨具体细节。",
        "我理解你的困惑。这个知识点确实是很多同学的难点。让我换一个角度来解释：关键是要理解它背后的直觉（intuition），而不仅仅是记忆公式。",
        "好问题！解题思路可以分三步：\n1️⃣ 审题——明确已知条件和求解目标\n2️⃣ 联想——回顾相关定理和公式\n3️⃣ 执行——按逻辑顺序推导\n需要我针对某个具体类型展开讲解吗？",
        "这个问题在实际应用中非常重要。学术界对此有多种解释角度，我建议从最直观的几何/物理意义入手理解。需要我画个思维导图帮你梳理吗？",
        "让我帮你分析一下。这个问题的关键突破口在于找到合适的转换视角。很多时候，换个角度看问题，复杂的问题就会变得简单。你目前对这个概念的理解到了哪个程度？"
    )

    // --- Practice diagnosis ---

    fun diagnose(subject: String, questionText: String, steps: List<String>): String {
        val nonEmptySteps = steps.filter { it.isNotBlank() }
        if (nonEmptySteps.isEmpty()) {
            return "⚠️ 未检测到有效解答步骤。请至少输入一个解题步骤后再提交。"
        }
        if (nonEmptySteps.size < 2) {
            return "📋 诊断报告\n\n你的解答只有 ${nonEmptySteps.size} 步，建议尝试拆分为更细致的步骤。\n\n✅ 优点：已开始尝试作答\n⚠️ 建议：\n• 将解题过程拆分为\"审题→设变量→列方程→求解→验证\"等步骤\n• 每一步对应一个关键推导\n• 完成后再次提交获取完整诊断"
        }

        val suggestions = diagnosisTemplate.random()
        return buildString {
            appendLine("📋 AI 诊断报告")
            appendLine()
            appendLine("📊 总体评价：解答过程包含 ${nonEmptySteps.size} 个步骤，结构较为完整。")
            appendLine()
            appendLine("✅ 亮点：")
            appendLine("• 步骤拆分清晰，逻辑链条完整")
            appendLine("• 关键推导方向正确")
            appendLine()
            appendLine("⚠️ 改进建议：")
            suggestions.forEach { appendLine("• $it") }
            appendLine()
            appendLine("📈 预估得分：${70 + nonEmptySteps.size * 5} / 100")
            appendLine()
            appendLine("💡 下一步：建议针对薄弱环节做 2-3 道变式练习巩固。")
        }
    }

    fun scorePractice(steps: List<String>): Int {
        val count = steps.filter { it.isNotBlank() }.size
        return (60 + count * 10).coerceAtMost(100)
    }

    private val diagnosisTemplate = listOf(
        listOf(
            "注意区分充分条件和必要条件",
            "第三步的推导可以更严谨地引用定理",
            "建议检查计算的中间结果是否有误"
        ),
        listOf(
            "第二步的代数变换可简化",
            "缺少对特殊情况的讨论（如分母为0）",
            "最终答案可进一步化简"
        ),
        listOf(
            "检查第二步到第三步的跳步",
            "建议标注所使用的定理名称",
            "可通过代入特例验证最终结果"
        )
    )

    // --- Question generation for lab ---

    fun generateQuestion(weakPoints: List<String>): GeneratedQuestion {
        val topic = if (weakPoints.isNotEmpty()) weakPoints.random() else "线性代数"
        val template = questionTemplates[topic] ?: questionTemplates.values.random()
        val q = template.random()

        return GeneratedQuestion(
            subject = topic,
            question = q.first,
            hint = q.second
        )
    }

    data class GeneratedQuestion(
        val subject: String,
        val question: String,
        val hint: String
    )

    private val questionTemplates = mapOf(
        "特征值与特征向量" to listOf(
            ("已知矩阵 A = [[3, 1], [0, 2]]，\n" +
                    "（1）求 A 的特征值和特征向量\n" +
                    "（2）判断 A 是否可对角化，若可以，求可逆矩阵 P 和对角矩阵 Λ") to
                    "提示：从特征方程 |A - λI| = 0 入手，分别计算每个特征值对应的特征向量。",
            ("设向量 v = (1, 2)，矩阵 B = [[4, -2], [1, 1]]，\n" +
                    "验证 v 是否为 B 的特征向量，并说明理由。") to
                    "提示：计算 Bv，检查是否存在标量 λ 使得 Bv = λv。"
        ),
        "行列式" to listOf(
            ("计算行列式：\n" +
                    "| 1  2  3 |\n" +
                    "| 4  5  6 |\n" +
                    "| 7  8  9 |\n" +
                    "并说明结果的含义。") to
                    "提示：可用展开定理或行变换化简计算。注意观察行之间的关系。",
            ("已知三阶方阵 C 的行列式为 5，\n" +
                    "求：（1）|2C|  （2）|C⁻¹|  （3）|C^T|") to
                    "提示：利用行列式性质：|kA| = kⁿ|A|（n为阶数），|A⁻¹| = 1/|A|，|A^T| = |A|。"
        ),
        "矩阵运算" to listOf(
            ("已知 A = [[1, 2], [3, 4]]，B = [[2, 0], [1, 3]]，\n" +
                    "计算：（1）A + B  （2）AB  （3）A^T B\n" +
                    "注意：矩阵乘法不满足交换律，请验证 AB 与 BA 是否相等。") to
                    "提示：矩阵乘法规则——结果矩阵第 i 行第 j 列 = A 的第 i 行 × B 的第 j 列。",
            ("利用初等行变换求矩阵 A = [[2, 1, 1], [1, 2, 1], [1, 1, 2]] 的逆矩阵。") to
                    "提示：构造增广矩阵 [A|I]，通过行变换将左侧变为单位矩阵。"
        ),
        "英语虚拟语气" to listOf(
            ("用所给动词的适当形式填空：\n" +
                    "1. If I ____ (be) you, I ____ (accept) the offer.\n" +
                    "2. If she ____ (study) harder, she ____ (pass) the exam yesterday.\n" +
                    "3. If it ____ (rain) tomorrow, we ____ (cancel) the picnic.") to
                    "提示：注意虚拟语气的三种时间类型——与现在、过去、将来事实相反，时态需\"后退一步\"。",
            ("翻译下列虚拟语气句子：\n" +
                    "1. 要是我当时听了你的建议就好了。\n" +
                    "2. 如果我是校长，我会减少作业量。\n" +
                    "3. 没有水，地球上就不会有生命。") to
                    "提示：中文虚拟语气通常用\"要是…就好了\"、\"如果…会…\"、\"没有…就没有…\"来表达。"
        ),
        "线性代数" to listOf(
            ("已知向量组 α₁=(1,0,1), α₂=(0,1,1), α₃=(1,1,2)，\n" +
                    "判断该向量组的线性相关性。若线性相关，求其秩和一个极大无关组。") to
                    "提示：构造矩阵，通过行变换化为行阶梯形，观察非零行数即为秩。",
            ("设线性方程组：\n" +
                    "x + 2y - z = 4\n" +
                    "2x - y + 3z = 1\n" +
                    "3x + y + 2z = 5\n" +
                    "判断该方程组解的情况（唯一解/无穷解/无解），并求解。") to
                    "提示：写出增广矩阵，通过行变换判断 rank(A) 与 rank(A|b) 的关系。"
        )
    )
}
