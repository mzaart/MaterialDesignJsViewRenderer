package utils.elementCss.properties

import org.w3c.dom.css.CSSStyleDeclaration

enum class Display: CssProperty, CssValue {
    None,
    BLOCK,
    INLINE,
    INLINE_BLOCK,
    FLEX;

    override fun applyToStyle(style: CSSStyleDeclaration) {
        style.display = cssString()
    }
}