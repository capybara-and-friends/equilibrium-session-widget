package expo.modules.sessionwidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.ButtonColors
import androidx.glance.ButtonDefaults
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
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.Spacer
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.text.FontWeight
import androidx.glance.unit.ColorProvider
import android.graphics.Color

val sessionTypeKey = ActionParameters.Key<String>("sessionType")

class SessionWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val prefs = context.getSharedPreferences("SessionWidgetPrefs", Context.MODE_PRIVATE)
            val consumeActive = prefs.getBoolean("consumeActive", false)
            val createActive = prefs.getBoolean("createActive", false)

            Column(
                modifier = GlanceModifier.fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "The Balance",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                
                Row(
                    modifier = GlanceModifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        text = if (consumeActive) "Stop" else "Start",
                        onClick = actionRunCallback<ToggleSessionAction>(
                            actionParametersOf(sessionTypeKey to "consume")
                        ),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = ColorProvider(Color.parseColor("#EAB308")), // Yellow-500
                            contentColor = ColorProvider(Color.WHITE)
                        )
                    )
                    Spacer(modifier = GlanceModifier.width(8.dp))
                    Button(
                        text = if (createActive) "Stop" else "Start",
                        onClick = actionRunCallback<ToggleSessionAction>(
                            actionParametersOf(sessionTypeKey to "create")
                        ),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = ColorProvider(Color.parseColor("#22C55E")), // Green-500
                            contentColor = ColorProvider(Color.WHITE)
                        )
                    )
                }
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
        val sessionType = parameters[sessionTypeKey] ?: return
        val intent = android.content.Intent("expo.modules.sessionwidget.TOGGLE_SESSION")
        intent.putExtra("sessionType", sessionType)
        intent.setPackage(context.packageName)
        context.sendBroadcast(intent)
    }
}
