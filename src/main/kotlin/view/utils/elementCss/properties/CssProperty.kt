package view.utils.elementCss.properties

import org.w3c.dom.HTMLElement

/**
 * Defines an interface for classes that represent a css property.
 */
interface CssProperty {

    /**
     * Sets the element's corresponding css property value
     */
    fun applyToStyle(element: HTMLElement)
}