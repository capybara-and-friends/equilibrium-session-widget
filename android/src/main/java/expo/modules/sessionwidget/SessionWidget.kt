package expo.modules.sessionwidget

import android.content.Context
import androidx.compose.ui.unit.dp
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
import androidx.compose.ui.graphics.Color

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.currentState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.size
import androidx.glance.layout.height
import androidx.compose.ui.unit.sp

import androidx.glance.appwidget.AndroidRemoteViews
import android.widget.RemoteViews
import android.os.SystemClock
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.appwidget.action.actionStartActivity
import android.content.Intent

val sessionTypeKey = ActionParameters.Key<String>("sessionType")
val consumeActiveKey = booleanPreferencesKey("consumeActive")
val createActiveKey = booleanPreferencesKey("createActive")
val consumeSecondsKey = intPreferencesKey("consumeSeconds")
val createSecondsKey = intPreferencesKey("createSeconds")
val consumeStartedAtKey = longPreferencesKey("consumeStartedAt")
val createStartedAtKey = longPreferencesKey("createStartedAt")

class SessionWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val consumeActive = currentState(key = consumeActiveKey) ?: false
            val createActive = currentState(key = createActiveKey) ?: false
            val consumeSeconds = currentState(key = consumeSecondsKey) ?: 0
            val createSeconds = currentState(key = createSecondsKey) ?: 0
            val consumeStartedAt = currentState(key = consumeStartedAtKey) ?: 0L
            val createStartedAt = currentState(key = createStartedAtKey) ?: 0L

            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ColorProvider(Color(0xFFF8FAFC)))
                    .padding(16.dp)
                    .clickable(actionRunCallback<OpenAppAction>()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Bar
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.ic_scale),
                        contentDescription = "Equilibrium Logo",
                        modifier = GlanceModifier.size(20.dp)
                    )
                    Spacer(modifier = GlanceModifier.width(8.dp))
                    Text(
                        text = "Equilibrium",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = ColorProvider(Color(0xFF475569))
                        ),
                        modifier = GlanceModifier.defaultWeight()
                    )
                    Image(
                        provider = ImageProvider(R.drawable.ic_more_vert),
                        contentDescription = "More Options",
                        modifier = GlanceModifier.size(16.dp)
                    )
                }

                Spacer(modifier = GlanceModifier.height(12.dp))

                // Consume Timer
                Box(
                    modifier = GlanceModifier.fillMaxWidth().height(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (consumeActive) {
                        val baseTime = if (consumeStartedAt > 0L) {
                            consumeStartedAt
                        } else {
                            SystemClock.elapsedRealtime() - (consumeSeconds * 1000L)
                        }
                        val remoteViews = RemoteViews(context.packageName, R.layout.widget_chronometer_consume)
                        val format = if (consumeSeconds < 3600) "00:%s" else "%s"
                        remoteViews.setChronometer(R.id.chronometer, baseTime, format, true)
                        remoteViews.setTextColor(R.id.chronometer, android.graphics.Color.rgb(249, 115, 22))
                        AndroidRemoteViews(remoteViews = remoteViews)
                    } else {
                        Text(
                            text = formatSeconds(consumeSeconds),
                            style = TextStyle(
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                color = ColorProvider(Color(0xFFF97316))
                            )
                        )
                    }
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Create Timer
                Box(
                    modifier = GlanceModifier.fillMaxWidth().height(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (createActive) {
                        val baseTime = if (createStartedAt > 0L) {
                            createStartedAt
                        } else {
                            SystemClock.elapsedRealtime() - (createSeconds * 1000L)
                        }
                        val remoteViews = RemoteViews(context.packageName, R.layout.widget_chronometer_create)
                        val format = if (createSeconds < 3600) "00:%s" else "%s"
                        remoteViews.setChronometer(R.id.chronometer, baseTime, format, true)
                        remoteViews.setTextColor(R.id.chronometer, android.graphics.Color.rgb(34, 197, 94))
                        AndroidRemoteViews(remoteViews = remoteViews)
                    } else {
                        Text(
                            text = formatSeconds(createSeconds),
                            style = TextStyle(
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                color = ColorProvider(Color(0xFF22C55E))
                            )
                        )
                    }
                }

                Spacer(modifier = GlanceModifier.height(14.dp))

                // Control Buttons side-by-side
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        provider = ImageProvider(if (consumeActive) R.drawable.ic_stop else R.drawable.ic_play),
                        contentDescription = "Consume Toggle",
                        modifier = GlanceModifier
                            .size(38.dp)
                            .background(ColorProvider(Color(0xFFF97316)))
                            .cornerRadius(19.dp)
                            .padding(10.dp)
                            .clickable(
                                actionRunCallback<ToggleSessionAction>(
                                    actionParametersOf(sessionTypeKey to "consume")
                                )
                            )
                    )
                    Spacer(modifier = GlanceModifier.width(24.dp))
                    Image(
                        provider = ImageProvider(if (createActive) R.drawable.ic_stop else R.drawable.ic_play),
                        contentDescription = "Create Toggle",
                        modifier = GlanceModifier
                            .size(38.dp)
                            .background(ColorProvider(Color(0xFF22C55E)))
                            .cornerRadius(19.dp)
                            .padding(10.dp)
                            .clickable(
                                actionRunCallback<ToggleSessionAction>(
                                    actionParametersOf(sessionTypeKey to "create")
                                )
                            )
                    )
                }
            }
        }
    }

    private fun formatSeconds(seconds: Int): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }
}

class ToggleSessionAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val sessionType = parameters[sessionTypeKey] ?: return
        val now = SystemClock.elapsedRealtime()
        
        var newConsumeActive = false
        var newCreateActive = false
        var newConsumeSeconds = 0
        var newCreateSeconds = 0
        var newConsumeStartedAt = 0L
        var newCreateStartedAt = 0L

        updateAppWidgetState(context, glanceId) { prefs ->
            val consumeActive = prefs[consumeActiveKey] ?: false
            val createActive = prefs[createActiveKey] ?: false
            val consumeSeconds = prefs[consumeSecondsKey] ?: 0
            val createSeconds = prefs[createSecondsKey] ?: 0
            val consumeStartedAt = prefs[consumeStartedAtKey] ?: 0L
            val createStartedAt = prefs[createStartedAtKey] ?: 0L
            
            newConsumeActive = consumeActive
            newCreateActive = createActive
            newConsumeSeconds = currentSeconds(consumeSeconds, consumeStartedAt, consumeActive, now)
            newCreateSeconds = currentSeconds(createSeconds, createStartedAt, createActive, now)
            newConsumeStartedAt = if (consumeActive) now - (newConsumeSeconds * 1000L) else 0L
            newCreateStartedAt = if (createActive) now - (newCreateSeconds * 1000L) else 0L
            
            if (sessionType == "consume") {
                newConsumeActive = !consumeActive
                if (newConsumeActive) {
                    newCreateActive = false
                    newConsumeStartedAt = now - (newConsumeSeconds * 1000L)
                    newCreateStartedAt = 0L
                } else {
                    newConsumeStartedAt = 0L
                }
            } else if (sessionType == "create") {
                newCreateActive = !createActive
                if (newCreateActive) {
                    newConsumeActive = false
                    newCreateStartedAt = now - (newCreateSeconds * 1000L)
                    newConsumeStartedAt = 0L
                } else {
                    newCreateStartedAt = 0L
                }
            }
            
            prefs[consumeActiveKey] = newConsumeActive
            prefs[createActiveKey] = newCreateActive
            prefs[consumeSecondsKey] = newConsumeSeconds
            prefs[createSecondsKey] = newCreateSeconds
            prefs[consumeStartedAtKey] = newConsumeStartedAt
            prefs[createStartedAtKey] = newCreateStartedAt
        }

        // Also update SharedPreferences so JS gets the correct initial state on app boot
        val sharedPrefs = context.getSharedPreferences("SessionWidgetPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putBoolean("consumeActive", newConsumeActive)
            .putBoolean("createActive", newCreateActive)
            .putInt("consumeSeconds", newConsumeSeconds)
            .putInt("createSeconds", newCreateSeconds)
            .putLong("consumeStartedAt", newConsumeStartedAt)
            .putLong("createStartedAt", newCreateStartedAt)
            .apply()

        // Update the widget UI
        SessionWidget().update(context, glanceId)

        val toggledActive = if (sessionType == "consume") newConsumeActive else newCreateActive

        // Start the Headless JS service
        try {
            val serviceIntent = android.content.Intent(context, SessionWidgetHeadlessService::class.java).apply {
                putExtra("sessionType", sessionType)
                putExtra("isActive", toggledActive)
            }
            context.startService(serviceIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val broadcastIntent = android.content.Intent("expo.modules.sessionwidget.TOGGLE_SESSION").apply {
            setPackage(context.packageName)
            putExtra("sessionType", sessionType)
            putExtra("isActive", toggledActive)
        }
        context.sendBroadcast(broadcastIntent)
    }

    private fun currentSeconds(storedSeconds: Int, startedAt: Long, active: Boolean, now: Long): Int {
        if (!active || startedAt <= 0L) {
            return storedSeconds
        }

        return ((now - startedAt) / 1000L).toInt()
    }
}

class OpenAppAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (intent != null) {
            context.startActivity(intent)
        }
    }
}

