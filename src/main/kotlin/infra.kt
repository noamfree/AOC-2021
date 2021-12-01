inline fun <T> T.transform(times: Int, action: T.() -> T): T {
    require(times >= 0) { "$times negative" }
    var res = this
    repeat(times) {
        res = action(res)
    }
    return res
}

inline fun <T> T.transformIndexed(times: Int, action: T.(i: Int) -> T): T {
    require(times >= 0) { "$times negative" }
    var res = this
    repeat(times) {
        res = res.action(it)
    }
    return res
}

fun String.splitByEmptyLine() = split("\n\n")

fun <T> assertEquals(actual: T, expected: T, message: String? = null) {
    require(actual == expected) { "${if(message!= null) "\n$message:" else ""} \nactual: $actual\nexpected:$expected" }
}



fun assertTrue(actual: Boolean) = assertEquals(actual, true)