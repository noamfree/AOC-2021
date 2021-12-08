private val testInput1 =
    """
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
""".trimIndent()


fun main() {
    //val input = testInput1
    val input = readInputFile("input4")
    val s = input.splitByEmptyLine()
    val numbers = s[0].split(",").map { it.toInt() }
    val boardsString = s.drop(1)


    println("part1: ${part1(boardsString, numbers)}")
    println("part2: ${part2(boardsString, numbers)}")


    println("day4")
}

private fun part2(boardsString: List<String>, numbers: List<Int>): Int {
    val boards = boardsString.map { parseBoard(it.lines()) }
    var index = -1
    while (boards.count { !it.isWinning() } > 1) {
        index++
        val number = numbers[index]
        playRound(boards, number)
    }
    val loosingBoardIndex = boards.indexOfFirst { !it.isWinning() }
    val loosingBoard = boards[loosingBoardIndex]
    while (!loosingBoard.isWinning()) {
        index++
        val number = numbers[index]
        loosingBoard.mark(number)
    }
    return loosingBoard.sumUnmarked() * numbers[index]
}

private fun part1(boardsString: List<String>, numbers: List<Int>): Int {
    val boards = boardsString.map { parseBoard(it.lines()) }
    var index = -1
    while (!boards.any { it.isWinning() }) {
        index++
        val number = numbers[index]
        playRound(boards, number)
    }
    val winningBoardIndex = boards.indexOfFirst { it.isWinning() }
    val winningBoard = boards[winningBoardIndex]
    println(winningBoard.sumUnmarked())

    println(numbers[index])
    return winningBoard.sumUnmarked() * numbers[index]
}


private fun playRound(boards: List<BingoBoard>, number: Int) {
    boards.forEach { board -> board.mark(number) }
}

private fun parseBoard(string: List<String>): BingoBoard {
    return BingoBoard(
        string.map { row ->
            row.removePrefix(" ").split(Regex(" +"))

                .map {
                    it.toInt()
                }
        })
}

private class BingoBoard(val numbers: List<List<Int>>) {
    init {
        // no duplicates
        require(numbers.flatten().size == numbers.flatten().toSet().size)
    }

    private val height = numbers.size
    private val width = numbers.first().size

    private val marked = mutableMapOf<Pair<Int, Int>, Boolean>().apply {
        (0 until height).forEach { row ->
            (0 until width).forEach { col ->
                put(row to col, false)
            }
        }
    }

    override fun toString(): String {
        return numbers.mapIndexed { r, row ->
            row.mapIndexed { c, col -> if (marked[r to c] == true) "X" else col.toString() }.joinToString(" ")
        }.joinToString("\n")
    }

    fun mark(number: Int) {
        val index = indexOf(number) ?: return
        marked[index] = true
    }

    private fun indexOf(number: Int): Pair<Int, Int>? {
        val row = numbers.indexOfFirst { number in it }
        if (row == -1) return null
        val col = numbers[row].indexOf(number)
        if (col == -1) return null
        return row to col
    }

    fun isWinning(): Boolean {
        val winningRow = (0 until height).any { row ->
            (0 until width).map { row to it }.all { marked[it] == true }
        }
        if (winningRow) return true
        val winningCol = (0 until width).any { col ->
            (0 until height).map { it to col }.all { marked[it] == true }
        }
        return winningCol
    }

    fun sumUnmarked(): Int {
        var sum = 0
        (0 until height).forEach { row ->
            (0 until width).forEach { col ->
                if (!marked[row to col]!!) {
                    sum += numbers[row][col]
                }
            }
        }
        return sum
    }
}