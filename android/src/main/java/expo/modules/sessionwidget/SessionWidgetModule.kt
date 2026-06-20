package expo.modules.sessionwidget

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class SessionWidgetModule : Module() {
  override fun definition() = ModuleDefinition {
    Name("SessionWidget")

    Events("onChange")

    Function("hello") {
      "Hello world! 👋"
    }
  }
}
