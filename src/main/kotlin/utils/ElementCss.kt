package utils

import org.w3c.dom.HTMLElement
import org.w3c.dom.css.CSSStyleDeclaration

typealias Dimension = Pair<Double, ElementCss.DimensionUnit>;

open class ElementCss {

    interface CssValue {
        fun value(): String = this.toString().toLowerCase()
    }

    enum class Visibility: CssValue {
        VISIBLE,
        HIDDEN,
        COLLAPSE,
        INITIAL,
        INHERIT
    }

    enum class Display: CssValue {
        None
    }

    enum class DimensionUnit: CssValue {
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
        RELATIVE {
            override fun value() = "%"
        },
        VIEWPORT_WIDTH {
            override fun value() = "vw"
        },
        VIEWPORT_HEIGHT {
            override fun value() = "vh"
        },
        VIEWPORT_MIN {
            override fun value() = "vmin"
        },
        VIEWPORT_HEIGHT_MIN {
            override fun value() = "vmax"
        }
    }

    enum class Overflow: CssValue {
        SCROLL,
        AUTO,
        HIDDEN,
        VISIBLE,
        INITIAL,
        INHERIT
    }

    enum class WhiteSpace: CssValue {
        NORMAL,
        NO_WRAP {
            override fun value() = "nowrap"
        },
        PRE,
        PRE_LINE {
            override fun value() = "pre-line"
        },
        PRE_WRAP {
            override fun value() = "pre_wrap"
        },
        INITIAL,
        INHERIT
    }

    private val css: MutableMap<String, Any?> = mutableMapOf()

    var visibility: Visibility? by css
    var display: Display? by css

    var width: Dimension? by css
    var height: Dimension? by css
    
    var marginTop: Dimension? by css
    var marginBottom: Dimension? by css
    var marginStart: Dimension? by css
    var marginEnd: Dimension? by css

    var paddingTop: Dimension? by css
    var paddingBottom: Dimension? by css
    var paddingStart: Dimension? by css
    var paddingEnd: Dimension? by css

    var overflowX: Overflow? by css
    var overflowY: Overflow? by css

    var whiteSpace: WhiteSpace? by css

    fun Dimension.toCss() = first.toString() + second.value()

    protected open val cssSetters: Map<String, (CSSStyleDeclaration) -> Unit> = mapOf(
            "visibility" to { s -> s.visibility = visibility!!.value() },
            "display" to { s -> s.display = display!!.value() },
            "width" to { s -> s.width = width!!.toCss() },
            "height" to { s -> s.height = height!!.toCss() },
            "marginTop" to { s -> s.marginTop = marginTop!!.toCss() },
            "marginBottom" to { s -> s.marginBottom = marginBottom!!.toCss() },
            "marginStart" to { s -> s.marginLeft = marginStart!!.toCss() },
            "marginEnd" to { s -> s.marginRight = marginEnd!!.toCss() },
            "paddingTop" to { s -> s.paddingTop = paddingTop!!.toCss() },
            "paddingBottom" to { s -> s.paddingBottom = paddingBottom!!.toCss() },
            "paddingStart" to { s -> s.paddingLeft = paddingStart!!.toCss() },
            "paddingEnd" to { s -> s.paddingRight = paddingEnd!!.toCss() },
            "overflowX" to { s -> s.overflowX = overflowX!!.value() },
            "overflowY" to { s -> s.overflowY = overflowY!!.value() },
            "whiteSpace" to { s -> s.whiteSpace = whiteSpace!!.value() }
    )

    fun extend(style: ElementCss, override: Boolean = false) {
        css += if (override) {
            style.css
        } else {
            (style.css - css).toMutableMap() as MutableMap<String, Any?>
        }
    }

    open fun applyTo(element: HTMLElement) {
        css.keys.forEach { attr -> cssSetters[attr]!!(element.style) }
    }
}
