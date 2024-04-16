package uk.ac.aber.dcs.cs31620.assignment.ui.components

/**
 * Original code sourced from a question found on StackOverflow asking how to convert an int to an ordinal
 * https://stackoverflow.com/questions/41772666/does-kotlin-have-a-standard-way-to-format-a-number-as-an-english-ordinal
 */

fun Int.toOrdinal() = "$this" + when {
    (this % 100 in 11..13) -> "th"
    (this % 10) == 1 -> "st"
    (this % 10) == 2 -> "nd"
    (this % 10) == 3 -> "rd"
    else -> "th"
}

fun Int.toSets() = "$this set" + when {
    this == 1 -> ""
    else -> "s"
}

fun Int.toExercises() = "$this exercise" + when {
    this == 1 -> ""
    else -> "s"
}

fun String.toUpperFirst() = this.lowercase().replaceFirstChar { char -> char.titlecase() }