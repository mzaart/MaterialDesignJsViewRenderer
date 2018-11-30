package view.core.renderers.viewRenderers.layouts

import kotlin.browser.document
import view.core.views.View
import view.core.views.layouts.GridLayout
import org.w3c.dom.*
import view.utils.elementCss.ElementCss
import view.utils.elementCss.RelativePositioning
import view.utils.elementCss.properties.CssUnit
import view.utils.elementCss.properties.Display
import view.utils.elementCss.properties.Position
import view.utils.extensions.*

class GridLayoutRenderer(
        view: GridLayout,
        element: HTMLElement,
        reRendering: Boolean = true
): LayoutRenderer<GridLayout>(view, element, reRendering) {

    constructor(view: GridLayout): this(view, document.createElement("div") as HTMLElement, false)

    override fun buildElement() {
        element.applyCss {
            display = Display.GRID
            grid.rowCount = view.rowCount
            grid.columnCount = view.columnCount
            grid.rowGap.set(view.horizontalSpace to CssUnit.PX)
            grid.columnGap.set(view.verticalSpace to CssUnit.PX)
        }
        super.buildElement()
    }

    override fun childCreated(child: View, childElement: HTMLElement): Boolean {
        val childIndex = view.children().indexOf(child)
        val cell = view.cells[childIndex]

        // construct container div
        val cellDiv = document.createElement("div") as HTMLDivElement
        cellDiv.applyCss {
            position = Position.RELATIVE
            gridItem.row = cell.row + 1
            gridItem.column = cell.column + 1
            gridItem.rowSpan = cell.rowSpan
            gridItem.columnSpan = cell.columnSpan
        }
        cellDiv.setAttribute("index", "(${cell.row},${cell.column})")

        // edit child element
        val childCss = ElementCss().apply { position = Position.ABSOLUTE }

        when(cell.horizontalAlignment) {
            GridLayout.Cell.HorizontalAlignment.START -> RelativePositioning.alignToParentStart(childCss)
            GridLayout.Cell.HorizontalAlignment.END -> RelativePositioning.alignToParentEnd(childCss)
            GridLayout.Cell.HorizontalAlignment.CENTER -> RelativePositioning.centerHorizontal(childCss)
        }
        when (cell.verticalAlignment) {
            GridLayout.Cell.VerticalAlignment.TOP -> RelativePositioning.alignToParentTop(childCss)
            GridLayout.Cell.VerticalAlignment.BOTTOM -> RelativePositioning.alignToParentBottom(childCss)
            GridLayout.Cell.VerticalAlignment.CENTER -> RelativePositioning.centerVertical(childCss)
        }
        childCss.applyTo(childElement)

        cellDiv.style.setProperty("place-self", "stretch")
        cellDiv.appendChild(childElement)
        element.appendChild(cellDiv)
        return false
    }

    override fun beforeChildIsRemoved(childElement: HTMLElement): Boolean {
        val cell = childElement.parentElement!!
        cell.parentNode!!.removeChild(cell)
        return false
    }

    override fun getChildElements(): List<HTMLElement> {
        return element.children().map { it.children[0]!! as HTMLElement }
    }
}