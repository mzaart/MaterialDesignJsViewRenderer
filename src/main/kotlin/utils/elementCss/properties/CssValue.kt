package utils.elementCss.properties

/**
 * Represents a value of a CSS property.
 */
interface CssValue {

    /**
     *  This method is commonly used in enums representing values It facilitates the conversion from enum value
     *  to css string value. However, for more specific cases, overriding this method is necessary.
     *
     *  @return A string representing the CSS value.
     */
    fun cssString() = this.toString().toLowerCase().replace('_', '-')
}