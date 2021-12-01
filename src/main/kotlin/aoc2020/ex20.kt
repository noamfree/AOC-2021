private val sampleInput1 = """
    Tile 2311:
    ..##.#..#.
    ##..#.....
    #...##..#.
    ####.#...#
    ##.##.###.
    ##...#.###
    .#.#.#..##
    ..#....#..
    ###...#.#.
    ..###..###

    Tile 1951:
    #.##...##.
    #.####...#
    .....#..##
    #...######
    .##.#....#
    .###.#####
    ###.##.##.
    .###....#.
    ..#.#..#.#
    #...##.#..

    Tile 1171:
    ####...##.
    #..##.#..#
    ##.#..#.#.
    .###.####.
    ..###.####
    .##....##.
    .#...####.
    #.##.####.
    ####..#...
    .....##...

    Tile 1427:
    ###.##.#..
    .#..#.##..
    .#.##.#..#
    #.#.#.##.#
    ....#...##
    ...##..##.
    ...#.#####
    .#.####.#.
    ..#..###.#
    ..##.#..#.

    Tile 1489:
    ##.#.#....
    ..##...#..
    .##..##...
    ..#...#...
    #####...#.
    #..#.#.#.#
    ...#.#.#..
    ##.#...##.
    ..##.##.##
    ###.##.#..

    Tile 2473:
    #....####.
    #..#.##...
    #.##..#...
    ######.#.#
    .#...#.#.#
    .#########
    .###.#..#.
    ########.#
    ##...##.#.
    ..###.#.#.

    Tile 2971:
    ..#.#....#
    #...###...
    #.#.###...
    ##.##..#..
    .#####..##
    .#..####.#
    #..#.#..#.
    ..####.###
    ..#.#.###.
    ...#.#.#.#

    Tile 2729:
    ...#.#.#.#
    ####.#....
    ..#.#.....
    ....#..#.#
    .##..##.#.
    .#.####...
    ####.#.#..
    ##.####...
    ##..#.##..
    #.##...##.

    Tile 3079:
    #.#.#####.
    .#..######
    ..#.......
    ######....
    ####.#..#.
    .#...#.##.
    #.#####.##
    ..#.###...
    ..#.......
    ..#.###...
""".trimIndent()


private enum class Edge {
    T, TF,
    B, BF,
    R, RF,
    L, LF;

    fun opposite() = when (this) {
        T -> B
        TF -> BF
        B -> T
        BF -> TF
        R -> L
        RF -> LF
        L -> R
        LF -> RF
    }

    fun right() = when (this) {
        T -> R
        R -> B
        B -> L
        L -> T
        RF -> TF
        BF -> RF
        LF -> BF
        TF -> LF
    }

    fun flipped() = when (this) {
        T -> TF
        TF -> T
        B -> BF
        BF -> B
        R -> RF
        RF -> R
        L -> LF
        LF -> L
    }

    companion object {
        fun topForPositionOnLeft(onLeft: Edge) = onLeft.right()
    }
}
typealias TileEdge = List<Char>

private data class Tile(val id: String, val rawData: List<String>) {
    val edges: Map<Edge, TileEdge> = mapOf(
        Edge.T to rawData.first().toList(),
        Edge.TF to rawData.first().reversed().toList(),

        Edge.B to rawData.last().reversed().toList(),
        Edge.BF to rawData.last().toList(),

        Edge.R to rawData.map { it.last() },
        Edge.RF to rawData.map { it.last() }.reversed(),

        Edge.L to rawData.map { it.first() }.reversed(),
        Edge.LF to rawData.map { it.first() }
    )

    private fun withData(data: List<String>) = copy(rawData = data)

    fun withUpEdge(edge: Edge): Tile = withData(
        when (edge) {
            Edge.T -> rawData
            Edge.TF -> rawData.flipRtL()
            Edge.B -> rawData.rotateLeftOnTop().rotateLeftOnTop()
            Edge.BF -> rawData.rotateLeftOnTop().rotateLeftOnTop().flipRtL()
            Edge.R -> rawData.rotateLeftOnTop().rotateLeftOnTop().rotateLeftOnTop()
            Edge.RF -> rawData.rotateLeftOnTop().rotateLeftOnTop().rotateLeftOnTop().flipRtL()
            Edge.L -> rawData.rotateLeftOnTop()
            Edge.LF -> rawData.rotateLeftOnTop().flipRtL()
        }
    )

    fun withUpToEdge(edge: Edge) = withData(when(edge) {
        Edge.T -> rawData
        Edge.TF -> rawData.flipRtL()
        Edge.B -> rawData.rotateLeftOnTop().rotateLeftOnTop()
        Edge.BF -> rawData.flipRtL().rotateLeftOnTop().rotateLeftOnTop()
        Edge.R -> rawData.rotateLeftOnTop()
        Edge.RF -> rawData.flipRtL().rotateLeftOnTop()
        Edge.L -> rawData.rotateLeftOnTop().rotateLeftOnTop().rotateLeftOnTop()
        Edge.LF -> rawData.flipRtL().rotateLeftOnTop().rotateLeftOnTop().rotateLeftOnTop()
    })

    private fun List<String>.flipRtL() = map { it.reversed() }

    private fun List<String>.rotateLeftOnTop(): List<String> {
        val size = size
        return (0 until size).map { index ->
            map { it[index] }.reversed().joinToString("")
        }
    }

    fun show() = rawData.joinToString("\n")
}

private fun testFlipping() {
    val testTile = Tile("id", listOf("##.", "...", "..."))

    assertEquals(testTile.withUpEdge(Edge.T).rawData, listOf("##.", "...", "..."))
    assertEquals(testTile.withUpEdge(Edge.TF).rawData, listOf(".##", "...", "..."))
    assertEquals(testTile.withUpEdge(Edge.B).rawData, listOf("...", "...", ".##"))
    assertEquals(testTile.withUpEdge(Edge.BF).rawData, listOf("...", "...", "##."))
    assertEquals(testTile.withUpEdge(Edge.L).rawData, listOf("..#", "..#", "..."))
    assertEquals(testTile.withUpEdge(Edge.LF).rawData, listOf("#..", "#..", "..."))
    assertEquals(testTile.withUpEdge(Edge.R).rawData, listOf("...", "#..", "#.."))
    assertEquals(testTile.withUpEdge(Edge.RF).rawData, listOf("...", "..#", "..#"))

    val tile2 = Tile("id2", listOf("123", "456", "789"))
    assertEquals(tile2.edges[Edge.T], "123".toList())
    assertEquals(tile2.edges[Edge.B], "987".toList())
    assertEquals(tile2.edges[Edge.L], "741".toList())
    assertEquals(tile2.edges[Edge.R], "369".toList())

    // transformation is natural
    assertEquals(testTile.withUpEdge(Edge.T).edges[Edge.T], testTile.edges[Edge.T])
    assertEquals(testTile.withUpEdge(Edge.L).edges[Edge.T], testTile.edges[Edge.L])
    assertEquals(testTile.withUpEdge(Edge.B).edges[Edge.T], testTile.edges[Edge.B])
    assertEquals(testTile.withUpEdge(Edge.R).edges[Edge.T], testTile.edges[Edge.R])

    assertEquals(testTile.withUpEdge(Edge.TF).edges[Edge.T], testTile.edges[Edge.TF])
    assertEquals(testTile.withUpEdge(Edge.LF).edges[Edge.T], testTile.edges[Edge.LF])
    assertEquals(testTile.withUpEdge(Edge.BF).edges[Edge.T], testTile.edges[Edge.BF])
    assertEquals(testTile.withUpEdge(Edge.RF).edges[Edge.T], testTile.edges[Edge.RF])

    Edge.values().forEach {
        assertEquals(testTile.withUpEdge(it).withUpToEdge(it), testTile, it.toString())
    }
}

fun main() {
    testFlipping()

    val input = readInputFile("aoc2020/input20")
    //val input = sampleInput1
    val tilesStrings = input.splitByEmptyLine()
    val tiles = parseTiles(tilesStrings)
    val edgesMap = createEdgesMap(tiles)
    val neighborsMap = createNeighborsMao(edgesMap)
    verifyOnlyOneOption(tiles, neighborsMap)

    val cornerTile = tiles.first { tile -> neighborsMap.keys.count { tile.id == it.first } == 4 }

    val firstTileTopDirection = calculateFirstCornerTileDirection(cornerTile, neighborsMap)
    val positions: MutableList<MutableList<MarkedEdge>> =
        mutableListOf(mutableListOf(cornerTile.id to firstTileTopDirection))

    var line = 0
    while (true) {
        while (true) {
            val lastInRow = positions[line].last()
            val lastTop = lastInRow.second
            val lastRight = lastTop.right()
            if (!neighborsMap.contains(lastInRow.first to lastRight)) {
                println("finished row")
                line++
                break
            }
            val next = neighborsMap.getValue(lastInRow.first to lastRight)
            val id = next.first
            val dirOnTop = Edge.topForPositionOnLeft(next.second.flipped())
            positions[line].add(id to dirOnTop)
        }
        println(positions)

        val lastRowStart = positions.last().first()
        val lastRowTop = lastRowStart.second
        val lastBottom = lastRowTop.opposite()
        if (!neighborsMap.contains(lastRowStart.first to lastBottom)) {
            println("finished")
            break
        }
        positions.add(mutableListOf())
        val nextRow = neighborsMap.getValue(lastRowStart.first to lastBottom.flipped())
        val id = nextRow.first
        val dirOnTop = nextRow.second
        positions[line].add(id to dirOnTop)
    }

    printWithEdges(positions, tiles)
    val unifiedImage = unifyTiles(positions, tiles)
    val unifiedTile = Tile("fake", unifiedImage.map { it.joinToString("") })
    val allSeaMonsterPositions = Edge.values().associateWith { edge ->
        patternPositions(unifiedTile.withUpEdge(edge).rawData, seaMonster)
    }
    println(allSeaMonsterPositions)
    val withMarkedMonsters = allSeaMonsterPositions.map { (edge, monsterPositions) ->
        val image = unifiedTile.withUpEdge(edge).rawData.map {
            it.toMutableList()
        }

        markPattern(image, seaMonster, monsterPositions, 'O')
        Tile("fake2", image.map { it.joinToString("") }).withUpToEdge(edge)
//        println(edge)
//        println(image.map { it.joinToString() }.joinToString("\n"))
//        println("\n\n")
    }
    var roughness = 0
    val height = unifiedTile.rawData.size
    val width = unifiedTile.rawData.first().length
    (0 until height).forEach { row ->
        (0 until width).forEach { col ->
            if (withMarkedMonsters.all { it.rawData[row][col] == '#'}) {
                roughness++
            }
        }
    }
    print(roughness)
}

private fun markPattern(image: List<MutableList<Char>>, pattern: List<Vector>, positions: List<Vector>,
                        mark: Char) {
    positions.forEach { v ->
        pattern.forEach { patternV ->
            val u = v + patternV
            image[u.s][u.w] = mark
        }
    }
}

private fun patternPositions(image: List<String>, pattern: List<Vector>): List<Vector> {
    val patternWidth: Int = pattern.maxOf { it.w }
    val patternHeight = pattern.maxOf { it.s }

    val imageWidth = image.first().length
    val imageHeight = image.size

    val positions = mutableListOf<Vector>()
    (0 until imageHeight-patternHeight).forEach { row ->
        (0 until imageWidth - patternWidth).forEach { col ->
            if (pattern.all { patternV ->
                image[row + patternV.s][col+patternV.w] == '#'
            }) {
                positions.add(Vector(w = col, s = row))
            }
        }
    }
    return positions
}

val seaMonster = listOf(
    Vector(0, 1),
    Vector(1, 2),

    Vector(4, 2),
    Vector(5, 1),
    Vector(6, 1),
    Vector(7, 2),

    Vector(10, 2),
    Vector(11, 1),
    Vector(12, 1),
    Vector(13, 2),

    Vector(16,2),
    Vector(17,1),
    Vector(18,0),
    Vector(18,1),
    Vector(19,1),
)

private fun calculateFirstCornerTileDirection(
    cornerTile: Tile,
    neighborsMap: Map<MarkedEdge, MarkedEdge>
): Edge {
    return if (
        neighborsMap.contains(cornerTile.id to Edge.B) &&
        neighborsMap.contains(cornerTile.id to Edge.R)
    ) {
        Edge.T
    } else if (
        neighborsMap.contains(cornerTile.id to Edge.B) &&
        neighborsMap.contains(cornerTile.id to Edge.L)
    ) {
        Edge.TF
    } else if (
        neighborsMap.contains(cornerTile.id to Edge.T) &&
        neighborsMap.contains(cornerTile.id to Edge.L)
    ) {
        Edge.B
    } else {
        require(
            neighborsMap.contains(cornerTile.id to Edge.T) &&
                    neighborsMap.contains(cornerTile.id to Edge.R)
        )
        Edge.BF
    }
}

private fun verifyOnlyOneOption(tiles: List<Tile>, neighborsMap: Map<MarkedEdge, MarkedEdge>) {
    tiles.filter { tile ->
        neighborsMap.keys.count { it.first == tile.id } == 4
    }.apply {
        require(size == 4)
    }.forEach {
        println("${it.id} is corner")
    }
}


private fun unifyTiles(positions: List<List<Pair<String, Edge>>>, tiles: List<Tile>): List<List<Char>> {
    val ids = positions.flatten().map { it.first }
    require(ids.size == ids.toSet().size)

    val tileSize = tiles.first().rawData.size

    val correctedTiles: List<Tile> = tiles.map { tile ->
        val top = positions.flatten().find { it.first == tile.id }!!.second
        tile.withUpEdge(top)
    }

    val a: List<List<Char>> = positions.flatMap { row ->
        val rowTiles: List<Tile> = row.map { it.first }.map { correctedTiles.find { tile -> tile.id == it }!! }
        val allRowRows: List<List<Char>> = (0 until tileSize)
            .drop(1)
            .dropLast(1).map { innerRow ->
                rowTiles.flatMap {
                    it.rawData[innerRow]
                        .drop(1)
                        .dropLast(1).toList()
                }
            }
        allRowRows
    }
    return a
}

private fun printWithEdges(positions: List<List<Pair<String, Edge>>>, tiles: List<Tile>) {
    val ids = positions.flatten().map { it.first }
    require(ids.size == ids.toSet().size)

    val tileSize = tiles.first().rawData.size

    val correctedTiles: List<Tile> = tiles.map { tile ->
        val top = positions.flatten().find { it.first == tile.id }!!.second
        tile.withUpEdge(top)
    }

    positions.forEach { row ->
        val rowTiles = row.map { it.first }.map { correctedTiles.find { tile -> tile.id == it }!! }
        (0 until tileSize).forEach { innerRow ->
            println(rowTiles.map { it.rawData[innerRow] }.joinToString(" "))
        }
        println()
    }
}

private typealias MarkedEdge = Pair<String, Edge>

private fun createNeighborsMao(edgesMap: Map<TileEdge, List<MarkedEdge>>): Map<MarkedEdge, MarkedEdge> {
    val neighborsMap = mutableMapOf<Pair<String, Edge>, Pair<String, Edge>>()
    edgesMap.forEach { (k, v) ->
        require(v.size <= 2)
        if (v.size == 2) {
            neighborsMap[v[1]] = v[0]
            neighborsMap[v[0]] = v[1]
        }
    }
    return neighborsMap
}

private fun createEdgesMap(tiles: List<Tile>): Map<TileEdge, List<MarkedEdge>> {
    val edgesMap = mutableMapOf<TileEdge, List<Pair<String, Edge>>>()
    tiles.forEach { tile ->
        tile.edges.forEach { (e: Edge, tileEdge: TileEdge) ->
            val current = edgesMap.getOrDefault(tileEdge, listOf())
            edgesMap[tileEdge] = current + listOf(tile.id to e)
        }
    }
    return edgesMap
}

private fun parseTiles(tilesStrings: List<String>): List<Tile> {
    return tilesStrings.map { tileString ->
        val idLine = tileString.lines()[0]
        require(idLine.endsWith(":"))
        val id = idLine.dropLast(1).split(" ")[1]
        val rawData = tileString.lines().drop(1)
        Tile(id, rawData)
    }
}