package utils.elementCss.properties

import org.w3c.dom.css.CSSStyleDeclaration

enum class WhiteSpace: CssValue, CssProperty {
    NORMAL,
    NOWRAP,
    PRE,
    PRE_LINE,
    PRE_WRAP,
    INITIAL,
    INHERIT;

    override fun applyToStyle(style: CSSStyleDeclaration) {
        style.whiteSpace = cssString()
    }
}