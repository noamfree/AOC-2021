private val testInput1 = """
    00100
    11110
    10110
    10111
    10101
    01111
    00111
    11100
    10000
    11001
    00010
    01010
""".trimIndent()

private fun tests() {
    assertEquals(gammaRate(parseInput(testInput1)), "10110")
    assertEquals(gammaRateDecimal(parseInput(testInput1)), 22)
    assertEquals(epsilonRate(parseInput(testInput1)), "01001")
    assertEquals(epsilonRateDecimal(parseInput(testInput1)), 9)
    assertEquals(part1(testInput1), 198)
    assertEquals(oxygenRating(parseInput(testInput1)), 23)
    assertEquals(co2Rating(parseInput(testInput1)), 10)
}

private fun main() {
    tests()

    val input = readInputFile("input3")
    println("part 1:")
    println(part1(input))
    println("part 2:")
    println(part2(input))


    println()
    println("day3")
}

private fun part1(input: String): Int {
    val numbers = parseInput(input)
    return gammaRateDecimal(numbers) * epsilonRateDecimal(numbers)
}

private fun parseInput(input: String): List<String> {
    return input.lines()
}

fun epsilonRateDecimal(numbers: List<String>): Int = epsilonRate(numbers).toInt(2)

fun epsilonRate(numbers: List<String>): String = gammaRate(numbers).map {
    if (it == '0') '1' else '0'
}.joinToString("")

fun gammaRateDecimal(numbers: List<String>): Int = gammaRate(numbers).toInt(2)

fun gammaRate(numbers: List<String>): String {
    val len = numbers.first().length
    return (0 until len).map { index ->
        numbers.count { it[index].toString().toInt() == 1 }
    }.map {
        if (it > numbers.size / 2) 1 else 0
    }.joinToString("")
}

private fun oxygenRating(numbers: List<String>): Int {
    var candidates = numbers
    var criteriaIndex = 0
    while (candidates.size > 1) {
        val zeroCount = candidates.count { it[criteriaIndex] == '0' }
        val criteria = if (zeroCount > candidates.size / 2) '0' else '1'
        candidates = candidates.filter { it[criteriaIndex] == criteria }
        criteriaIndex++
    }
    require(candidates.size == 1)
    return candidates.first().toInt(2)
}

private fun co2Rating(numbers: List<String>): Int {
    var candidates = numbers
    var criteriaIndex = 0
    while (candidates.size > 1) {
        val zeroCount = candidates.count { it[criteriaIndex] == '0' }
        val criteria = if (zeroCount <= candidates.size / 2) '0' else '1'
        candidates = candidates.filter { it[criteriaIndex] == criteria }
        criteriaIndex++
    }
    require(candidates.size == 1)
    return candidates.first().toInt(2)
}

private fun part2(input: String): Int {
    val numbers = parseInput(input)
    return co2Rating(numbers) * oxygenRating(numbers)
}
