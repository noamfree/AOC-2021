import java.util.*

fun main() {
    val input = readInputFile("aoc2020/input10")
    val data = input.lines().map { it.toInt() }
    val sorted = data.sorted()
    val withActual = listOf(0) + sorted + (sorted.last() + 3)
    require(withActual.size == withActual.toSet().size) { "size: $withActual set:${withActual.toSet()}" }
    println(withActual)
    val diffs = withActual.zipWithNext { a, b -> b - a }
    val sortedDiffs = diffs.sorted()
    require(sortedDiffs.first() >= 1)
    require(sortedDiffs.last() <= 3)
    println(sortedDiffs)
    val ones = diffs.count { it == 1 }
    val threes = diffs.count { it == 3 }
    println("$ones, $threes")
    println(ones * threes)
    print(findOptions(withActual.first(), withActual.drop(1), withActual.last()))

}

val cache: MutableMap<Pair<Int, List<Int>>, Long> = mutableMapOf()

fun findOptions(start: Int, adapters: List<Int>, end: Int): Long {
    println("start: $start, options: $adapters")
    require(adapters.sorted() == adapters)
    require(adapters.isEmpty() || adapters.first() != 0)
//    require(adapters.last() == adapters[adapters.lastIndex - 1] + 3)
    if (cache.contains(start to adapters)) {
        println("from cache ${cache.getValue(start to adapters)}")
        return cache.getValue(start to adapters)
    }
    if (adapters.isEmpty()&&end==start) return 1L.also{
        println("calculated: start:$start, options: $adapters ans: 1")
    }
    if (adapters.isEmpty()||adapters.first() - start > 3) return 0L.also{
        println("calculated: start:$start, options: $adapters ans: 0")
    }
    val ans = findOptions(start, adapters.drop(1), end) + findOptions(adapters.first(), adapters.drop(1), end)
    cache[start to adapters] = ans
    return ans.also {
        println("calculated: start:$start, options: $adapters ans: $ans")
    }
}

fun findOptions2(adapters: List<Int>): Int {
    require(adapters.sorted() == adapters)
    require(adapters.first() == 0)
    require(adapters.last() == adapters[adapters.lastIndex - 1] + 3)


    val used = Stack<Pair<Int, Int>>()

    used.push(adapters.first() to 0)
    val indexedAdapters = adapters.mapIndexed { index, i -> i to index }
    val remainingOptions = indexedAdapters.toMutableList().apply { removeAt(0) }
    var count = 0

    fun backtrack() {
        used.pop()
        val last = used.pop()
        remainingOptions.addAll(indexedAdapters.subList(last.second+1, indexedAdapters.size))
    }

    var iter = 0
    while (true && iter < 1000000) {
        iter++
        //println("stack $used")
        //println("remaining $remainingOptions")
        //println("count $count")

        if (remainingOptions.isEmpty()) {
            println("found combination ${used.map { it.first }}")
            count++
            backtrack()
            continue
        }

        if(used.isEmpty()) return count
        val lastAdapter = used.peek()
        if (remainingOptions.first().first - lastAdapter.first <= 3) {
            used.push(remainingOptions.first())
            remainingOptions.apply { removeAt(0) }
        } else {
            used.push(remainingOptions.first())
            remainingOptions.apply { clear() }
            backtrack()
        }
    }
    return count
}

/***
 * push
 * if not valid pop2
 * if is empty. validate, increment
 * pop2 restore remaining from last pop from original array. if stack is empty, return
 * remove first option
 *
 * 12345
 * []
 *
 * 2345 - push
 * 1
 *
 * 345
 * 12
 *
 * 45
 * 123
 *
 * 5
 * 1234 -push
 *
 * []
 * 12345 - is empty. validate, increment/or not
 *
 * 45
 * 123 - pop2 restore remaining from last pop from original array
 *
 * 5
 * 123 - remove first option
 *
 * 1235
 *
 * XXX
 * 35
 * 12
 *
 * 345
 * 12
 *
 * 45
 * 12
 *
 * 5
 * 124
 *
 * 1245
 *
 * 45
 * 12
 *
 * 5
 * 12
 *
 * 125
 *
 * 2345
 * 1
 *
 * 345
 * 1
 *
 * 45
 * 13
 *
 * 5
 * 134
 *
 * 1345
 *
 * 45
 * 13
 *
 * 5
 * 13
 *
 * 135
 *
 * 345
 * 1
 *
 * 45
 * 1
 *
 * 5
 * 14
 *
 * 145
 *
 * 45
 * 1
 *
 * 5
 * 1
 *
 * 15 - X . is empty validate, dont incement
 *
 * 12345 . pop 2 as usual. if stack is empty, return
 * 0
 *
 * 2345
 * 0
 *
 */