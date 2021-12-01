package aoc2019

import assertEquals
import readInputFile
import kotlin.math.max

fun main() {
    tests()
    val input = readInputFile("aoc2019/day1")
    println(input.lines().sumOf { fuelPerModule(it.toLong()) })
    println(input.lines().sumOf { totalFuelPerModule(it.toLong()) })
    print("hi")
}

private fun tests() {
    assertEquals(fuelPerModule(12), 2)
    assertEquals(fuelPerModule(14), 2)
    assertEquals(fuelPerModule(1969), 654)
    assertEquals(fuelPerModule(100756), 33583)


    assertEquals(totalFuelPerModule(14), 2)
    assertEquals(totalFuelPerModule(1969), 966)
    assertEquals(totalFuelPerModule(100756), 50346)
}


private fun totalFuelPerModule(mass: Long): Long {
    var currentFuel = fuelPerModule(mass)
    var totalFuel = currentFuel
    while (currentFuel > 0) {
       currentFuel = fuelPerModule(currentFuel)
       totalFuel += currentFuel
    }
    return totalFuel
}


private fun fuelPerModule(mass: Long) = max(mass / 3L - 2L, 0L)