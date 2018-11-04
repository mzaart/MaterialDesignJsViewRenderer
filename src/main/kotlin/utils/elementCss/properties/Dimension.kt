package utils.elementCss.properties

import org.w3c.dom.css.CSSStyleDeclaration

typealias CssDimen = Dimension
typealias CssUnit = Dimension.Unit

open class Dimension(
        var value: Double?,
        var unit: Unit?,
        val propertyName: String
): CssValue, CssProperty {

    enum class Unit: CssValue {
        CM,
        MM,
        IN,
        PX,
        PT,
        PC,
        EM,
        EX,
        CH ,
        REM,
        AUTO,
        MIN_CONTENT,
        MAX_CONTENT,
        RELATIVE {
            override fun cssString() = "%"
        },
        VIEWPORT_WIDTH {
            override fun cssString() = "vw"
        },
        VIEWPORT_HEIGHT {
            override fun cssString() = "vh"
        },
        VIEWPORT_MIN {
            override fun cssString() = "vmin"
        },
        VIEWPORT_HEIGHT_MIN {
            override fun cssString() = "vmax"
        };
    }

    companion object {
        val ZERO = 0.0 to Unit.PX
        val AUTO = 0.0 to Unit.AUTO
        val MIN_CONTENT = 0.0 to Unit.MIN_CONTENT
        val MAX_CONTENT = 0.0 to Unit.MAX_CONTENT
    }

    val isSet
        get() = value != null && unit != null

    constructor(propertyName: String): this(null, null, propertyName)

    fun set(pair: Pair<Double, Unit>) {
        this.value = pair.first
        this.unit = pair.second
    }

    override fun applyToStyle(style: CSSStyleDeclaration) {
        if (isSet) {
            val propValue = when (unit) {
                in listOf(Unit.AUTO, Unit.MAX_CONTENT, Unit.MIN_CONTENT) -> unit!!.cssString()
                else -> value!!.toString() + unit!!.cssString()
            }
            style.setProperty(propertyName, propValue)
        }
    }
}