package core.renderers

import core.renderers.viewRenderers.AbstractViewRenderer
import core.views.View
import core.views.layouts.Layout
import di.inject
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import utils.ElementCss
import kotlin.browser.document

object MaterialDesignJsRenderer: ViewTreeRenderer {

    private val contentRoot = document.getElementsByTagName("body")
    private lateinit var rootLayout: Layout
    private var isInitialized = false

    init {
        if (contentRoot.length != 1) {
            throw IllegalStateException("None or more than one 'body' tags exist in document")
        }
    }

    override fun setRoot(layout: Layout) {
        rootLayout = layout
        val layoutRenderer by inject<ViewRenderer<Layout>, View>(layout.name, layout)
        val layoutElement = layoutRenderer.renderView()

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
        isInitialized = true
    }

    override fun invalidate(view: View) {
        if (isViewAttached(view)) {
            val renderedElement = document.getElementById(view.id.toString())!! as HTMLElement
            println(renderedElement)
            val renderer by inject<ViewRenderer<*>, View, HTMLElement>(view.name, view, renderedElement)
            renderer.renderView()
        }
    }

    private fun isViewAttached(view: View): Boolean {
        if (!isInitialized) {
            return false
        }
        var root = view
        while (root.parent != null) {
            root = root.parent!!
        }

        return rootLayout == root
    }
}