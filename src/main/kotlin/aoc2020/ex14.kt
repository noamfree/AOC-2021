fun main() {
    tests()
    val input = readInputFile("aoc2020/input14")
    require(part1(input) == 13727901897109L)
    val memory = executeCommands2(parseInput(input))
    println(memory.values.sumOf { it })
}

private fun part1(input: String): Long {
    val commands = parseInput(input)
    val memory = executeCommands1(commands)
    return memory.values.sumOf { it }
}

private class MaskCommand2(val mask: String, val address: Int, val value: Long)

private fun executeCommands2(commands: List<MaskCommand>): MutableMap<Long, Long> {
    require(commands.first() is MaskCommand.Mask)
    val memory = mutableMapOf<Long, Long>()
    var mask = "0"
    val commands2 = mutableListOf<MaskCommand2>()
    commands.forEach {
        when (it) {
            is MaskCommand.Mask -> mask = it.mask
            is MaskCommand.Store -> commands2.add(MaskCommand2(mask, it.address, it.value))
        }
    }

    commands2.forEachIndexed { index, command ->
        println("executing command $index")
        val places = command.address.places(command.mask)
        places.forEach { place ->
            memory[place] = command.value
        }
    }
    return memory
}

private fun executeCommands1(commands: List<MaskCommand>): Map<Int, Long> {
    require(commands.first() is MaskCommand.Mask)
    val memory = mutableMapOf<Int, Long>()
    var mask = "0"
    commands.forEach {
        when (it) {
            is MaskCommand.Mask -> mask = it.mask
            is MaskCommand.Store -> memory[it.address] = it.value.masked(mask)
        }
    }
    return memory
}

private fun parseInput(input: String): List<MaskCommand> {
    val commands = input.lines().map {
        val s = it.split(" = ")
        require(s.size == 2)
        if (s[0] == "mask") {
            require(s[1].length == 36)
            MaskCommand.Mask(s[1])
        } else if (s[0].startsWith("mem")) {
            MaskCommand.Store(s[0].drop(4).dropLast(1).toInt(), s[1].toLong())
        } else {
            error("cant")
        }
    }
    require(commands.first() is MaskCommand.Mask)
    return commands
}

private sealed class MaskCommand {
    class Mask(val mask: String) : MaskCommand()
    class Store(val address: Int, val value: Long) : MaskCommand()
}

private fun tests() {
    val mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"
    require(11L.masked(mask) == 73L)
    require(101L.masked(mask) == 101L)
    require(0L.masked(mask) == 64L)
    val input = """
        mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
        mem[8] = 11
        mem[7] = 101
        mem[8] = 0
    """.trimIndent()
    require(part1(input) == 165L)
    require(
        42.masked2("000000000000000000000000000000X1001X") ==
                "000000000000000000000000000000X1101X"
    )
    require(
        26.masked2("00000000000000000000000000000000X0XX") ==
                "00000000000000000000000000000001X0XX"
    )
    require(
        42.places("000000000000000000000000000000X1001X") ==
                listOf(26L, 27L, 58L, 59L)
    )

    require(
        26.places("00000000000000000000000000000000X0XX") ==
                listOf(16L, 17L, 18L, 19L, 24L, 25L, 26L, 27L)
    )
}

private fun Long.masked(mask: String): Long {
    val binaryValue = this.toString(2).padStart(36, '0')
    return mask.mapIndexed { index, char ->
        when (char) {
            'X' -> binaryValue[index]
            '0' -> 0L
            '1' -> 1L
            else -> error("cant")
        }
    }.joinToString("").toLong(2)
}

private fun Int.masked2(mask: String): String {
    val binaryValue = this.toString(2).padStart(36, '0')
    return mask.mapIndexed { index, char ->
        when (char) {
            'X' -> 'X'
            '0' -> binaryValue[index]
            '1' -> '1'
            else -> error("cant")
        }
    }.joinToString("")
}

private fun Int.places(mask: String): List<Long> { // todo consider sequence, or search
    val masked = masked2(mask)
    return places(masked.toList())
}

val globalAddressMaskCache = mutableMapOf<List<Char>, List<Long>>()


private fun places(string: List<Char>): List<Long> {
    val indexOfFirst = string.indexOfFirst { it == 'X' }
    if (indexOfFirst == -1) return listOf(string.joinToString("").toLong(2))
    if (globalAddressMaskCache.contains(string)) return globalAddressMaskCache.getValue(string)
    val res = places(string.toMutableList().apply { set(indexOfFirst, '0') }) +
            places(string.toMutableList().apply { set(indexOfFirst, '1') })
    globalAddressMaskCache.put(string, res)
    return res
}