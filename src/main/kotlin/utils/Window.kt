package utils

import kotlin.browser.window

fun getGridSize() = when (window.innerWidth) {
        in 0..599 -> 4
        in 600..839 -> 8
        in 840..Int.MAX_VALUE -> 12
        else -> throw IllegalArgumentException()
}