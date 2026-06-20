package expo.modules.sessionwidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition

class SessionWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val prefs = context.getSharedPreferences("SessionWidgetPrefs", Context.MODE_PRIVATE)
            val isActive = prefs.getBoolean("isActive", false)

            Column(
                modifier = GlanceModifier.fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isActive) "Session is Active" else "No Session",
                )
                Button(
                    text = if (isActive) "Stop" else "Start",
                    onClick = actionRunCallback<ToggleSessionAction>()
                )
            }
        }
    }
}

class ToggleSessionAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        // Here we send a broadcast that the Expo Module receiver will catch
        val intent = android.content.Intent("expo.modules.sessionwidget.TOGGLE_SESSION")
        intent.setPackage(context.packageName)
        context.sendBroadcast(intent)
    }
}
