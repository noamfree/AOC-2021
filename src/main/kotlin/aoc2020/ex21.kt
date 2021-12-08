package aoc2020

import readInputFile

private val testInput1 = """
    mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
    trh fvjkl sbzzf mxmxvkd (contains dairy)
    sqjhc fvjkl (contains soy)
    sqjhc mxmxvkd sbzzf (contains fish)
""".trimIndent()

fun main() {
    //val input = testInput1
    val input = readInputFile("aoc2020/input21")
    val cookBook = parseInput(input)
   // println(cookBook)
    val allergenicCandidates = cookBook.alergens.values.associateWith { alergen ->
        val recepiesThatSurelyContainTheAlergen = cookBook.recepies.filter { alergen in it.alergens }
        val candidates = recepiesThatSurelyContainTheAlergen.fold(
            recepiesThatSurelyContainTheAlergen.first().ingredients.toSet()) { acc, recepie ->
            acc.intersect(recepie.ingredients.toSet())
        }
        candidates
    }
    val nonAlergenic = cookBook.ingredients.values.toSet() - allergenicCandidates.values.flatten().toSet()

    val part1 = cookBook.recepies.sumOf { it.ingredients.count { it in nonAlergenic } }
    println(part1)

    val alergenicIngredients = findAllergens(allergenicCandidates)
    println(alergenicIngredients.sortedBy { it.first.name }.map { it.second.name }.joinToString(","))
    println("day21")
}

private fun findAllergens(allergenicCandidates: Map<Alergen, Set<Ingredient>>): List<Pair<Alergen, Ingredient>> {
    val known: List<Ingredient> = allergenicCandidates.filter { it.value.size == 1 }.map { it.value.first() }
    var currentCandidates: Map<Alergen, Set<Ingredient>> = allergenicCandidates
    var needFilterOut = known
    while (needFilterOut.isNotEmpty()) {
        val nextIter = mutableListOf<Ingredient>()
        currentCandidates = currentCandidates.map { (alergen, ingredients: Set<Ingredient>) ->
            if (ingredients.size == 1 ) return@map alergen to ingredients
            val newCandidates = (ingredients.toSet() - needFilterOut.toSet())
            if (newCandidates.size == 1 && ingredients.size > 1) {
                nextIter.add(newCandidates.first())
            }
            alergen to newCandidates
        }.toMap()
        needFilterOut = nextIter
    }
    require(currentCandidates.all { it.value.size == 1 })
    return currentCandidates.map { it.key to it.value.first() }
}

private data class Ingredient(val id: Int, val name: String)
private data class Alergen(val id: Int, val name: String)

private data class Recepie(val ingredients: List<Ingredient>, val alergens: List<Alergen>)
private data class CookBook(
    val ingredients: Map<String, Ingredient>,
    val alergens: Map<String, Alergen>,
    val recepies: List<Recepie>
)

private fun parseInput(input: String): CookBook {
    val ingredientsMap = mutableMapOf<String, Ingredient>()
    val alergenMap = mutableMapOf<String, Alergen>()

    val recepies = input.lines().map { line ->
        val s = line.split(" (contains ")
        require(s.size == 2)
        val ingredients = s[0].split(" ").map {
            require(' ' !in it)
            ingredientsMap.getOrPut(it) { Ingredient(ingredientsMap.size, it) }
        }
        val alergens = s[1].dropLast(1).split(", ").map {
            require(' ' !in it)
            require(',' !in it)
            alergenMap.getOrPut(it) { Alergen(alergenMap.size, it) }
        }
        Recepie(ingredients = ingredients, alergens = alergens)
    }
    return CookBook(ingredientsMap, alergenMap, recepies)
}
