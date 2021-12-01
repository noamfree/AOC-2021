fun main() {
    val input = readInputFile("aoc2020/input4")

    val passports = splitToPassports(input)
    val passportData = passports.map { mapPassportData(it) }

    println(passportData.count { validatePassportData(it, requiredFields) })

}

fun splitToPassports(input: String): List<String> {
    return input.split("\n\n")
}

fun mapPassportData(passportData: String): Map<String, String> {
    return passportData.split(Regex("[ \n]")).associate { pair ->
        val keyValue = pair.split(":")
        require(keyValue.size == 2)
        keyValue[0] to keyValue[1]
    }
}

fun validatePassportData(passportData: Map<String, String>, requiredFields: List<String>): Boolean {
    if (!passportData.keys.containsAll(requiredFields)) return false
    passportData["byr"]!!.run {
        if (length != 4) return false
        val int = toInt()
        if (int !in 1920..2002) return false
    }
    println("byr")
    passportData["iyr"]!!.run {
        if (length != 4) return false
        val int = toInt()
        if (int !in 2010..2020) return false
    }
    println("iyr")
    passportData["eyr"]!!.run {
        if (length != 4) return false
        val int = toInt()
        if (int !in 2020..2030) return false
    }
    println("eyr")
    passportData["hgt"]!!.run {
        val measure = takeLast(2)
        val size = take(length - 2).toInt()
        when (measure) {
            "cm" -> if (size !in 150..193) return false
            "in" -> if (size !in 59..76) return false
            else -> return false
        }
    }
    println("hgt")
    passportData["hcl"]!!.run {
        if (length != 7) return false
        if (this[0] != '#') return false
        if (!this.drop(1).all {
                it in listOf('0','1','2','3','4','5','6','7','8','9','0','a','b','c','d','e','f')
        }) return false
    }
    println("hcl")
    passportData["ecl"]!!.run {
        if (this !in listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth"))return false
    }
    println("ecl")
    passportData["pid"]!!.run {
        if (length != 9) return false
        if (!this.all { it in listOf('0','1','2','3','4','5','6','7','8','9') })
            return false
    }
    println("pid")

//    val byr = byr!!.toInt()
    return true
}

val requiredFields = listOf<String>(
    "byr",
    "iyr",
    "eyr",
    "hgt",
    "hcl",
    "ecl",
    "pid",
    //"cid",
)