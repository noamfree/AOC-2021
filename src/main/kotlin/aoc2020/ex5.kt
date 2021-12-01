fun main() {
    val input = readInputFile("aoc2020/input5")
    val rowsAndColums = input.lines().map { row(it.take(7)) to column(it.takeLast(3)) }.sortedBy { (a, b) ->
        seatId(a, b)
    }
    val firstRow = rowsAndColums.first().first
    val lastRow = rowsAndColums.last().first
    var index = 0
    print(rowsAndColums)
    for (i in firstRow..lastRow) {
        for (j in 0 ..7) {
            if (rowsAndColums[index] == i to j) {
                println("$i, $j, exists")
                index++
            } else println("$i, $j, not exists")
                //index++

        }
    }


}

fun row(string: String): Int {
    require(string.length == 7)
    return parseBinary(string, zero = 'F', one = 'B')
}

fun column(string: String): Int {
    require(string.length == 3)
    return parseBinary(string, zero = 'L', one = 'R')
}

fun parseBinary(string: String, zero: Char, one: Char): Int {
    val binaryString = string.map { char ->
        when (char) {
            zero -> '0'
            one -> '1'
            else -> error("cant")
        }
    }.joinToString("")
    return Integer.parseInt(binaryString, 2)
}

fun getSeatId(string: String): Int {
    val row = row(string.take(7))
    val column = column(string.takeLast(3))
    return seatId(row = row, column = column)
}

fun seatId(row: Int, column: Int) = row * 8 + column

//        100
//1000100 -> bfffbffrll
