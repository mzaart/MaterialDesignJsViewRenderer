package utils.elementCss.properties

import org.w3c.dom.HTMLElement

interface CssProperty {

    fun applyToStyle(element: HTMLElement)
}