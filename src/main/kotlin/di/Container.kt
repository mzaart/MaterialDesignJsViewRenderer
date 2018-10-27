package di

import core.renderers.*
import core.renderers.viewRenderers.inputs.ButtonRenderer
import core.renderers.viewRenderers.layouts.LinearLayoutRenderer
import core.views.Theme
import core.views.View
import core.views.input.Button
import core.views.layouts.LinearLayout
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.factory
import org.kodein.di.erased.singleton
import org.w3c.dom.HTMLElement

object Container {

    val kodein = Kodein {

        bind<RendererConfig>() with singleton { RendererConfig() }
        bind<Theme>() with singleton { DefaultTheme() }

        bind<ViewTreeRenderer>() with singleton { MaterialDesignJsRenderer }

        bind<ViewRenderer<*>>("Button") with factory { v: View -> ButtonRenderer(v as Button) }
        bind<ViewRenderer<*>>("LinearLayout") with factory { v: View -> LinearLayoutRenderer(v as LinearLayout) }

        bind<ViewRenderer<*>>("Button") with factory { v: View, e: HTMLElement -> ButtonRenderer(v as Button, e) }
        bind<ViewRenderer<*>>("LinearLayout") with factory { v: View, e: HTMLElement-> LinearLayoutRenderer(v as LinearLayout, e) }
    }
}