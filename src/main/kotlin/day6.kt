private val testInput1 = """
   3,4,3,1,2
""".trimIndent()

fun main() {
    //val input = testInput1
    val input = readInputFile("input6")
    val initialState = input.split(",").map { it.toInt() }
    println(initialState.sumOf { it.numberAfterNDays(256) })

    println("day6")
}

private val fishCache = mutableMapOf<Int, Long>()

fun Int.numberAfterNDays(days: Int): Long {
    if (days == 0) return 1
    if (this == 0) return fishCache.getOrPut(days) {
        6.numberAfterNDays(days-1) + 8.numberAfterNDays(days-1)
    }
    return (this-1).numberAfterNDays(days-1)
}

var count = 0

private fun passDay(fish: List<Int>): List<Int> {
    count++
    println("day $count")
    var new = 0
    val aged = fish.map {
        if (it > 0) it - 1
        else 6.also {
            new++
        }
    }
    return aged + List(new) { 8 }
}

private fun passDay(int: Int): Int {
    return if (int > 0) int - 1
    else 6
}

