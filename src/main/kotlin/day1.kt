
private fun tests() {
    val measures = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent()
    val numbers = measures.lines().map { it.toInt() }
    assertEquals(numberOfIncreases(numbers), 7)
}

fun main() {
    tests()
    val input = readInputFile("input1")
    println(part1(input))

    // sanity
    println("day1")
}

private fun parseInput(input: String) = input.lines().map { it.toInt() }

private fun part1(input: String): Int =
    numberOfIncreases(parseInput(input))

fun numberOfIncreases(numbers: List<Int>): Int {
    return numbers.zipWithNext().count { (a, b) -> b > a }
}
