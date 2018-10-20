package core.renderers

import core.renderers.viewRenderers.ViewRenderer
import core.views.Theme
import core.views.events.Event
import core.views.events.EventListener
import core.views.layouts.Layout
import di.inject
import org.w3c.dom.get
import utils.ElementCss
import kotlin.browser.document

object MaterialDesignJsRenderer: ViewTreeRenderer {

    private val contentRoot = document.getElementsByTagName("body")

    init {
        if (contentRoot.length != 1) {
            throw IllegalStateException("None or more than one 'body' tags exist in document")
        }
    }

    fun setRoot(layout: Layout) {
        val layoutRenderer by inject<ViewRenderer<Layout>>(layout::class.simpleName)
        val layoutElement = layoutRenderer.renderView(layout)

        val css = ElementCss().apply {
            width = 100.0 to ElementCss.DimensionUnit.VIEWPORT_WIDTH
            height = 100.0 to ElementCss.DimensionUnit.VIEWPORT_HEIGHT
            overflowX = ElementCss.Overflow.HIDDEN
            overflowY = ElementCss.Overflow.SCROLL
        }

        val config by inject<RendererConfig>()
        if (config.rootLayoutHorizontalNoWrap) {
            css.whiteSpace = ElementCss.WhiteSpace.NO_WRAP
        }

        css.applyTo(layoutElement)
        contentRoot[0]!!.appendChild(layoutElement)
    }

    override fun invalidate(viewId: Int) {
        // todo optimize invalidations

    }

    override fun setEventListener(viewId: Int, event: Event, listener: EventListener) {
        throw NotImplementedError()
    }
}