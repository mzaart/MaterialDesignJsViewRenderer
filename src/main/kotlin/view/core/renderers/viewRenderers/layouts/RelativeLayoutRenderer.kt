package view.core.renderers.viewRenderers.layouts

import view.core.loaders.viewTree.IllegalViewTreeException
import view.core.views.View
import view.core.views.layouts.RelativeLayout
import org.w3c.dom.HTMLElement
import view.utils.elementCss.ElementCss
import view.utils.algos.graph.Graph
import view.utils.elementCss.RelativePositioning
import view.utils.elementCss.properties.Position
import kotlin.browser.document

open class RelativeLayoutRenderer(
        view: RelativeLayout,
        element: HTMLElement,
        reRendering: Boolean = true
): LayoutRenderer<RelativeLayout>(view, element, reRendering) {

    constructor(view: RelativeLayout): this(view, document.createElement("div") as HTMLElement, false)

    override fun buildElement() {
        css.position = Position.RELATIVE
        super.buildElement()
    }

    override fun childCreated(child: View, childElement: HTMLElement): Boolean {
        val index = view.children().indexOf(child)
        val childCss = ElementCss()
        for (positioning in view.positions[index]) {
            childCss.position = Position.ABSOLUTE
            when (positioning.first) {
                RelativeLayout.Positioning.ALIGN_PARENT_TOP -> RelativePositioning.alignToParentTop(childCss)
                RelativeLayout.Positioning.ALIGN_PARENT_BOTTOM -> RelativePositioning.alignToParentBottom(childCss)
                RelativeLayout.Positioning.ALIGN_PARENT_START -> RelativePositioning.alignToParentStart(childCss)
                RelativeLayout.Positioning.ALIGN_PARENT_END -> RelativePositioning.alignToParentEnd(childCss)
                RelativeLayout.Positioning.CENTER_VERTICAL -> RelativePositioning.centerVertical(childCss)
                RelativeLayout.Positioning.CENTER_HORIZONTAL -> RelativePositioning.centerHorizontal(childCss)
                else -> throw NotImplementedError("Renderer doesn't support $positioning positioning")
            }
            childCss.applyTo(childElement)
        }
        return true
    }

    override fun getViewsToBeRendered(): List<Int> {
        val ids = super.getViewsToBeRendered()

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
}