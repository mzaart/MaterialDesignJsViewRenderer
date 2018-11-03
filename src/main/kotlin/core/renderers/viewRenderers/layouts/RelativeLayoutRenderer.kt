package core.renderers.viewRenderers.layouts

import core.loaders.viewTree.IllegalViewTreeException
import core.views.View
import core.views.layouts.RelativeLayout
import org.w3c.dom.HTMLElement
import utils.ElementCss
import utils.algos.graph.Graph
import kotlin.browser.document

typealias Dimension = ElementCss.Dimension
typealias Unit = ElementCss.Dimension.Unit

class RelativeLayoutRenderer(
        view: RelativeLayout,
        element: HTMLElement,
        reRendering: Boolean = true
): LayoutRenderer<RelativeLayout>(view, element, reRendering) {

    constructor(view: RelativeLayout): this(view, document.createElement("div") as HTMLElement, false)

    override fun buildElement() {
        css.position = ElementCss.Position.RELATIVE
        super.buildElement()
    }

    override fun afterChildRenders(child: View, childElement: HTMLElement) {
        val index = view.children().indexOf(child)
        val childCss = ElementCss()
        for (positioning in view.positions[index]) {
            childCss.position = ElementCss.Position.ABSOLUTE
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
        childCss.top = Dimension.ZERO
    }

    private fun alignToParentBottom(childCss: ElementCss) {
        childCss.bottom = Dimension.ZERO
    }

    private fun alignToParentStart(childCss: ElementCss) {
        childCss.start = Dimension.ZERO
    }

    private fun alignToParentEnd(childCss: ElementCss) {
        childCss.end = Dimension.ZERO
    }

    private fun centerHorizontal(childCss: ElementCss) {
        childCss.apply {
            start = Dimension(50.0, Unit.RELATIVE)
            transformation.translateX(Dimension(-50.0, Unit.RELATIVE))

            marginStart = Dimension.ZERO
            marginEnd = Dimension.ZERO
        }
    }

    private fun centerVertical(childCss: ElementCss) {
        childCss.apply {
            top = Dimension(50.0, Unit.RELATIVE)
            transformation.translateY(Dimension(-50.0, Unit.RELATIVE))

            marginTop = Dimension.ZERO
            marginBottom = Dimension.ZERO
        }
    }
}