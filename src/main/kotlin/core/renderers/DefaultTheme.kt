package core.renderers

import core.renderers.viewRenderers.inputs.ButtonRenderer
import core.views.Theme
import core.views.View
import core.views.display.TextView
import core.views.input.Button
import utils.mapBased.keys.HasKeys

open class DefaultTheme: Theme() {

    // view settings
    open val horizontalMargin: Double = 16.0
    open val verticalMargin: Double = 16.0

    // button settings
    open val buttonRipple = true
    open val buttonRaised = true
    open val buttonAccentColor = ButtonRenderer.ButtonConfig.Color.ACCENT

    private val viewPreprocessor = { view: Any ->
        if (view !is View) {
            throwExcp()
        } else {
            view.marginStart = horizontalMargin
            view.marginEnd = horizontalMargin
            view.marginTop = verticalMargin
            view.marginBottom = verticalMargin
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