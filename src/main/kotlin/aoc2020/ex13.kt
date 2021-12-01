import kotlin.math.max

fun main() {
    //part1()
    part2()
}

private fun part2() {
    val input = "7,13,x,x,59,x,31,19"
//    require(findFirstTimeFor("17,x,13,19") == 3417L)
//    require(findFirstTimeFor("7,13,x,x,59,x,31,19") == 1068781L)
//    require(findFirstTimeFor("67,7,59,61") == 754018L)
//    require(findFirstTimeFor("67,x,7,59,61") == 779210L)
//    require(findFirstTimeFor("67,7,x,59,61") == 1261476L)
//    require(findFirstTimeFor("1789,37,47,1889") == 1202161486L)
    print(findFirstTimeFor("17,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,937,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,13,x,x,x,x,23,x,x,x,x,x,29,x,397,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,x,x,19"))
}

private fun findFirstTimeFor(input: String): Long {
    val busses = input
        .split(",")
        //.filter { it != "x" }
        .map {
            if (it == "x") -1 else it.toLong()
        }
    val maxBus = busses.maxOrNull()!!

    val nextMax =  busses.maxOfOrNull {
        if (it == maxBus) -1 else it
    } ?: maxBus

    val maxPlace = busses.indexOf(maxBus)
    val nextMaxPlace = busses.indexOf(nextMax)
    val (firstOption, distanceBetweenCanditades) =
        if(busses.count { it != -1L } == 2 ) {
            findFirstOption(maxBus, maxBus, maxPlace, maxPlace)
        } else {
            findFirstOption(maxBus, nextMax, maxPlace, nextMaxPlace)
        }

    println(maxBus)
    println(firstOption)

    var current = firstOption
    var iter = 0L
    val maxIter = 100_000_000_000L
    val printIter = max(1L, maxIter / 10_000L)

    while (iter < maxIter) {
        iter++
        if(iter % printIter == 0L
            || current / 100L == 10687L
        ) {
            println("iteration $iter. candidate is $current")
        }

        if (checkNumber(current, busses)) {
            print("found $current!")
            return current
        }
        current += distanceBetweenCanditades
    }
    return -1
}

fun findFirstOption(max: Long, nextMax: Long, maxPlace: Int, nextMaxPlace: Int): Pair<Long, Long> {
    if (max == nextMax) {
        return max - maxPlace.toLong() to max
    }
    //println("next max $nextMax")

    val lcm = lcm(nextMax, max)
    val constructedBussed = List<String>(max(maxPlace, nextMaxPlace)+1) {
        print(it)
        when (it) {
            maxPlace -> max.toInt().toString()
            nextMaxPlace -> nextMax.toInt().toString()
            else -> "x"
        }.also { print(": $it\n") }
    }.joinToString(",")
    println(constructedBussed)
    val fistTime = findFirstTimeFor(constructedBussed)
    return fistTime to lcm(max, nextMax)

}

fun checkNumber(number: Long, busses: List<Long>): Boolean {
    busses.forEachIndexed { index, bus: Long ->
        if (bus == -1L) return@forEachIndexed
        if ((number + index.toLong()) % bus != 0L) return false
    }
    return true
}

private fun part1() {


    // from actual file
    val input = """
        1007268
        17,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,937,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,13,x,x,x,x,23,x,x,x,x,x,29,x,397,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,x,x,19
    """.trimIndent()

//    val input = """
//        939
//        7,13,x,x,59,x,31,19
//    """.trimIndent()
//    val input = """
//        24
//        2,3,5,6,7,11,13
//    """.trimIndent()
    val lines = input.lines()
    val timeGettingToBusStation = lines[0].toInt()
    val busses = lines[1]
        .split(",")
        .filter { it != "x" }
        .map {
            it.toInt()
        }
    println(busses)
    val busAndNextTimeForEachBus = busses.associateWith { busInterval ->
        timeGettingToBusStation.ceilToProductOf(busInterval)
    }
    val nextBus = busAndNextTimeForEachBus.minByOrNull { it.value }!!
    println("next bus $nextBus")
    val waitTime = nextBus.value - timeGettingToBusStation
    println(waitTime * nextBus.key)
}

fun Int.ceilToProductOf(interval: Int) = (this + interval - 1).floorToProductOf(interval)
fun Int.floorToProductOf(interval: Int) = this - Math.floorMod(this, interval)

fun lcm(n1: Long, n2: Long): Long {
    var lcm: Long = if (n1 > n2) n1 else n2

    while (true) {
        if (lcm % n1 == 0L && lcm % n2 == 0L) {
            return lcm
        }
        lcm++
    }
}