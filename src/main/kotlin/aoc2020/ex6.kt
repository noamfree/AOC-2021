
fun main() {
    val input = readInputFile("aoc2020/input6")
    val groups = input.split("\n\n")
    println(groups.sumOf { everyoneYes(it).size })
}

fun everyoneYes(group: String): Set<Char> {
    return group.split("\n").map {
        it.toSet()
    }.fold(lettersForGroup(group)) { acc, set ->
        acc.intersect(set).toMutableSet()
    }

}

fun lettersForGroup(group: String): MutableSet<Char> {
    return group.toSet().toMutableSet().apply{remove('\n')}
}