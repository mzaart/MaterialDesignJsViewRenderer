package core.renderers.viewRenderers

import core.renderers.ViewRenderer
import core.views.Dimension
import core.views.Theme
import core.views.View
import di.inject
import org.w3c.dom.HTMLElement
import utils.elementCss.ElementCss
import utils.elementCss.properties.CssDimen
import utils.elementCss.properties.CssUnit
import utils.elementCss.properties.Display
import utils.elementCss.properties.Visibility
import utils.extensions.nonNull
import kotlin.browser.document

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
