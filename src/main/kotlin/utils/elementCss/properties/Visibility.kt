package utils.elementCss.properties

import org.w3c.dom.css.CSSStyleDeclaration

enum class Visibility: CssValue, CssProperty {
    VISIBLE,
    HIDDEN,
    COLLAPSE,
    INITIAL,
    INHERIT;

    override fun applyToStyle(style: CSSStyleDeclaration) {
        style.visibility = cssString()
    }
}