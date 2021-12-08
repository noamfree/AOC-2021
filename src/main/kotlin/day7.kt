import java.lang.Math.pow
import kotlin.math.abs

private val testInput1 = """
    16,1,2,0,4,2,7,1,2,14
""".trimIndent()

private fun tests() {
    assertEquals(parseInput(testInput1).fuelToMoveTo(2), 37)
    assertEquals(parseInput(testInput1).fuelToMoveTo(1), 41)
    assertEquals(parseInput(testInput1).fuelToMoveTo(3), 39)
    assertEquals(parseInput(testInput1).fuelToMoveTo(10), 71)
}


fun main() {
    tests()

    val input = readInputFile("input7")
    //val input = testInput1
    val crabs = parseInput(input)
    println(crabs)
    println("min: ${crabs.minOrNull()}, max: ${crabs.maxOrNull()}")
    val leastLine = crabs.leastLine()
    println(leastLine)
    println(crabs.fuelToMoveTo(leastLine))

    println("\npart2")
    val leastLine2 = crabs.leastLine2()
    println(leastLine2)
    println(crabs.fuelToMoveTo2(leastLine2))

    println("day7")
}

private fun List<Int>.leastLine(): Int {
    val max = maxOrNull()!!
    return (0..max).minByOrNull {
        fuelToMoveTo(it)
    }!!
}

private fun List<Int>.leastLine2(): Int {
    val max = maxOrNull()!!
    return (0..max).minByOrNull {
        fuelToMoveTo2(it)
    }!!
}

private fun parseInput(input: String) = input.split(",").map { it.toInt() }

private fun List<Int>.fuelToMoveTo2(newPosition: Int): Int {
    return this.sumOf {
        val diff = abs(it - newPosition)
        diff * (diff + 1) / 2
    }
}

private fun List<Int>.fuelToMoveTo(newPosition: Int): Int {
    return this.sumOf { abs(it - newPosition) }
}