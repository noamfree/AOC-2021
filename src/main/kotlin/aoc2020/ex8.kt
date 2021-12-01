fun main() {
    val input = readInputFile("aoc2020/input8")
    val instructions = parseInput(input.lines())
    //for (i in instructions.indices) {
        println("$327")
        val hackedInstrctions = hackInstruction(327, instructions)
        val lines = mutableListOf<Int>()
        val computer = Computer()
        while (computer.currentLine !in lines) {
            lines.add(computer.currentLine)
            computer.operateInstruction(hackedInstrctions[computer.currentLine])
            println("acc ${computer.accumulator}")
        }

}

fun hackInstruction(i: Int, instructions: List<Instruction>): List<Instruction> {
    val instruction = instructions[i]
    return when (instruction) {
        is Instruction.Accumulate -> instructions
        is Instruction.Jump -> instructions.toMutableList().apply { set(i, Instruction.NoOp(instruction.lines)) }
        is Instruction.NoOp -> instructions.toMutableList().apply { set(i, Instruction.Jump(instruction.amount)) }
    }
}

class Computer {
    var currentLine = 0
    var accumulator = 0

    fun operateInstruction(instruction: Instruction) {
        when (instruction) {
            is Instruction.Accumulate -> {
                accumulator += instruction.amount
                currentLine++
            }
            is Instruction.Jump -> currentLine += instruction.lines
            is Instruction.NoOp -> currentLine++
        }
    }
}


sealed class Instruction {
    data class NoOp(val amount: Int) : Instruction()
    data class Accumulate(val amount: Int) : Instruction()
    data class Jump(val lines: Int) : Instruction()
}

private fun parseInput(lines: List<String>): List<Instruction> {
    return lines.map {
        val splitted = it.split(" ")
        require(splitted.size == 2)
        val amount = splitted[1].toInt()
        when (splitted[0]) {
            "nop" -> Instruction.NoOp(amount)
            "acc" -> Instruction.Accumulate(amount)
            "jmp" -> Instruction.Jump(amount)
            else -> error("cant")
        }
    }

}