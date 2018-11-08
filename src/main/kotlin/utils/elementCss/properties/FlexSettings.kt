package utils.elementCss.properties

import org.w3c.dom.HTMLElement
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

    override fun applyToStyle(element: HTMLElement) {
        direction.nonNull { element.style.flexDirection = it.cssString() }
    }
}