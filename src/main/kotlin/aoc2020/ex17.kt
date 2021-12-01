fun main() {
    val input = readInputFile("aoc2020/input17")
//    val input = """
//        .#.
//        ..#
//        ###
//    """.trimIndent()
    val cubes = input.lines().flatMapIndexed { r, rowString ->
        rowString.mapIndexed { c, char ->
            if (char == '#') {
                Vector4(c,r,0, 0)
            } else null
        }
    }.filterNotNull()
//    val cubes = listOf(
//    Vector3(0,-1,0),
//    Vector3(1,0,0),
//    Vector3(-1,1,0),
//    Vector3(0,1,0),
//    Vector3(1,1,0),
//    )

    require(cubes.toSet().size == cubes.size)
    val cubes1 = cubes.transform(6) { iterate(this) }
    //printPlane(cubes1)
    println(cubes1.size)
    //printPlane(iterate(cubes), -2)
}

private fun iterate(cubes: List<Vector4>): List<Vector4> {
    val neighborsCount = countNeighbors(cubes)
   // println(neighborsCount[Vector3(1, 2, -1)])
    return activateByNeighbors(cubes, neighborsCount)
}

private fun activateByNeighbors(
    cubes: List<Vector4>,
    neighborsCount: Map<Vector4, Int>
): List<Vector4> {
    return neighborsCount.filter { (cube: Vector4, activeNeighbors: Int) ->
        if (cube in cubes) {
            activeNeighbors == 2 || activeNeighbors == 3
        } else {
            activeNeighbors == 3
        }
    }.map { it.key }
}

private fun countNeighbors(cubes: List<Vector4>): MutableMap<Vector4, Int> {
    val neighborsCount = mutableMapOf<Vector4, Int>()
    cubes.forEach { cube ->
        neighbors.map { it + cube }.forEach {
            neighborsCount.set(it, neighborsCount.getOrDefault(it, 0) + 1)
        }
    }
    return neighborsCount
}

private val neighbors = listOf(
    Vector3(-1, -1, -1),
    Vector3(-1, -1, 0),
    Vector3(-1, -1, 1),

    Vector3(-1, 0, -1),
    Vector3(-1, 0, 0),
    Vector3(-1, 0, 1),

    Vector3(-1, 1, -1),
    Vector3(-1, 1, 0),
    Vector3(-1, 1, 1),


    Vector3(0, -1, -1),
    Vector3(0, -1, 0),
    Vector3(0, -1, 1),

    Vector3(0, 0, -1),
    Vector3(0, 0,0),
    Vector3(0, 0, 1),

    Vector3(0, 1, -1),
    Vector3(0, 1, 0),
    Vector3(0, 1, 1),

    Vector3(1, -1, -1),
    Vector3(1, -1, 0),
    Vector3(1, -1, 1),

    Vector3(1, 0, -1),
    Vector3(1, 0, 0),
    Vector3(1, 0, 1),

    Vector3(1, 1, -1),
    Vector3(1, 1, 0),
    Vector3(1, 1, 1),
).flatMap {
    listOf(Vector4(it,-1), Vector4(it,0), Vector4(it,1))
}.toMutableSet().apply {
    remove(Vector4(0,0,0,0))
    require(size == 80) { "$size"}
}.toList()

private fun printPlane(cubes: List<Vector3>) {
    val minx = cubes.minByOrNull { it.x }!!.x
    val maxx = cubes.maxByOrNull { it.x }!!.x

    val miny = cubes.minByOrNull { it.y }!!.y
    val maxy = cubes.maxByOrNull { it.y }!!.y

    val minz = cubes.minByOrNull { it.z }!!.z
    val maxz = cubes.maxByOrNull { it.z }!!.z

    (minz..maxz).forEach { z ->
        println("z=$z")
        cubes.filter { it.z == z }.let { planeCubes ->
            (miny..maxy).forEach { row ->
                planeCubes.filter { it.y == row }.let { rowCubes ->
                    (minx..maxx).map { col ->
                        if (rowCubes.find { it.x == col } != null) '#' else '.'
                    }.joinToString(" ").let {
                        println(it)
                    }
                }
            }
        }
        println("\n")
    }
}

data class Vector3(val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Vector3): Vector3 {
        return Vector3(x + other.x, y + other.y, z + other.z)
    }
}

data class Vector4(val x: Int, val y: Int, val z: Int, val w: Int) {
    constructor(v: Vector3, w: Int): this(v.x, v.y, v.z, w)

    operator fun plus(other: Vector4): Vector4 {
        return Vector4(x + other.x, y + other.y, z + other.z, w + other.w)
    }
}