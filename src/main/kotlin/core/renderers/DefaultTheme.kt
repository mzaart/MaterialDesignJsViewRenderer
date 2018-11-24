package core.renderers

import core.renderers.viewRenderers.inputs.ButtonRenderer
import core.views.Theme
import core.views.View
import core.views.display.TextView
import core.views.input.Button
import utils.mapBased.keys.HasKeys

/**
 * The theme that is applied to views by default.
 *
 * Note that the Them wont modify view properties that already have a value, it will just set a value if the view property
 * is not initialized.
 *
 * This theme edits view, before they are rendered, as follows:
 *  * Adds margin to views. See the properties [horizontalMargin] and [verticalMargin] for more information.
 *  * Configures button views. See the properties [buttonRipple], [buttonRaised] and [buttonAccentColor] for more information.
 *  * Sets Roboto fonts for [TextView]s.
 */
open class DefaultTheme: Theme() {

    /**
     * Specifies the horizontal margin for views in pixels.
      */
    open val horizontalMargin: Double = 16.0

    /**
     * Specifies the vertical margin for views in pixels.
     */
    open val verticalMargin: Double = 16.0

    /**
     * If true, a ripple animation when be played whenever a button is pressed.
      */
    open val buttonRipple = true

    /**
     * If true, buttons will be raised by default.
     */
    open val buttonRaised = true

    /**
     * If true, buttons will have the Material theme's accent color.
     */
    open val buttonAccentColor = ButtonRenderer.ButtonConfig.Color.ACCENT

    private val viewPreprocessor = { view: Any ->
        if (view !is View) {
            throwExcp()
        } else {
            if (view.marginStart == 0.0) view.marginStart = horizontalMargin
            if (view.marginEnd == 0.0) view.marginEnd = horizontalMargin
            if (view.marginTop == 0.0) view.marginTop = verticalMargin
            if (view.marginBottom == 0.0) view.marginBottom = verticalMargin
        }
    }

    private val buttonPreprocessor = { button: Any ->
        if (button !is Button) {
            throwExcp()
        } else {
            viewPreprocessor(button)
            if (button.webExtras == null) {
                button.webExtras = HasKeys(mutableMapOf<String, Any?>())
            }
            val keys = button.webExtras!!.keys
            keys.setIfEmpty("ripple", buttonRipple)
            keys.setIfEmpty("raise", buttonRaised)
            keys.setIfEmpty("accentColor", buttonAccentColor)
        }
    }

    private val textViewPreprocessor = { textView: Any ->
        if (textView !is TextView) {
            throwExcp()
        } else {
            viewPreprocessor(textView)
            if (textView.font == null) {
                textView.font = "Roboto"
            }
        }
    }

    init {
        // register preprocessors
        register(View::class, viewPreprocessor)
        register(Button::class, buttonPreprocessor)
        register(TextView::class, textViewPreprocessor)
    }

    protected fun <K, V> MutableMap<K, V>.setIfEmpty(k: K, v: V) {
        if (this[k] == null) {
            this[k] = v
        }
    }

    protected fun throwExcp() {
        throw IllegalStateException("Preprocessor does not correspond to the object passed")
    }
}