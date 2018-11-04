package utils.elementCss.properties

import org.w3c.dom.css.CSSStyleDeclaration

interface CssProperty {

    fun applyToStyle(style: CSSStyleDeclaration)
}