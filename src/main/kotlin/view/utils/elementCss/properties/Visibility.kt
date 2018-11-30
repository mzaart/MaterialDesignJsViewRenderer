package view.utils.elementCss.properties

import org.w3c.dom.HTMLElement

enum class Visibility: CssValue, CssProperty {
    VISIBLE,
    HIDDEN,
    COLLAPSE,
    INITIAL,
    INHERIT;

    override fun applyToStyle(element: HTMLElement) {
        element.style.visibility = cssString()
    }
}