package view.core.renderers.viewRenderers

import view.core.renderers.ViewRenderer
import view.core.views.Dimension
import view.core.views.Theme
import view.core.views.View
import view.di.inject
import org.w3c.dom.HTMLElement
import view.utils.elementCss.ElementCss
import view.utils.elementCss.properties.CssUnit
import view.utils.elementCss.properties.Display
import view.utils.elementCss.properties.Visibility
import view.utils.extensions.nonNull
import kotlin.browser.document

/**
 * This class is the base class for all view renderers.
 *
 * It renders common view properties i.e. the properties of the [View] class. The view is rendered by building a
 * DOM node representing the view.
 *
 * The procedure for building the DOM element goes as follows:
 *  1. An instance of the appropriate HTMLElement is passed to the renderer. Note that the DOM element passed may
 *  already be built, in this case the renderer is supposed to apply only that view properties that have changed. See
 *  [reRendering] for more details.
 *  2. This class applies common view attributes to the DOM element.
 *  3. The subclassing builder applies the attributes of a specific view type.
 *  4. The resultant HTMLElement is returned.
 *
 *  @param view The view to render
 *  @param element A HTMLElement that will represent the view.
 *  @param reRendering If true, this parameter indicates that the [element] passed was already built. This happens
 *  when a view is already rendered and one of its properties changes later. In this case the renderer should only
 *  update the element and not rebuild it from scratch.
 *
 */
abstract class AbstractViewRenderer<V: View>(
        protected val view: V,
        protected val element: HTMLElement,
        protected val reRendering: Boolean = false
): ViewRenderer<V> {

    protected val css = ElementCss()

    override fun renderView(): HTMLElement {
        applyCommonViewAttrs()
        buildElement()
        css.applyTo(element)
        return element
    }

    protected abstract fun buildElement()

    private fun applyCommonViewAttrs() {
        // mark element as View
        val viewNode = document.createAttribute("view")
        element.setAttributeNode(viewNode)

        // set id
        element.id = view.id.toString()

        // apply theme
        if (!reRendering) {
            val theme by inject<Theme>()
            theme.applyTo(view)
        }

        handleVisibility()
        handleWidth()
        handleHeight()
        handleMargins()
        handlePadding()
        handleDisabled()
        handleOnClick()
    }

    private fun handleVisibility() {
        if (view.visibility == View.Visibility.GONE) {
            css.display = Display.None
        } else {
            element.style.removeProperty("display")
            css.visibility = when (view.visibility) {
                View.Visibility.VISIBLE -> Visibility.VISIBLE
                View.Visibility.INVISIBLE -> Visibility.HIDDEN
                else -> throw IllegalStateException()
            }
        }
    }

    private fun handleWidth() {
        val width = when (Dimension.type(view.width)) {
            Dimension.Type.WRAP_CONTENT -> null
            Dimension.Type.RELATIVE ->  view.width * 100 to CssUnit.RELATIVE
            Dimension.Type.EXPLICIT -> view.width to CssUnit.PX
        }
        width.nonNull { css.width.set(it) }
    }

    private fun handleHeight() {
        val height = when (Dimension.type(view.height)) {
            Dimension.Type.WRAP_CONTENT -> null
            Dimension.Type.RELATIVE ->  view.height * 100 to CssUnit.RELATIVE
            Dimension.Type.EXPLICIT -> view.height to CssUnit.PX
        }
        height.nonNull { css.height.set(it) }
    }
    
    private fun handleMargins() {
        css.apply {
            view.marginStart.nonNull { marginStart.set(it to CssUnit.PX) }
            view.marginEnd.nonNull { marginEnd.set(it to CssUnit.PX) }
            view.marginTop.nonNull { marginTop.set(it to CssUnit.PX) }
            view.marginBottom.nonNull { marginBottom.set(it to CssUnit.PX) }
        }
    }

    private fun handlePadding() {
        css.apply {
            view.paddingStart.nonNull { paddingStart.set(it to CssUnit.PX) }
            view.paddingEnd.nonNull { paddingEnd.set(it to CssUnit.PX) }
            view.paddingTop.nonNull { paddingTop.set(it to CssUnit.PX) }
            view.paddingBottom.nonNull { paddingBottom.set(it to CssUnit.PX) }
        }
    }
    
    private fun handleDisabled() {
        if (view.disabled) {
            val attr = document.createAttribute("disabled")
            element.setAttributeNode(attr)
        } else {
            element.removeAttribute("disabled")
        }
    }

    private fun handleOnClick() {
        if (view.onClickListener != null) {
            element.onclick = { view.onClickListener!!(view) }
        } else {
            element.onclick = null
        }
    }

    private fun handleOnLongClick() {
        throw TODO()
    }

    private fun handleOnResize() {
        if (view.onResize != null) {
            element.onresize = { view.onResize!!(view) }
        } else {
            element.onresize = null
        }
    }
}
