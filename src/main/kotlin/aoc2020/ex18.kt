
fun main() {
    tests()
    val input = readInputFile("aoc2020/input18")
    print(input.lines().associateWith { calc(it) }.toList()
       // .sortedBy { it.second }
        .sumOf {
        println("${it.first} = ${it.second}")
        it.second
    })
}

private fun tests() {
    require(calc("1 + 2 * 3 + 4 * 5 + 6") == 71L)
    require(calc("2 * 3 + (4 * 5)") == 26L)
    require(calc("5 + (8 * 3 + 9 + 3 * 4 * 3)") == 437L)
    require(calc("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))") == 12240L)
    require(calc("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2") == 13632L)
    require(calc("2* (1+1+(1+1))+1") == 9L)
   // require(calc("4 * (4 + 7 + 4 * (9 * 2 + 3 * 9 * 8 * 7)) * (7 * 6 * 8) + 7 + 4 * 6") == 1280240706L)
}
//(3 + (5 * 5 * 2 * 2 + 4) + 7) + (8 * 6 * 8 * (7 + 9 + 3 + 8 + 3)) + (8 * 7) * (8 * 3 + 3 + 2 * (8 * 7 + 8 + 5) + 3) * 8
// 114 + (8 * 6 * 8 * (7 + 9 + 3 + 8 + 3)) + (8 * 7) * (8 * 3 + 3 + 2 * (8 * 7 + 8 + 5) + 3) * 8
// 114 + (384 * (7+9+3+8+3))
// 114 + 11520 + 54 * (8 * 3 + 3 + 2 * (8 * 7 + 8 + 5) + 3) * 8
// 114 + 11520 + 54 * (8 * 3 + 3 + 2 * 69 + 3) * 8
// 114 + 11520 + 54 * 2004 * 8

//8 + 7 * ((4 * 5 * 6 + 4 + 9) * 5) + 6 * ((8 + 2 * 4 * 9 * 8) * 6 * (4 + 2 * 8 + 2) * 9 + 3 * 2)
// 15 * ((4 * 5 * 6 + 4 + 9) * 5) + 6 * ((8 + 2 * 4 * 9 * 8) * 6 * (4 + 2 * 8 + 2) * 9 + 3 * 2)
// 15 * (133 * 5) + 6 * ((8 + 2 * 4 * 9 * 8) * 6 * (4 + 2 * 8 + 2) * 9 + 3 * 2)
// 15 * (133 * 5) + 6 * (2880 * 6 * (4 + 2 * 8 + 2) * 9 + 3 * 2)
// 9975 + 6 * (2880 * 6 * (4 + 2 * 8 + 2) * 9 + 3 * 2)
// 9981 * (2880 * 6 * 50 * 9 + 3 * 2)
// 9981 * 15552006
private enum class MathOperation {
    ADD, MULTIPLY
}

private sealed class MathState {
    class AfterOperation(val operation: MathOperation): MathState()
    object AfterNumber: MathState()
    object Init: MathState()
}

private fun calc(string: String) = Calculator().calculate(string, 0).first

private class Calculator {
    var state: MathState = MathState.Init
    var result = 0L
    var currentIndex = 0
    var string = ""
    var ended = false

    fun calculate(string: String, index: Int): Pair<Long, Int> {
        this.string = string
        currentIndex = index
        while (currentIndex < string.length && !ended) {
            handleChar(string[currentIndex])
            currentIndex++
        }
        require(state is MathState.AfterNumber)
        return result to currentIndex
    }

    private fun handleChar(char: Char) {
        when (char) {
            '(' -> {
                val innerResult = Calculator().calculate(string, currentIndex + 1)
                aggregateWithNumber(innerResult.first)
                currentIndex = innerResult.second-1
            }
            ')' -> {
                require(state == MathState.AfterNumber)
                ended = true
            }
            ' ' -> Unit
            '+' -> {
                require(state is MathState.AfterNumber)
                state = MathState.AfterOperation(MathOperation.ADD)
            }
            '*' -> {
                require(state is MathState.AfterNumber)
                state = MathState.AfterOperation(MathOperation.MULTIPLY)
            }
            else -> {
                val number = char.toString().toLong()
                aggregateWithNumber(number)
            }
        }
    }

    private fun aggregateWithNumber(number: Long) {
        result = when (val state = state) {
            is MathState.AfterOperation -> operate(result, state.operation, number)
            is MathState.Init -> number
            else -> error("currentIndex: $currentIndex, state $state")
        }
        state = MathState.AfterNumber
    }
}

private fun operate(current: Long, operation: MathOperation, new: Long): Long {
    return when (operation) {
        MathOperation.ADD -> current + new
        MathOperation.MULTIPLY -> current * new
    }
}