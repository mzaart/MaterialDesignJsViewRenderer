package view.utils.elementCss.properties

import org.w3c.dom.HTMLElement

enum class WhiteSpace: CssValue, CssProperty {
    NORMAL,
    NOWRAP,
    PRE,
    PRE_LINE,
    PRE_WRAP,
    INITIAL,
    INHERIT;

    override fun applyToStyle(element: HTMLElement) {
        element.style.whiteSpace = cssString()
    }
}