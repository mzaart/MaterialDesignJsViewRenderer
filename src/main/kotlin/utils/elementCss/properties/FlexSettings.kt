package utils.elementCss.properties

import org.w3c.dom.css.CSSStyleDeclaration
import utils.extensions.nonNull

open class FlexSettings: CssProperty {
    enum class Direction: CssValue {

        HORIZONTAL {
            override fun cssString() = "row"
        },
        VERTICAL {
            override fun cssString() = "column"
        }
    }

    open var direction: Direction? = null

    override fun applyToStyle(style: CSSStyleDeclaration) {
        direction.nonNull { style.flexDirection = it.cssString() }
    }
}