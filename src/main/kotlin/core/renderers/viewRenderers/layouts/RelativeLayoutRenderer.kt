package core.renderers.viewRenderers.layouts

import core.loaders.viewTree.IllegalViewTreeException
import core.views.View
import core.views.layouts.RelativeLayout
import org.w3c.dom.HTMLElement
import utils.elementCss.ElementCss
import utils.algos.graph.Graph
import utils.elementCss.properties.CssDimen
import utils.elementCss.properties.CssUnit
import utils.elementCss.properties.Position
import kotlin.browser.document

class RelativeLayoutRenderer(
        view: RelativeLayout,
        element: HTMLElement,
        reRendering: Boolean = true
): LayoutRenderer<RelativeLayout>(view, element, reRendering) {

    constructor(view: RelativeLayout): this(view, document.createElement("div") as HTMLElement, false)

    override fun buildElement() {
        css.position = Position.RELATIVE
        super.buildElement()
    }

    override fun afterChildRenders(child: View, childElement: HTMLElement) {
        val index = view.children().indexOf(child)
        val childCss = ElementCss()
        for (positioning in view.positions[index]) {
            childCss.position = Position.ABSOLUTE
            when (positioning.first) {
                RelativeLayout.Positioning.ALIGN_PARENT_TOP -> alignToParentTop(childCss)
                RelativeLayout.Positioning.ALIGN_PARENT_BOTTOM -> alignToParentBottom(childCss)
                RelativeLayout.Positioning.ALIGN_PARENT_START -> alignToParentStart(childCss)
                RelativeLayout.Positioning.ALIGN_PARENT_END -> alignToParentEnd(childCss)
                RelativeLayout.Positioning.CENTER_VERTICAL -> centerVertical(childCss)
                RelativeLayout.Positioning.CENTER_HORIZONTAL -> centerHorizontal(childCss)
                else -> throw NotImplementedError("Renderer doesn't support $positioning positioning")
            }
            childCss.applyTo(childElement)
        }
    }

    override fun getViewsToBeRendered(): List<Int> {
        val ids = super.getViewsToBeRendered()
        val childIds = view.children().map { it.id }

        val edges: MutableList<Pair<Int, Int>> = mutableListOf()
        for (i in 0 until view.children().size) {
            for (p in view.positions[i]) {
                edges += view.children()[i].id to p.second
            }
        }

        val graph = Graph.fromEdges(edges)
        val orderedIds: List<Int>
        try {
            orderedIds = graph.topologicalSort()
        } catch (e: IllegalStateException) {
            throw IllegalViewTreeException("Invalid relative positioning")
        }
        return orderedIds.filter { it in ids }
    }

    private fun alignToParentTop(childCss: ElementCss) {
        childCss.top.set(CssDimen.ZERO)
    }

    private fun alignToParentBottom(childCss: ElementCss) {
        childCss.bottom.set(CssDimen.ZERO)
    }

    private fun alignToParentStart(childCss: ElementCss) {
        childCss.start.set(CssDimen.ZERO)
    }

    private fun alignToParentEnd(childCss: ElementCss) {
        childCss.end.set(CssDimen.ZERO)
    }

    private fun centerHorizontal(childCss: ElementCss) {
        childCss.apply {
            start.set(50.0 to CssUnit.RELATIVE)
            transformation.translateX(-50.0 to CssUnit.RELATIVE)

            marginStart.set(CssDimen.ZERO)
            marginEnd.set(CssDimen.ZERO)
        }
    }

    private fun centerVertical(childCss: ElementCss) {
        childCss.apply {
            top.set(50.0 to CssUnit.RELATIVE)
            transformation.translateY(-50.0 to CssUnit.RELATIVE)

            marginTop.set(CssDimen.ZERO)
            marginBottom.set(CssDimen.ZERO)
        }
    }
}