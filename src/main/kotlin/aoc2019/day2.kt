//package aoc2019
//
//import assertEquals
//import readInputFile
//
//fun main() {
//    tests()
//    val input = readInputFile("aoc2019/day2")
////    val input = """
////        1,9,10,3,2,3,11,0,99,30,40,50
////    """.trimIndent()
//    val numbers = input.split(',').map { it.toLong() }
//    val afterCalculation = runComputerOn(numbers)
//    println(afterCalculation)
//    println(afterCalculation[0])
//    println("day2")
//}
//
//private fun tests() {
//    assertEquals(
//        runComputerOn(
//            listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)),
//        listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)
//    )
//    assertEquals(
//        runComputerOn(
//            listOf(1,0,0,0,99)),
//        listOf(2,0,0,0,99)
//    )
//    assertEquals(
//        runComputerOn(
//            listOf(2,3,0,3,99)),
//        listOf(2,3,0,6,99)
//    )
//    assertEquals(
//        runComputerOn(
//            listOf(2,4,4,5,99,0)),
//        listOf(2,4,4,5,99,9801)
//    )
//    assertEquals(
//        runComputerOn(
//            listOf(1,1,1,4,99,5,6,0,99)),
//        listOf(30,1,1,4,2,5,6,0,99)
//    )
//}
//
//private fun runComputerOn(numbers: List<Long>): List<Int> {
//    val workingNumbers = numbers.toMutableList()
//    var position = 0
//    while (true) {
//        val op = workingNumbers[position]
//        when (op) {
//            ADD -> {
//                workingNumbers[workingNumbers[position + 3]] =
//                    workingNumbers[workingNumbers[position + 1]] + workingNumbers[workingNumbers[position + 2]]
//            }
//            MUL -> {
//                workingNumbers[workingNumbers[position + 3]] =
//                    workingNumbers[workingNumbers[position + 1]] * workingNumbers[workingNumbers[position + 2]]
//            }
//            TERMINATE -> break
//        }
//        position += 4
//    }
//    return workingNumbers
//}
//
//
//private const val ADD = 1
//private const val MUL = 2
//private const val TERMINATE = 99