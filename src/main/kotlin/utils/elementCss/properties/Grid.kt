package utils.elementCss.properties

import org.w3c.dom.HTMLElement
import utils.extensions.nonNull

/**
 * Sets CSS properties related to a CSS grid.
 */
class Grid: CssProperty {

    var rowGap: CssDimen = Dimension("grid-row-gap")
    var columnGap: CssDimen = Dimension("grid-column-gap")

    var rowCount: Int? = null
    var columnCount: Int? = null

    override fun applyToStyle(element: HTMLElement) {
        rowGap.applyToStyle(element)
        columnGap.applyToStyle(element)

        val style = element.style
        rowCount.nonNull { style.setProperty("grid-template-rows", "repeat($it, 1fr") }
        columnCount.nonNull { style.setProperty("grid-template-columns", "repeat($it, 1fr") }
        style.setProperty("justify-items", "center")
        style.setProperty("align-items", "center")
    }
}