package view.utils.elementCss.properties

import org.w3c.dom.HTMLElement
import view.utils.extensions.nonNull

/**
 * Sets CSS properties of child elements of a FlexBox.
 */
open class FlexItemSettings: CssProperty {

    enum class AlignSelf: CssValue {

        CENTER,
        FLEX_START,
        FLEX_END,
        BASELINE,
        STRETCH
    }

    open var alignSelf: AlignSelf? = null
    open var shrink: Number? = null
    open var grow: Number? = null
    open var flexBasis: Number? = null

    override fun applyToStyle(element: HTMLElement) {
        alignSelf.nonNull { element.style.alignSelf = it.cssString() }
        shrink.nonNull { element.style.flexShrink = it.cssString() }
        grow.nonNull { element.style.flexGrow = it.cssString() }
        flexBasis.nonNull { element.style.flexBasis = it.cssString() }
    }
}