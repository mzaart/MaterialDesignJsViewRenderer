package utils.elementCss.properties

import org.w3c.dom.HTMLElement
import utils.extensions.nonNull

/**
 * Sets the css properties related to the CSS Grid.
 */
class GridItem: CssProperty {

    var row: Int? = null
    var column: Int? = null

    var rowSpan: Int? = null
    var columnSpan: Int? = null

    override fun applyToStyle(element: HTMLElement) {
        val style = element.style
        row.nonNull { style.setProperty("grid-row-start", it.toString()) }
        column.nonNull { style.setProperty("grid-column-start", it.toString()) }

        rowSpan.nonNull { style.setProperty("grid-row-end", "span $it") }
        columnSpan.nonNull { style.setProperty("grid-column-end", "span $it") }
    }
}