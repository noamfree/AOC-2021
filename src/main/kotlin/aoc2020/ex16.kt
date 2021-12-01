fun main() {
    val input = readInputFile("aoc2020/input16")
//    val input = """
//        class: 1-3 or 5-7
//        row: 6-11 or 33-44
//        seat: 13-40 or 45-50
//
//        your ticket:
//        7,1,14
//
//        nearby tickets:
//        7,3,47
//        40,4,50
//        55,2,20
//        38,6,12
//    """.trimIndent()
//    val input = """
//        class: 0-1 or 4-19
//        row: 0-5 or 8-19
//        seat: 0-13 or 16-19
//
//        your ticket:
//        11,12,13
//
//        nearby tickets:
//        3,9,18
//        15,1,5
//        5,14,9
//    """.trimIndent()
    val parts = input.split("\n\n")
    require(parts.size == 3)
    val rulesPart = parts[0].lines()
    val ticketsPart = parts[2].lines()
    require(ticketsPart[0] == "nearby tickets:")

    val requirements = parseRequirement(rulesPart)
    val tickets = parseTickets(ticketsPart.drop(1))
    val validTickets = validTickets(requirements, tickets)
    val numberOfFields = tickets.first().fields.size
    var requirementsOptions = List(numberOfFields) {
        requirements
    }


    validTickets.forEach { ticket ->
        requirementsOptions = requirementsOptions.filterForTicket(ticket)
    }
    var iter = 0
    while (requirementsOptions.any { it.size > 1 } && iter < 1000) {
        iter++
        println("removing iteration $iter")
        requirementsOptions.forEachIndexed { index, list ->
            if (list.size == 1) {
                requirementsOptions = chooseOption(requirementsOptions, index)
            }
        }
    }
    val finalRequirements = requirementsOptions.map { it[0] }
    val myTicket = parseTicket(parts[1].lines()[1])
    myTicket.fields.filterIndexed {index, value -> finalRequirements[index].name.contains("departure") }
        .fold(1L) {acc, i -> acc*i.toLong() }.let { print(it) }

    //print(requirementsOptions.map { it.map { it.name }.joinToString(",") }.joinToString("\n"))
}

private typealias RequirementOptions = List<List<TicketRequirement>>

private fun RequirementOptions.filterForTicket(ticket: Ticket): List<List<TicketRequirement>> {
    return ticket.fields.mapIndexed { index, value ->
        filterForField(index, value)
    }
}

private fun RequirementOptions.filterForField(index: Int, value: Int): List<TicketRequirement> {
    val optionalRequirements = get(index)
    if (optionalRequirements.size == 1) return optionalRequirements
    return optionalRequirements.filter { it.holds(value) }.toList()
}

private fun chooseOption(requirementsOptions: List<List<TicketRequirement>>, index: Int): List<List<TicketRequirement>> {
    require(requirementsOptions[index].size == 1)
    val optionToChoose = requirementsOptions[index][0]
    return requirementsOptions.mapIndexed { i, list ->
        if (i == index) return@mapIndexed list
        val place = list.indexOf(optionToChoose)
        return@mapIndexed if (place != -1) {
            list.toMutableList().apply { removeAt(place) }
        } else list
    }
}

private fun validTickets(
    requirements: List<TicketRequirement>,
    tickets: List<Ticket>
): List<Ticket> {
    val allRequirement = unifyAllRequirements(requirements)
    return tickets
        .filter {
            it.fields.all { allRequirement.holds(it) }
        }
}

private data class Ticket(val fields: List<Int>)

private fun parseTickets(ticketsString: List<String>): List<Ticket> {
    return ticketsString.map { parseTicket(it) }
}

private fun parseTicket(ticketsString: String): Ticket {
    require(ticketsString.lines().size == 1)
    return Ticket(ticketsString.split(",").map { it.toInt() })
}

private fun unifyAllRequirements(requirements: List<TicketRequirement>): TicketRequirement {
    return TicketRequirement("fake all", requirements.flatMap { it.optionalRanges })
}

private data class TicketRequirement(
    val name: String,
    val optionalRanges: List<IntRange>
) {
    fun holds(field: Int): Boolean {
        return optionalRanges.any { field in it }
    }
}

private fun parseRequirement(rulesPart: List<String>) = rulesPart.map { row ->
    val s = row.split(": ")
    require(s.size == 2)
    val name = s[0]
    val rangesTexts = s[1].split(" or ")
    require(rangesTexts.size == 2)
    val ranges: List<IntRange> = rangesTexts.map { rangeText ->
        val se = rangeText.split("-")
        require(se.size == 2)
        val start = se[0].toInt()
        val end = se[1].toInt()
        require(start < end)
        start..end
    }
    TicketRequirement(
        name,
        ranges
    )
}
