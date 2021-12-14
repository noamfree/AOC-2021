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
    assertEquals(numberOfMaskedIncreasings2(numbers, 3), 5)
    assertEquals(
        numberOfMaskedIncreasings3(parseInput(readInputFile("input1")), 5),
        numberOfMaskedIncreasings(parseInput(readInputFile("input1")), 5),
    )
    assertEquals(
        numberOfMaskedIncreasings3(parseInput(readInputFile("input1")), 5),
        numberOfMaskedIncreasings2(parseInput(readInputFile("input1")), 5),
    )
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

// solving in a single iteration. since addition is reversible, we could remove the first
// element from the mask, and add a new one
private fun numberOfMaskedIncreasings2(numbers: List<Int>, maskSize: Int): Int {
    var increasings = 0
    var currentSum = numbers.take(maskSize).sum()
    var index = maskSize
    while (index < numbers.size) {
        val newSum = currentSum - numbers[index - maskSize] + numbers[index]
        if (currentSum < newSum) {
            increasings++
        }
        currentSum = newSum
        index++
    }
    return increasings
}

// with the same idea as solution 2, we don't really need the mask. we could just compare the numbers on the edge of
// the mask.
private fun numberOfMaskedIncreasings3(numbers: List<Int>, maskSize: Int): Int {
    val maskStartings = numbers.dropLast(maskSize)
    val maskEndings = numbers.drop(maskSize)
    return maskStartings.zip(maskEndings).count { it.second > it.first }
}


private fun part1(input: String): Int =
    numberOfIncreases(parseInput(input))

private fun parseInput(input: String) = input.lines().map { it.toInt() }

private fun numberOfIncreases(numbers: List<Int>): Int =
    numberOfMaskedIncreasings(numbers, 1)
