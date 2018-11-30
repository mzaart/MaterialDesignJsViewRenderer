package view.utils.extensions

import org.w3c.dom.*
import view.utils.elementCss.ElementCss

/**
 * Adds a set of classes to an HTMLElement.
 *
 * @param The classes to add to the element
 */
fun <E: HTMLElement> E.addClasses(classes: Set<String>) {
    this.className += " " + classes.joinToString(separator = " ")
}

/**
 * Returns the DOM elements, corresponding to views, that meet a certain condition.
 */
fun <E: HTMLElement> E.filterChildrenBy(condition: (HTMLElement) -> Boolean): List<HTMLElement> = viewChildren().filter(condition)

/**
 * Returns all child DOM elements that correspond to a view.
 */
fun <E: HTMLElement> E.viewChildren(): List<HTMLElement> = children().filter { e -> e.hasAttribute("view") }

/**
 * Returns a list of child nodes of an HTMLElement
 */
fun <E: HTMLElement> E.children(): List<HTMLElement> = (0 until childElementCount)
        .map { i -> childNodes[i] as HTMLElement }

/**
 * Applies css on the element.
 *
 * @see ElementCss
 */
fun <E: HTMLElement> E.applyCss(init: ElementCss.() -> Unit) {
    val css = ElementCss()
    css.init()
    css.applyTo(this)
}
