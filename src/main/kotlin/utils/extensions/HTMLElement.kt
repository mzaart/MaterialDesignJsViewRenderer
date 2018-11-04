package utils.extensions

import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import utils.elementCss.ElementCss

fun HTMLElement.addClasses(classes: Set<String>) {
    this.className += " " + classes.joinToString(separator = " ")
}

fun HTMLElement.findChild(id: Int) = filterChildrenBy { c -> c.id.toInt() == id }.first()

fun HTMLElement.filterChildrenBy(condition: (HTMLElement) -> Boolean): List<HTMLElement> = children().filter(condition)

fun HTMLElement.children(): List<HTMLElement> = (0 until childElementCount)
        .map { i -> childNodes[i] as HTMLElement }
        .filter { e -> e.hasAttribute("view") }

fun HTMLElement.applyCss(init: ElementCss.() -> Unit) {
    val css = ElementCss()
    css.init()
    css.applyTo(this)
}