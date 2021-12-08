package aoc2020

import readInputFile
import splitByEmptyLine
import java.util.*

private val testInput1 = """
    Player 1:
    9
    2
    6
    3
    1

    Player 2:
    5
    8
    4
    7
    10
""".trimIndent()

fun main() {
    val input = readInputFile("aoc2020/input22")
    //val input = testInput1
    println("part1 ${part1(input)}")
    val (deck1, deck2) = parseInput(input)
    val (end1, end2) = recursiveRaft(deck1, deck2)
    val deck1Score = end1.reversed().mapIndexed { index, i -> (index + 1) * i }.sum()
    val deck2Score = end2.reversed().mapIndexed { index, i -> (index + 1) * i }.sum()
    println(deck1Score + deck2Score)
    println("day22")
}

private val gameCache = mutableListOf(mutableMapOf<Pair<List<Int>, List<Int>>, Pair<List<Int>, List<Int>>>()).apply {
    repeat(13) { add(mutableMapOf())}
}

var game = 0

private fun recursiveRaft(deck1: Queue<Int>, deck2: Queue<Int>, depth: Int =0): Pair<List<Int>, List<Int>> {
    println("Depth $depth")
    println("Cache size ${gameCache[depth].size}")
    game++

    if(deck1.isEmpty() || deck2.isEmpty()) return deck1.toList() to deck2.toList()
    // todo break loops
    val localCache = mutableListOf<Pair<List<Int>, List<Int>>>()
    while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
        if(gameCache[depth].contains(deck1.toList() to deck2.toList())) {
            val res = gameCache[depth][deck1.toList() to deck2.toList()]!!
            localCache.forEach {
                gameCache[depth][it] = res
            }
            return res
        }
        if (deck1.toList() to deck2.toList() in localCache) {
            val res = deck1.toList() to emptyList<Int>()
            localCache.forEach {
                gameCache[depth][it] = res
            }
            return res
        }
        localCache.add(deck1.toList() to deck2.toList())

        val card1 = deck1.remove()
        val card2 = deck2.remove()
        if (card1 <= deck1.size && card2 <= deck2.size) {
            val result = recursiveRaft(LinkedList(deck1), LinkedList(deck2), depth+1)
            if (result.first.isNotEmpty()) {
                deck1.add(card1)
                deck1.add(card2)
            } else {
                deck2.add(card2)
                deck2.add(card1)
            }
        } else {
            if (card1 > card2) {
                deck1.add(card1)
                deck1.add(card2)
            } else if (card1 < card2) {
                deck2.add(card2)
                deck2.add(card1)
            } else error("1: $card2 2: $card2")
        }
    }
    val res = deck1.toList() to deck2.toList()
    localCache.forEach {
        gameCache[depth][it] = res
    }
    return res
}


private fun part1(input: String): Int {
    val (deck1, deck2) = parseInput(input)
    return playRaft(deck1, deck2)
}

private fun playRaft(deck1: Queue<Int>, deck2: Queue<Int>): Int {
    var round = 0
    while (deck1.isNotEmpty() && deck2.isNotEmpty()) {
        round++
        println("round $round")
        val card1 = deck1.remove()
        val card2 = deck2.remove()
        if (card1 > card2) {
            deck1.add(card1)
            deck1.add(card2)
        } else if (card1 < card2) {
            deck2.add(card2)
            deck2.add(card1)
        } else error("1: $card2 2: $card2")
    }

    val deck1Score = deck1.reversed().mapIndexed { index, i -> (index + 1) * i }.sum()
    val deck2Score = deck2.reversed().mapIndexed { index, i -> (index + 1) * i }.sum()
    return deck1Score + deck2Score
}

private fun parseInput(input: String): Pair<Queue<Int>, Queue<Int>> {
    val playersStrings = input.splitByEmptyLine()
    require(playersStrings.size == 2)

    val numbers = playersStrings.map {
        val lines = it.lines()
        require(lines[0].startsWith("Player"))
        lines.drop(1).map { it.toInt() }
    }
    val player1 = LinkedList(numbers[0])
    val player2 = LinkedList(numbers[1])
    return player1 to player2
}