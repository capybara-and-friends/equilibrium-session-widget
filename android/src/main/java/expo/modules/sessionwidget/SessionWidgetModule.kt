package expo.modules.sessionwidget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.SystemClock
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SessionWidgetModule : Module() {
  private val PREFS_NAME = "SessionWidgetPrefs"
  private val ACTION_TOGGLE_SESSION = "expo.modules.sessionwidget.TOGGLE_SESSION"
  private var toggleReceiver: BroadcastReceiver? = null

  override fun definition() = ModuleDefinition {
    Name("SessionWidget")

    Events("onSessionToggled")

    OnCreate {
      val context = appContext.reactContext ?: return@OnCreate
      val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
          val type = intent.getStringExtra("sessionType") ?: return
          val isActive = intent.getBooleanExtra("isActive", false)
          sendEvent("onSessionToggled", mapOf("type" to type, "isActive" to isActive))
        }
      }
      toggleReceiver = receiver

      val filter = IntentFilter(ACTION_TOGGLE_SESSION)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
      } else {
        context.registerReceiver(receiver, filter)
      }
    }

    OnDestroy {
      val context = appContext.reactContext ?: return@OnDestroy
      toggleReceiver?.let { receiver ->
        try {
          context.unregisterReceiver(receiver)
        } catch (_: IllegalArgumentException) {
        }
      }
      toggleReceiver = null
    }

    Function("setSessionState") { consumeActive: Boolean, createActive: Boolean, consumeSeconds: Double, createSeconds: Double ->
      val context = appContext.reactContext ?: return@Function
      val now = SystemClock.elapsedRealtime()
      val consumeStartedAt = if (consumeActive) now - (consumeSeconds.toLong() * 1000L) else 0L
      val createStartedAt = if (createActive) now - (createSeconds.toLong() * 1000L) else 0L
      val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
      prefs.edit()
          .putBoolean("consumeActive", consumeActive)
          .putBoolean("createActive", createActive)
          .putInt("consumeSeconds", consumeSeconds.toInt())
          .putInt("createSeconds", createSeconds.toInt())
          .putLong("consumeStartedAt", consumeStartedAt)
          .putLong("createStartedAt", createStartedAt)
          .apply()

      CoroutineScope(Dispatchers.Main).launch {
        GlanceAppWidgetManager(context).getGlanceIds(SessionWidget::class.java).forEach { glanceId ->
            updateAppWidgetState(context, glanceId) { state ->
                state[booleanPreferencesKey("consumeActive")] = consumeActive
                state[booleanPreferencesKey("createActive")] = createActive
                state[intPreferencesKey("consumeSeconds")] = consumeSeconds.toInt()
                state[intPreferencesKey("createSeconds")] = createSeconds.toInt()
                state[longPreferencesKey("consumeStartedAt")] = consumeStartedAt
                state[longPreferencesKey("createStartedAt")] = createStartedAt
            }
            SessionWidget().update(context, glanceId)
        }
      }
    }

    Function("getSessionState") { ->
      val context = appContext.reactContext ?: return@Function mapOf("consumeActive" to false, "createActive" to false, "consumeSeconds" to 0, "createSeconds" to 0)
      val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
      val now = SystemClock.elapsedRealtime()
      val consumeActive = prefs.getBoolean("consumeActive", false)
      val createActive = prefs.getBoolean("createActive", false)
      val consumeSeconds = getCurrentSeconds(
          prefs.getInt("consumeSeconds", 0),
          prefs.getLong("consumeStartedAt", 0L),
          consumeActive,
          now
      )
      val createSeconds = getCurrentSeconds(
          prefs.getInt("createSeconds", 0),
          prefs.getLong("createStartedAt", 0L),
          createActive,
          now
      )
      return@Function mapOf(
          "consumeActive" to consumeActive,
          "createActive" to createActive,
          "consumeSeconds" to consumeSeconds,
          "createSeconds" to createSeconds
      )
    }
  }

  private fun getCurrentSeconds(storedSeconds: Int, startedAt: Long, active: Boolean, now: Long): Int {
    if (!active || startedAt <= 0L) {
      return storedSeconds
    }

    return ((now - startedAt) / 1000L).toInt()
  }
}
