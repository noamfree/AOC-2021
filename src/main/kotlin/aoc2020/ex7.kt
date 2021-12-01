fun main() {
    val input = readInputFile("aoc2020/input7")
    //println(input.lines()[0])
    val rawRequirements = input.lines().map { parseLine(it) }
    val (colorMap, requirements) = createColorMap(rawRequirements)
    part1(colorMap, requirements)
    part2(colorMap, requirements)
}

private fun part2(colorMap: MutableMap<String, Color>, requirements: List<Requirement>) {
    println(countContainedAndSelf("shiny gold", colorMap, requirements) - 1)
}

private fun part1(colorMap: MutableMap<String, Color>, requirements: List<Requirement>) {
    println(findContainersOf("shiny gold", colorMap, requirements).size)
}

private fun countContainedAndSelf(colorString: String,
                                  colorMap: MutableMap<String, Color>,
                                  requirements: List<Requirement>): Int {
    val requirement = requirements.find { it.container.originalString == colorString }!!
    if (requirement.containedOptions.isEmpty()) return 1
    println(requirement)
    return requirement.containedOptions.sumOf { option -> option.number * countContainedAndSelf(option.color.originalString, colorMap, requirements)  } + 1
}

private fun findContainersOf(colorString: String,
                             colorMap: MutableMap<String, Color>,
                             requirements: List<Requirement>): Set<String> {
    var iterStart = setOf<String>(colorString)
    var iterEnd = findContainersOf(iterStart, requirements)
    while (iterStart.size != iterEnd.size) {
       iterStart = iterEnd
       iterEnd += findContainersOf(iterStart, requirements)
    }
    return iterEnd
}

private fun findContainersOf(contained: Set<String>, requirements: List<Requirement>): Set<String> {
    return requirements.filter {  requirement ->
        requirement.containedOptions.find { it.color.originalString in contained } != null
    }.map {
        it.container.originalString
    }.toSet()
}


data class Color(val int: Int, val originalString: String)

private fun createColorMap(rawRequirements: List<Pair<String, List<RawContained>>>): Pair<MutableMap<String, Color>, List<Requirement>> {
    val map = mutableMapOf<String, Color>()

    val requirements = rawRequirements.mapIndexed { index, requirement ->
        val containerColor = map.getOrPut(requirement.first) { Color(map.size, requirement.first) }
        val containedOptions = requirement.second.mapNotNull { rawContained ->
            if (rawContained == RawContained.EMPTY) return@mapNotNull null
            val color = map.getOrPut(rawContained.color) { Color(map.size, rawContained.color) }
            ContainedOption(color, rawContained.number)
        }
        Requirement(containerColor, containedOptions, lineInFile = index+1)
    }
    return map to requirements
}

private data class Requirement(val container: Color, val containedOptions: List<ContainedOption>, val lineInFile: Int = 0) {
    override fun toString(): String {
        return "line ${lineInFile}: ${container.originalString} contains ${containedOptions.map { "${it.number} ${it.color.originalString}" }.joinToString(", ")}"
    }
}
data class ContainedOption(val color: Color, val number: Int)

data class RawContained(val color: String, val number: Int) {
    companion object {
        val EMPTY = RawContained("", 0)
    }
}

fun parseLine(line: String): Pair<String, List<RawContained>> {
    require(line.last() == '.')
    val s = line.dropLast(1).split("contain")
    require(s.size == 2)
    val container = s[0]
    val inTheBag = s[1]
    return getContainerColor(container) to getContainedOptions(inTheBag)
}

private fun getContainedOptions(containedStrings: String): List<RawContained> {
    val optionStrings = containedStrings.split(", ")
    if (optionStrings.isEmpty()) return emptyList()

    require(optionStrings.first().startsWith(""))
    val fistOptionCorrected = optionStrings.first().drop(1)

    return optionStrings.drop(1).map { getContainedOption(it) } + getContainedOption(fistOptionCorrected)
}

private fun getContainedOption(optionString: String): RawContained {
    require(!optionString.startsWith(" "))
    if (optionString == "no other bags") {
        return RawContained.EMPTY
    }
    val splitted = optionString.split(" ")
    require(splitted.size == 4)
    require(splitted[3] == "bag" || splitted[3] == "bags")
    val number = splitted[0].toInt()
    val color = "${splitted[1]} ${splitted[2]}"
    return RawContained(color, number)
}

private fun getContainerColor(containerString: String): String {
    require(containerString.endsWith(" bags "))
    return containerString.dropLast(6)
}