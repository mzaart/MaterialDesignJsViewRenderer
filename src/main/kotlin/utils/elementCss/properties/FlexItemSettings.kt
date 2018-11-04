package utils.elementCss.properties

import org.w3c.dom.css.CSSStyleDeclaration
import utils.extensions.nonNull

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

    override fun applyToStyle(style: CSSStyleDeclaration) {
        alignSelf.nonNull { style.alignSelf = it.cssString() }
        shrink.nonNull { style.flexShrink = it.cssString() }
        grow.nonNull { style.flexGrow = it.cssString() }
        flexBasis.nonNull { style.flexBasis = it.cssString() }
    }
}