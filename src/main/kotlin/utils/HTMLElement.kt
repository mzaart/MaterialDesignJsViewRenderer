package utils

import org.w3c.dom.HTMLElement

fun HTMLElement.addClasses(classes: Set<String>) {
    this.className += " " + classes.joinToString(separator = " ")
}