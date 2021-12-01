
fun main() {
    val input = readInputFile("aoc2020/input9")
    val data = input.lines().map { it.toLong() }
    val invalid = findFirstInvalid(data, 25)
    val (low, high) = findSummingRangeOf(invalid, data)
    val summands = data.subList(low, high+1)
    println(summands)
    println("${summands.minOrNull()}, ${summands.maxOrNull()}")
    println(summands.minOrNull()!! + summands.maxOrNull()!!)
}

fun findSummingRangeOf(invalid: Long, data: List<Long>): Pair<Int, Int> {
    // TODO: 23/11/2021 seems like first time that performance matters!!
    var start = 0
    var end = 0
    var sum = 0L
    while (start <= data.lastIndex) {
        sum += data[end]
        if (sum == invalid) return start to end
        if (sum > invalid) {
            start++
            end = start
            sum = 0L
        } else { // sum < invalid
            end++
        }
    }
    return -1 to -1
}

private fun findFirstInvalid(numbers: List<Long>, preSize: Int): Long {
    require(numbers.take(preSize).toSet().size == preSize)
    numbers.drop(preSize).forEachIndexed { index, number ->
        val options = numbers.subList(index, index + preSize)
        require(options.size == preSize)  {"${options.size}"}
        //println("$options")
        if(!isSumOf(number, options)) return number
    }
    return -1L
}

private fun isSumOf(number: Long, options: List<Long>): Boolean {
    options.forEach { option ->
        if (number - option in options && number - option != option) {
            //println("$number=$option+${number-option}")
            return true
        }
    }
    println("$number invalid")
    return false
}