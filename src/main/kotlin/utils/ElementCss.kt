package utils

import org.w3c.dom.HTMLElement
import org.w3c.dom.css.CSSStyleDeclaration
import utils.extensions.nonNull

open class ElementCss {

    interface CssValue {
        fun value(): String = this.toString().replace('_', '-').toLowerCase()
    }

    enum class Position: CssValue {
        STATIC,
        RELATIVE,
        FIXED,
        ABSOLUTE,
        STICKY
    }

    enum class Visibility: CssValue {
        VISIBLE,
        HIDDEN,
        COLLAPSE,
        INITIAL,
        INHERIT
    }

    enum class Display: CssValue {
        None,
        BLOCK,
        FLEX
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

    class Dimension(val value: Double, val unit: Unit = Unit.PX) {

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

        companion object {
            val ZERO = Dimension(0.0, Unit.PX)
            val AUTO = Dimension(0.0, Unit.AUTO)
            val MIN_CONTENT = Dimension(0.0, Unit.MIN_CONTENT)
            val MAX_CONTENT = Dimension(0.0, Unit.MAX_CONTENT)
        }

        fun toCss(): String {
            return if (this in listOf(AUTO, MIN_CONTENT, MAX_CONTENT)) unit.value()
            else value.toString() + unit.value()
        }
    }

    enum class FlexItemAlignSelf: CssValue {

        CENTER,
        FLEX_START,
        FLEX_END,
        BASELINE,
        STRETCH
    }

    class Transformation {

        private var translate = arrayOf(Dimension.ZERO, Dimension.ZERO)

        fun translateX(value: Dimension) {
            translate(value, translate[1])
        }

        fun translateY(value: Dimension) {
            translate(translate[0], value)
        }

        fun translate(x: Dimension, y: Dimension) {
            translate[0] = x
            translate[1] = y
        }

        fun toCss(): String? {
            if (translate[0] == Dimension.ZERO && translate[1] == Dimension.ZERO) {
                return null
            }
            return "translate(${translate[0].toCss()},${translate[1].toCss()})"
        }
    }

    private val css: MutableMap<String, Any?> = mutableMapOf()

    var visibility: Visibility? by css
    var display: Display? by css

    var width: Dimension? by css
    var height: Dimension? by css

    var position: Position? by css

    var top: Dimension? by css
    var bottom: Dimension? by css
    var start: Dimension? by css
    var end: Dimension? by css
    
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

    var horizontalFlexDirection: Boolean? by css
    var alignSelf: FlexItemAlignSelf? by css

    var transformation: Transformation by css

    protected open val cssSetters: Map<String, (CSSStyleDeclaration) -> Unit> = mapOf(
            "visibility" to { s -> s.visibility = visibility!!.value() },
            "display" to { s -> s.display = display!!.value() },
            "width" to { s -> s.width = width!!.toCss() },
            "height" to { s -> s.height = height!!.toCss() },
            "position" to { s -> s.position = position!!.value() },
            "top" to { s -> s.top = top!!.toCss() },
            "bottom" to { s -> s.bottom = bottom!!.toCss() },
            "start" to { s -> s.left = start!!.toCss() },
            "end" to { s -> s.right = end!!.toCss() },
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
            "whiteSpace" to { s -> s.whiteSpace = whiteSpace!!.value() },
            "horizontalFlexDirection" to { s -> s.flexDirection = if (horizontalFlexDirection!!) "row" else "column"},
            "alignSelf" to { s -> s.alignSelf = alignSelf!!.value() },
            "transformation" to { s -> transformation.toCss().nonNull { s.transform = it } }
    )

    init {
        transformation = Transformation()
    }

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
