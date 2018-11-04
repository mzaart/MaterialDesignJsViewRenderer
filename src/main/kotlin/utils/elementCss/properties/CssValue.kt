package utils.elementCss.properties

interface CssValue {

    fun cssString() = this.toString().toLowerCase().replace('_', '-')
}