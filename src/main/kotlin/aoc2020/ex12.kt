import kotlin.math.abs

fun main() {
    val input = readInputFile("aoc2020/input12")
//    val input = """
//        F10
//        N3
//        F7
//        R90
//        F11
//    """.trimIndent()
    val sea = Sea(Ship(pos = Vector.ZERO, faceDir = Dir.E), Vector(s = -1, w = -10))
    val lines = input.lines()
    val endSea = sea.transformIndexed(lines.size) { i ->
        val line = lines[i]
        operateInstruction(line)
            .also { println(it) }
    }
    println(endSea)
    println(endSea.ship.pos.l1())
}

private fun Sea.operateInstruction(line: String): Sea {
    val command = SeaCommand.fromChar(line[0])
    val amount = line.drop(1).toInt()

    //return copy(ship = ship.operateCommand(Command.fromChar(command), amount))
    return operateCommand(command, amount)
}

private fun Sea.operateCommand(command: SeaCommand, amount: Int) : Sea {
    return when (command) {
        SeaCommand.N -> copy(waypoint = waypoint + Dir.N.v *  amount)
        SeaCommand.W -> copy(waypoint = waypoint + Dir.W.v *  amount)
        SeaCommand.S -> copy(waypoint = waypoint + Dir.S.v *  amount)
        SeaCommand.E -> copy(waypoint = waypoint + Dir.E.v *  amount)
        SeaCommand.L -> copy(waypoint = waypoint.rotate(-amount))
        SeaCommand.R -> copy(waypoint = waypoint.rotate(amount))
        SeaCommand.F -> moveShipForward(amount)
    }
}

private fun Sea.moveShipForward(amount: Int): Sea {
    val dir = waypoint
    val movement = dir * amount
    return copy(ship = ship.move(movement), waypoint = waypoint)
}


private fun Ship.operateCommand(command: SeaCommand, amount: Int): Ship {
    return when (command) {
        SeaCommand.N -> move(Dir.N, amount)
        SeaCommand.W -> move(Dir.W, amount)
        SeaCommand.S -> move(Dir.S, amount)
        SeaCommand.E -> move(Dir.E, amount)
        SeaCommand.L -> rotate(-amount)
        SeaCommand.R -> rotate(amount)
        SeaCommand.F -> forward(amount)
    }
}

data class Sea(val ship: Ship, val waypoint: Vector)
//fun Sea.operateCommand(command: Command, amount: Int) {
//    return when (command) {
//        Command.N -> this.copy(ship.operateCommand(move(Dir.N, amount)
//        Command.W -> move(Dir.W, amount)
//        Command.S -> move(Dir.S, amount)
//        Command.E -> move(Dir.E, amount)
//        Command.L -> rotate(-amount)
//        Command.R -> rotate(amount)
//        Command.F -> forward(amount)
//    }
//}

data class Ship(val pos: Vector, val faceDir: Vector) {

    constructor(pos: Vector, faceDir: Dir): this(pos, faceDir.v)
    init {
        require(faceDir.isDir()) { "$faceDir is not unit"}
    }
    fun move(dir: Dir, amount: Int) = move(dir.v * amount)
    fun move(d: Vector) = copy(pos = pos + d)
    fun rotate(deg: Int) = copy(faceDir = faceDir.rotate(deg))
    fun forward(units: Int) = copy(pos = pos + faceDir * units)
}

data class Vector(val s: Int, val w: Int) {
    operator fun plus(v: Vector) = copy(s = s + v.s, w = w + v.w)
    operator fun minus(v: Vector) = copy(s = s - v.s, w = w - v.w)
    operator fun times(i: Int) = copy(s = s * i, w = w * i)
    operator fun div(i: Int) = copy(s = s / i, w = w / i)

    fun isDir() = l1() == 1
    fun l1() = abs(w) + abs(s)

    private fun rotate90(): Vector = copy(s = -w, w = s)
    private fun rotate270() = transform(3) { rotate90() }

    fun rotate(deg: Int): Vector {
        require(deg % 90 == 0) { "$deg is not mod 90" }
        val rotations = deg / 90
        return if (rotations >= 0) {
            transform(rotations) { rotate90() }
        } else {
            transform(-rotations) { rotate270() }
        }
    }

    companion object {
        val ZERO = Vector(0, 0)
    }
}

enum class Dir(val v: Vector) {
    S(Vector(1, 0)),
    N(Vector(-1, 0)),
    W(Vector(0, 1)),
    E(Vector(0, -1));
}


private enum class SeaCommand {
    N, W, S, E, L, R, F;

    companion object {
        fun fromChar(char: Char): SeaCommand  = when (char) {
            'N' -> N
            'W' -> W
            'S' -> S
            'E' -> E
            'L' -> L
            'R' -> R
            'F' -> F
            else -> error("cant")
        }
    }
}
