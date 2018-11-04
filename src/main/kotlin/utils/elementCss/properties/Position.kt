package utils.elementCss.properties

import org.w3c.dom.css.CSSStyleDeclaration

enum class Position: CssValue, CssProperty {
    STATIC,
    RELATIVE,
    FIXED,
    ABSOLUTE,
    STICKY;

    override fun applyToStyle(style: CSSStyleDeclaration) {
        style.position = cssString()
    }
}