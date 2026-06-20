package expo.modules.sessionwidget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import androidx.glance.appwidget.updateAll
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SessionWidgetModule : Module() {
  private val PREFS_NAME = "SessionWidgetPrefs"
  private val ACTION_TOGGLE_SESSION = "expo.modules.sessionwidget.TOGGLE_SESSION"
  
  private val receiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      if (intent?.action == ACTION_TOGGLE_SESSION) {
        // Toggle state in SharedPreferences
        context?.let { ctx ->
          val prefs = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
          val isActive = prefs.getBoolean("isActive", false)
          prefs.edit().putBoolean("isActive", !isActive).apply()
          
          // Emit event to JS
          this@SessionWidgetModule.sendEvent("onSessionToggled", mapOf(
            "isActive" to !isActive
          ))
          
          // Update the widget UI
          CoroutineScope(Dispatchers.Main).launch {
            SessionWidget().updateAll(ctx)
          }
        }
      }
    }
  }

  override fun definition() = ModuleDefinition {
    Name("SessionWidget")

    Events("onSessionToggled")

    OnCreate {
      val context = appContext.reactContext ?: return@OnCreate
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.registerReceiver(receiver, IntentFilter(ACTION_TOGGLE_SESSION), Context.RECEIVER_EXPORTED)
      } else {
        context.registerReceiver(receiver, IntentFilter(ACTION_TOGGLE_SESSION))
      }
    }

    OnDestroy {
      val context = appContext.reactContext ?: return@OnDestroy
      try {
        context.unregisterReceiver(receiver)
      } catch (e: Exception) {
        // Ignore
      }
    }

    Function("setSessionState") { isActive: Boolean ->
      val context = appContext.reactContext ?: return@Function
      val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
      prefs.edit().putBoolean("isActive", isActive).apply()
      
      CoroutineScope(Dispatchers.Main).launch {
        SessionWidget().updateAll(context)
      }
    }

    Function("getSessionState") { ->
      val context = appContext.reactContext ?: return@Function false
      val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
      return@Function prefs.getBoolean("isActive", false)
    }
  }
}
