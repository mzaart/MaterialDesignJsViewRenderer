package utils.elementCss.properties

import org.w3c.dom.HTMLElement

enum class Display: CssProperty, CssValue {
    None,
    BLOCK,
    INLINE,
    INLINE_BLOCK,
    FLEX,
    GRID,
    INLINE_GRID;

    override fun applyToStyle(element: HTMLElement) {
        element.style.display = cssString()
    }
}