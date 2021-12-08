import kotlin.math.abs
import kotlin.math.max

private val testInput1 = """
    0,9 -> 5,9
    8,0 -> 0,8
    9,4 -> 3,4
    2,2 -> 2,1
    7,0 -> 7,4
    6,4 -> 2,0
    0,9 -> 2,9
    3,4 -> 1,4
    0,0 -> 8,8
    5,5 -> 8,2
""".trimIndent()

fun main() {
   // val input = testInput1
    val input = readInputFile("input5")
    val lines = parseInput(input)
    val axisAlignedLines = lines.filter {
        it.x1 == it.x2 || it.y1 == it.y2
    }
    val width = lines.maxOf { max(it.x1, it.x2) }
    val height = lines.maxOf { max(it.y1, it.y2) }
    val sensors: Map<Pair<Int, Int>, MutableList<Line>> = mutableMapOf<Pair<Int, Int>, MutableList<Line>>().apply {
        (0..height).forEach { r ->
            (0..width).forEach { c ->
                put(r to c, mutableListOf())
            }
        }
    }
    lines.forEach { line ->
        line.points().forEach { point -> sensors[point]!!.add(line) }
    }

//    (0..height).forEach { r ->
//        (0..width).forEach { c ->
//            val size = sensors[r to c]!!.size
//            print(if (size == 0) '.' else size.toString())
//        }
//        print("\n")
//    }
    println(sensors.count { it.value.size > 1 })

    println("day5")
}

private fun Line.points(): List<Pair<Int, Int>> {
    return if (x1 == x2) range(y1, y2).map { it to x1 }
    else if (y1 == y2) range(x1, x2).map { y1 to it }
    else range(y1, y2).zip(range(x1, x2)).also {
        println(it)
    }
}
private fun range(a: Int, b: Int) = if (a < b) a..b else a downTo  b

private fun parseInput(string: String): List<Line> {
    return string.lines().map {
        val (x1, y1, x2, y2) = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)").find(it)!!.destructured
        Line(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt())
    }
}

private data class Line(
    val x1: Int, val y1: Int,
    val x2: Int, val y2: Int
) {
    init {
        require(x1 == x2 ||
        y1 == y2 ||
        abs(x1 - x2) == abs(y1 - y2))
    }
    override fun toString(): String = "$x1,$y1 -> $x2,$y2"

}
