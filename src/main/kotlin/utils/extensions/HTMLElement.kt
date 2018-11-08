package utils.extensions

import org.w3c.dom.*
import utils.elementCss.ElementCss

fun <E: HTMLElement> E.addClasses(classes: Set<String>) {
    this.className += " " + classes.joinToString(separator = " ")
}

fun <E: HTMLElement> E.filterChildrenBy(condition: (HTMLElement) -> Boolean): List<HTMLElement> = viewChildren().filter(condition)

fun <E: HTMLElement> E.viewChildren(): List<HTMLElement> = children().filter { e -> e.hasAttribute("view") }

fun <E: HTMLElement> E.children(): List<HTMLElement> = (0 until childElementCount)
        .map { i -> childNodes[i] as HTMLElement }

fun <E: HTMLElement> E.applyCss(init: ElementCss.() -> Unit) {
    val css = ElementCss()
    css.init()
    css.applyTo(this)
}
