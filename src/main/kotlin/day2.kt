private val testInput1 = """
    forward 5
    down 5
    forward 8
    up 3
    down 8
    forward 2
""".trimIndent()

private fun tests() {
    assertEquals(part1(parseInput(testInput1)), 150)
}

private fun main() {
    tests()

    val input = readInputFile("input2")
    val commands = parseInput(input)
    println(part1(commands))

    println("day2")
}

private fun part1(commands: List<SubmarineCommand>): Int {
    val initialPosition = Submarine(0, 0)
    val finalPosition = commands.fold(initialPosition) { acc, submarineCommand ->
        acc.executeCommand(submarineCommand)
    }
    return finalPosition.x * finalPosition.y
}

sealed interface SubmarineCommand
data class Down(val amount: Int) : SubmarineCommand
data class Up(val amount: Int) : SubmarineCommand
data class Forward(val amount: Int) : SubmarineCommand

private data class Submarine(val x: Int, val y: Int) // todo should we work with longs?

private fun Submarine.executeCommand(command: SubmarineCommand) = when (command) {
    is Down -> copy(y = y + command.amount)
    is Forward -> copy(x = x + command.amount)
    is Up -> copy(y = y - command.amount)
}

private fun parseInput(string: String): List<SubmarineCommand> =
    string.lines().map { parseCommand(it) }

private fun parseCommand(string: String): SubmarineCommand {
    val s = string.split(" ")
    require(s.size == 2)
    val command = s[0]
    val amount = s[1].toInt()
    return when (command) {
        F -> Forward(amount)
        D -> Down(amount)
        U -> Up(amount)
        else -> error("cant $command")
    }
}

private const val F = "forward"
private const val D = "down"
private const val U = "up"
