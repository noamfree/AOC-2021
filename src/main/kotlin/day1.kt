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
    assertEquals(numberOfMaskedIncreasings(numbers, 3), 5)
}

private fun main() {
    tests()
    val input = readInputFile("input1")
    println(part1(input))
    println(part2(input))

    // sanity
    println("day1")
}

private fun part2(input: String): Int =
    numberOfMaskedIncreasings(parseInput(input), 3)

private fun numberOfMaskedIncreasings(numbers: List<Int>, maskSize: Int): Int {
    return numbers.windowed(maskSize).map { it.sum() }.zipWithNext().count { (a, b) -> b > a }
}

private fun part1(input: String): Int =
    numberOfIncreases(parseInput(input))

private fun parseInput(input: String) = input.lines().map { it.toInt() }

private fun numberOfIncreases(numbers: List<Int>): Int =
    numberOfMaskedIncreasings(numbers, 1)
