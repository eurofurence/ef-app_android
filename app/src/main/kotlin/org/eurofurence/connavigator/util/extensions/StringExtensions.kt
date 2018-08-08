package org.eurofurence.connavigator.util.extensions

fun String?.markdownLinks() = this?.replace(Regex("(https?://.+)")) {
    result: MatchResult -> "[${result.value}](${result.value})"
}

/**
 * Tries to parse the string as a unicode character
 */
fun String.toUnicode() = this.toLong(radix = 16).toChar().toString()