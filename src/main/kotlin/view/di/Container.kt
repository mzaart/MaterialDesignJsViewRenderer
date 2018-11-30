package view.di

import view.core.renderers.*
import view.core.renderers.viewRenderers.display.ImageViewRenderer
import view.core.renderers.viewRenderers.display.TextViewRenderer
import view.core.renderers.viewRenderers.inputs.ButtonRenderer
import view.core.renderers.viewRenderers.layouts.GridLayoutRenderer
import view.core.renderers.viewRenderers.layouts.LinearLayoutRenderer
import view.core.renderers.viewRenderers.layouts.RelativeLayoutRenderer
import view.core.views.Theme
import view.core.views.View
import view.core.views.display.ImageView
import view.core.views.display.TextView
import view.core.views.input.Button
import view.core.views.layouts.GridLayout
import view.core.views.layouts.LinearLayout
import view.core.views.layouts.RelativeLayout
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

        bind<ViewRenderer<*>>("LinearLayout") with factory { v: View -> LinearLayoutRenderer(v as LinearLayout) }
        bind<ViewRenderer<*>>("LinearLayout") with factory { v: View, e: HTMLElement-> LinearLayoutRenderer(v as LinearLayout, e) }

        bind<ViewRenderer<*>>("RelativeLayout") with factory { v: View -> RelativeLayoutRenderer(v as RelativeLayout) }
        bind<ViewRenderer<*>>("RelativeLayout") with factory { v: View, e: HTMLElement-> RelativeLayoutRenderer(v as RelativeLayout, e) }

        bind<ViewRenderer<*>>("GridLayout") with factory { v: View -> GridLayoutRenderer(v as GridLayout) }
        bind<ViewRenderer<*>>("GridLayout") with factory { v: View, e: HTMLElement-> GridLayoutRenderer(v as GridLayout, e) }

        bind<ViewRenderer<*>>("Button") with factory { v: View -> ButtonRenderer(v as Button) }
        bind<ViewRenderer<*>>("Button") with factory { v: View, e: HTMLElement -> ButtonRenderer(v as Button, e) }

        bind<ViewRenderer<*>>("ImageView") with factory { v: View -> ImageViewRenderer(v as ImageView) }
        bind<ViewRenderer<*>>("ImageView") with factory { v: View, e: HTMLElement -> ImageViewRenderer(v as ImageView, e)}

        bind<ViewRenderer<*>>("TextView") with factory { v: View -> TextViewRenderer(v as TextView) }
        bind<ViewRenderer<*>>("TextView") with factory { v: View, e: HTMLElement -> TextViewRenderer(v as TextView, e) }
    }
}