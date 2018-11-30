package view.core.renderers

import view.core.views.View
import view.core.views.layouts.Layout
import view.di.inject
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import view.utils.elementCss.ElementCss
import view.utils.elementCss.properties.CssUnit
import view.utils.elementCss.properties.Overflow
import view.utils.elementCss.properties.WhiteSpace
import kotlin.browser.document

/**
 * Maps view trees to DOM trees that comply with Material Design standards.
 */
object MaterialDesignJsRenderer: ViewTreeRenderer {

    private val contentRoot = document.getElementsByTagName("body")
    private lateinit var rootLayout: Layout
    private var isInitialized = false

    init {
        if (contentRoot.length != 1) {
            throw IllegalStateException("None or more than one 'body' tags exist in document")
        }

        ElementCss().apply {
            width.set(100.0 to CssUnit.VIEWPORT_WIDTH)
            height.set(100.0 to CssUnit.VIEWPORT_HEIGHT)
        }.applyTo(contentRoot[0]!! as HTMLElement)
    }

    override fun setRoot(layout: Layout) {
        rootLayout = layout
        val layoutRenderer by inject<ViewRenderer<Layout>, View>(layout.name, layout)
        val layoutElement = layoutRenderer.renderView()

        val css = ElementCss().apply {
            width.set(100.0 to CssUnit.VIEWPORT_WIDTH)
            height.set(100.0 to CssUnit.VIEWPORT_HEIGHT)
            overflowX.value = Overflow.Value.HIDDEN
            overflowY.value = Overflow.Value.AUTO
        }

        val config by inject<RendererConfig>()
        if (config.rootLayoutHorizontalNoWrap) {
            css.whiteSpace = WhiteSpace.NOWRAP
        }

        css.applyTo(layoutElement)
        contentRoot[0]!!.appendChild(layoutElement)
        isInitialized = true
    }

    override fun invalidate(view: View) {
        if (isViewAttached(view)) {
            val renderedElement = document.getElementById(view.id.toString())!! as HTMLElement
            val renderer by inject<ViewRenderer<*>, View, HTMLElement>(view.name, view, renderedElement)
            renderer.renderView()
        }
    }

    /**
     * Returns the DOM node corresponding to the view.
     */
    fun <V: View> V.physicalView(): HTMLElement = document.getElementById(id.toString()) as HTMLElement

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