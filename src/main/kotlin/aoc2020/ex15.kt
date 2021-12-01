package aoc2020

import kotlin.math.sign

fun main() {
    println(nThNumber("8,11,0,19,1,2", 30_000_000))
    error("")
    require(nThNumber("1,3,2", 2020) == 1)
    require(nThNumber("2,1,3", 2020) == 10)
    require(nThNumber("1,2,3", 2020) == 27)
    require(nThNumber("2,3,1", 2020) == 78)
    require(nThNumber("3,2,1", 2020) == 438)
    require(nThNumber("3,1,2", 2020) == 1836)
    assertNValue("0,3,6", 30_000_000, 175594)
    assertNValue("1,3,2", 30_000_000, 2578)
    assertNValue("2,1,3", 30_000_000, 3544142)
    assertNValue("1,2,3", 30_000_000, 261214)
    assertNValue("2,3,1", 30_000_000, 6895259)
    assertNValue("3,2,1", 30_000_000, 18)
    assertNValue("3,1,2", 30_000_000, 362)
}

private fun assertNValue(numbers: String, n: Int, expected: Int) {
    val res = nThNumber(numbers, n)
    if (res == expected) {
        println("got $res !!!")
    } else {
        println("got $res instead of $expected")
        error("")
    }

}

private fun nThNumber(input: String, n: Int): Int {
    val lines = input.lines()
    require(lines.size == 1)
    val startingNumbers = lines[0].split(",").map { it.toInt() }
    return play(startingNumbers, n).last()
}

private data class NumberSpeak(val last: Int, val before: Int) {
    fun add(time: Int) = NumberSpeak(last = time, before = this.last)
    fun diff() = if (before == -1) 0 else last - before

    companion object {
        val EMPTY = NumberSpeak(-1, -1)
    }
}

fun play(starters: List<Int>, turns: Int): List<Int> {
    val lastTimes = mutableMapOf<Int, NumberSpeak>()
    val allTimes = mutableListOf<Int>()
    if (turns <= starters.size) return starters.take(turns)
    var lastNumber = 0
    (0 until turns).forEach { turn ->
        if (turn % 1_000 == 0) println(turn)
        val current = if (turn < starters.size) {
            starters[turn]
        } else {
            val lastTime = lastTimes[lastNumber]!!
            lastTime.diff()
        }
        allTimes.add(current)
        val lastSpeak = lastTimes[current] ?: NumberSpeak.EMPTY
        lastTimes[current] = lastSpeak.add(turn)
        lastNumber = current
    }
    return allTimes
}