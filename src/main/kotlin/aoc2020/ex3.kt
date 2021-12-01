fun main() {
    val input = readInputFile("aoc2020/input3")
    var mul = 1L
    mul *= countForSlope(input = input, slopeX = 3, slopeY = 1).toLong()
    mul *= countForSlope(input = input, slopeX = 1, slopeY = 1).toLong()
    mul *= countForSlope(input = input, slopeX = 5, slopeY = 1).toLong()
    mul *= countForSlope(input = input, slopeX = 7, slopeY = 1).toLong()
    mul *= countForSlope(input = input, slopeX = 1, slopeY = 2).toLong()
    print(mul)
}

private fun countForSlope(input: String, slopeX: Int, slopeY: Int): Int {
    var x = 0
    var y = 0
    val lines = input.lines()
    var treeCount = 0
    val lineLength = lines[0].length
    require(lines[0][0] == NON_TREE)
    while (y < lines.size - 1) {
        x = (x + slopeX) % lineLength
        y += slopeY
        if (lines[y][x] == TREE) {
            treeCount++
        }
    }
    return treeCount
}

private const val TREE = '#'
private const val NON_TREE = '.'