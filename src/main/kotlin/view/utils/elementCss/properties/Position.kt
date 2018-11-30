package view.utils.elementCss.properties

import org.w3c.dom.HTMLElement

enum class Position: CssValue, CssProperty {
    STATIC,
    RELATIVE,
    FIXED,
    ABSOLUTE,
    STICKY;

    override fun applyToStyle(element: HTMLElement) {
        element.style.position = cssString()
    }
}