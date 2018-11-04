package utils.elementCss.properties

class Number(val number: Double): CssValue {

    constructor(value: Int): this(value.toDouble())

    companion object {
        val AUTO = Number(Double.MAX_VALUE)
        val INITIAL = Number(Double.MAX_VALUE - 1)
        val INHERIT =  Number(Double.MAX_VALUE - 2)
    }

    override fun cssString(): String {
        return when (number) {
            AUTO.number -> "auto"
            INITIAL.number -> "initial"
            INHERIT.number -> "inherit"
            else -> number.toString()
        }
    }
}