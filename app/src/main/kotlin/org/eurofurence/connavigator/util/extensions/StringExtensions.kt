package org.eurofurence.connavigator.util.extensions

fun String.markdownLinks() = this.replace(Regex("(https?:/\\/.+)"), {
    result: MatchResult -> "[${result.value}](${result.value})"
})
