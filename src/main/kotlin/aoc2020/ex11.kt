fun main() {
    assert1()
    assert2()

    val input = readInputFile("aoc2020/input11")

    occupiedOnStabilization(matFromInput(input)) { it.update() }
    occupiedOnStabilization(matFromInput(input)) { it.update3() }
}


fun Mat.update2() {
    val r = Mat(array.mapIndexed { rIndex, row ->
        row.mapIndexed { cIndex, _ ->
            hasNeighborAt(this, rIndex, cIndex, 0, -1, mutableMapOf()).let {
                if (it) PlaceState.OCCUPIED else PlaceState.SEAT
            }
        }
    })
    println("\n")
    println(r.repr())
    require(
        r == matFromInput(
            """
        L#####L###
        L#L###LLLL
        LLL#######
        L#L######L
        L#####LLLL
        L#####L###
        LLL#######
        L#L######L
        L##LL###LL
        L###L#####
    """.trimIndent()
        )
    )
}

data class PosAndDir(val x: Int, val y: Int, val dx: Int, val dy: Int)

private fun hasNeighborAt(
    mat: Mat,
    row: Int,
    col: Int,
    dr: Int,
    dc: Int,
    cache: MutableMap<PosAndDir, Boolean>
): Boolean {
    val entry = PosAndDir(col, row, dc, dr)
    val neighborY = entry.y + entry.dy
    val neighborX = entry.x + entry.dx
    if (neighborX < 0 || neighborX >= mat.columns) return false
    if (neighborY < 0 || neighborY >= mat.rows) return false
    if (cache.contains(entry)) return cache.getValue(entry)

    val immediateNeighbor = mat.array[neighborY][neighborX]
    return when (immediateNeighbor) {
        PlaceState.SEAT -> false
        PlaceState.FLOOR -> hasNeighborAt(mat, neighborY, neighborX, entry.dy, entry.dx, cache)
        PlaceState.OCCUPIED -> true
    }.also {
        cache.put(entry, it)
    }
}

fun matFromInput(input: String) = Mat(input.lines().map { row -> row.map { PlaceState.fromChar(it) } })

data class Mat(val array: List<List<PlaceState>>) {
    fun repr() = array.map { row -> row.map { it.repr() }.joinToString("") }.joinToString("\n")
    val columns = array.first().size
    val rows = array.size
}

enum class PlaceState {
    SEAT, FLOOR, OCCUPIED;

    companion object {
        fun fromChar(char: Char) = when (char) {
            SEAT_CODE -> SEAT
            FLOOR_CODE -> FLOOR
            OCCUPIED_CODE -> OCCUPIED
            else -> error("")
        }
    }
}

fun PlaceState.repr() = when (this) {
    PlaceState.SEAT -> SEAT_CODE
    PlaceState.FLOOR -> FLOOR_CODE
    PlaceState.OCCUPIED -> OCCUPIED_CODE
}

private const val SEAT_CODE = 'L'
private const val FLOOR_CODE = '.'
private const val OCCUPIED_CODE = '#'

fun updateSeat(state: PlaceState, occupiedNeighbort: Int): PlaceState {
    return when (state) {
        PlaceState.SEAT -> if (occupiedNeighbort == 0) PlaceState.OCCUPIED else PlaceState.SEAT
        PlaceState.FLOOR -> PlaceState.FLOOR
        PlaceState.OCCUPIED -> if (occupiedNeighbort >= 4) PlaceState.SEAT else PlaceState.OCCUPIED
    }
}

fun updateSeat2(state: PlaceState, occupiedNeighbort: Int): PlaceState {
    return when (state) {
        PlaceState.SEAT -> if (occupiedNeighbort == 0) PlaceState.OCCUPIED else PlaceState.SEAT
        PlaceState.FLOOR -> PlaceState.FLOOR
        PlaceState.OCCUPIED -> if (occupiedNeighbort >= 5) PlaceState.SEAT else PlaceState.OCCUPIED
    }
}

fun Mat.update3(): Mat {
    val neighborsCache = mutableMapOf<PosAndDir, Boolean>()
    return Mat(
        array.mapIndexed { rIndex, row ->
            row.mapIndexed { cIndex, cell ->
                val occupiedNeighbors = neighborDirections.count {
                    hasNeighborAt(this, rIndex, cIndex, it.first, it.second, neighborsCache)
                }
                updateSeat2(cell, occupiedNeighbors)
            }
        })
}


fun Mat.update(): Mat {
    val rows = array.size
    val columns = array.first().size
    return Mat(
        array.mapIndexed { rIndex, row ->
            row.mapIndexed { cIndex, cell ->
                val occupiedNeighbors = neighborDirections
                    .map { it.first + rIndex to it.second + cIndex }
                    .filter {
                        it.first in 0 until rows &&
                                it.second in 0 until columns
                    }.map {
                        array[it.first][it.second]
                    }.count { it == PlaceState.OCCUPIED }
                updateSeat(cell, occupiedNeighbors)
            }
        })
}


fun Mat.countOccupied() = array.sumOf { row -> row.count { it == PlaceState.OCCUPIED } }

private val neighborDirections = listOf(
    -1 to -1,
    -1 to 0,
    -1 to 1,

    0 to -1,
    0 to 1,

    1 to -1,
    1 to 0,
    1 to 1
)

fun occupiedOnStabilization(input: Mat, change: (Mat) -> Mat) {
    var prevMat = input
    var mat = prevMat.update()
    var iter = 0
    while (mat != prevMat && iter < 100) {
        iter++
        println("iter $iter")
        prevMat = mat
        mat = change(mat)
    }
    println("stabilized after $iter")
    println("occupied ${mat.countOccupied()}")
}

private fun assert1() {
    val input = """
        L.LL.LL.LL
        LLLLLLL.LL
        L.L.L..L..
        LLLL.LL.LL
        L.LL.LL.LL
        L.LLLLL.LL
        ..L.L.....
        LLLLLLLLLL
        L.LLLLLL.L
        L.LLLLL.LL
    """.trimIndent()
    val t1 = """
        #.##.##.##
        #######.##
        #.#.#..#..
        ####.##.##
        #.##.##.##
        #.#####.##
        ..#.#.....
        ##########
        #.######.#
        #.#####.##
    """.trimIndent()
    val t2 = """
        #.LL.L#.##
        #LLLLLL.L#
        L.L.L..L..
        #LLL.LL.L#
        #.LL.LL.LL
        #.LLLL#.##
        ..L.L.....
        #LLLLLLLL#
        #.LLLLLL.L
        #.#LLLL.##
    """.trimIndent()
    val t3 = """
        #.##.L#.##
        #L###LL.L#
        L.#.#..#..
        #L##.##.L#
        #.##.LL.LL
        #.###L#.##
        ..#.#.....
        #L######L#
        #.LL###L.L
        #.#L###.##
    """.trimIndent()
    val t4 = """
        #.#L.L#.##
        #LLL#LL.L#
        L.L.L..#..
        #LLL.##.L#
        #.LL.LL.LL
        #.LL#L#.##
        ..L.L.....
        #L#LLLL#L#
        #.LLLLLL.L
        #.#L#L#.##
    """.trimIndent()
    val t5 = """
        #.#L.L#.##
        #LLL#LL.L#
        L.#.L..#..
        #L##.##.L#
        #.#L.LL.LL
        #.#L#L#.##
        ..L.L.....
        #L#L##L#L#
        #.LLLLLL.L
        #.#L#L#.##
    """.trimIndent()
    val ts = listOf(t1, t2, t3, t4, t5).map { matFromInput(it) }
    val mat = matFromInput(input)

    ts.forEachIndexed { index, _ ->
        println("test ${index + 1}")
        require(ts[index] == mat.update(index + 1)) {
            "${ts[index].repr()}\n\n${mat.update(index + 1).repr()}"
        }
    }
}

private fun assert2() {
    val input = """
        L.LL.LL.LL
        LLLLLLL.LL
        L.L.L..L..
        LLLL.LL.LL
        L.LL.LL.LL
        L.LLLLL.LL
        ..L.L.....
        LLLLLLLLLL
        L.LLLLLL.L
        L.LLLLL.LL
    """.trimIndent()
    val t1 = """
        #.##.##.##
        #######.##
        #.#.#..#..
        ####.##.##
        #.##.##.##
        #.#####.##
        ..#.#.....
        ##########
        #.######.#
        #.#####.##
    """.trimIndent()
    val t2 = """
        #.LL.LL.L#
        #LLLLLL.LL
        L.L.L..L..
        LLLL.LL.LL
        L.LL.LL.LL
        L.LLLLL.LL
        ..L.L.....
        LLLLLLLLL#
        #.LLLLLL.L
        #.LLLLL.L#
    """.trimIndent()
    val t3 = """
        #.L#.##.L#
        #L#####.LL
        L.#.#..#..
        ##L#.##.##
        #.##.#L.##
        #.#####.#L
        ..#.#.....
        LLL####LL#
        #.L#####.L
        #.L####.L#
    """.trimIndent()
    val t4 = """
        #.L#.L#.L#
        #LLLLLL.LL
        L.L.L..#..
        ##LL.LL.L#
        L.LL.LL.L#
        #.LLLLL.LL
        ..L.L.....
        LLLLLLLLL#
        #.LLLLL#.L
        #.L#LL#.L#
    """.trimIndent()
    val t5 = """
        #.L#.L#.L#
        #LLLLLL.LL
        L.L.L..#..
        ##L#.#L.L#
        L.L#.#L.L#
        #.L####.LL
        ..#.#.....
        LLL###LLL#
        #.LLLLL#.L
        #.L#LL#.L#
    """.trimIndent()
    val ts = listOf(t1, t2, t3, t4, t5).map { matFromInput(it) }
    val mat = matFromInput(input)

    ts.forEachIndexed { index, _ ->
        println("test ${index + 1}")
        require(ts[index] == mat.update3(index + 1)) {
            "\n${ts[index].repr()}\n\n${mat.update3(index + 1).repr()}"
        }
    }
}


fun Mat.update(i: Int) = transform(i) { update() }
fun Mat.update3(i: Int) = transform(i) { update3() }