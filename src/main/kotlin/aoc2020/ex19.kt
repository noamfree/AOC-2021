package aoc2020

import readInputFile

fun main() {
    val input = readInputFile("aoc2020/input19")
    //.replace(Regex("8: 42")) { "8: 42 | 42 8"}
    //.replace(Regex("11: 42 31")) { "11: 42 31 | 42 11 31"}
//    val input = """
//        0: 4 1 5
//        1: 2 3 | 3 2
//        2: 4 4 | 5 5
//        3: 4 5 | 5 4
//        4: "a"
//        5: "b"
//
//        ababbb
//        bababa
//        abbbab
//        aaabbb
//        aaaabbb
//    """.trimIndent()
    val rulesLines = input.split("\n\n")[0].lines().let {
        val index = it.indexOfFirst { it.startsWith("11:") }
        it.toMutableList().apply {
            val reg = (1..100).map {
                val list = List(it) { 42 } + List(it) { 31 }
                list.joinToString(" ")
            }
            val line = "11: " + reg.joinToString(" | ")
            set(index, line)
        }
    }
    println(rulesLines)
    val messageLines = input.split("\n\n")[1].lines()

    val rules = rulesLines.map {
        val s = it.split(": ")
        require(s.size == 2)
        val index = s[0].toInt()
        parseRule(index, s[1])
    }.associateBy { it.index }
    println(rules)

    val expandedRule = expandRule(0, rules)
    println(messageLines.count { expandedRule.matches(it) })
}

fun expandRule(index: RuleIndex, rules: Map<RuleIndex, RawRule>): Regex {
    val cache = mutableMapOf<RuleIndex, Regex>()
    return expandRuleRecursive(index, rules, cache)
}

fun expandRuleRecursive(
    index: RuleIndex,
    rules: Map<RuleIndex, RawRule>,
    cache: MutableMap<RuleIndex, Regex>
): Regex {
    return cache.getOrPut(index) {
        if (index == 8) {
            return expandRuleRecursive(42, rules, cache).pattern.let {
                Regex("($it+)")
            }
        }
        val rule = rules.getValue(index)
        when (rule) {
            is RawRule.Concat -> rule.concats.map {
                expandRuleRecursive(it, rules, cache)
            }.map {
                it.pattern
            }.joinToString("").let { Regex("($it)") }
            is RawRule.Or -> {
                rule.groups.map { group ->
                    group.map {
                        expandRuleRecursive(it, rules, cache)
                    }.map {
                        it.pattern
                    }.joinToString("").let { Regex("($it)") }
                }.joinToString("|").let {
                    Regex("($it)")
                }
            }
            is RawRule.Single -> Regex("(" + rule.string + ")")
        }.also {
            println("$rule matches $it")
        }
    }

}

private fun parseRule(index: RuleIndex, string: String): RawRule {
    return when {
        '"' in string -> {
            require(string[2] == '"')
            require(string.length == 3)
            RawRule.Single(index, string[1])
        }
        '|' in string -> {
            val s = string.split(" | ").filter { it.isNotEmpty() }
            RawRule.Or(index, s.map { it.split(" ").map { it.toInt() } })
        }
        else -> {
            val s = string.split(" ").map { it.toInt() }
            RawRule.Concat(index, s)
        }
    }
}

private fun tests() {
//    assertEquals(Rule.Single('a').matches("a"), 1)
//    assertEquals(Rule.Single('b').matches("a"), -1)
//    assertEquals(
//        Rule.Single('a').concat(Rule.Single('b')).matches("a"),
//        -1
//    )
    println("all tests passed")
}

private typealias RuleIndex = Int

sealed interface RawRule {
    val index: RuleIndex

    data class Single(override val index: RuleIndex, val string: Char) : RawRule

    data class Concat(
        override val index: RuleIndex,
        val concats: List<RuleIndex>
    ) : RawRule {
        override fun toString(): String {
            return concats.joinToString(" ")
        }
    }

    data class Or(
        override val index: RuleIndex,
        val groups: List<List<RuleIndex>>
    ) : RawRule {
        override fun toString(): String {
            return groups.map { it.joinToString(" ") }.joinToString("|")
        }
    }
}