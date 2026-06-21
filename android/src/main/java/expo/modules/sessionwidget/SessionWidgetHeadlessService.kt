package expo.modules.sessionwidget

import android.content.Intent
import com.facebook.react.HeadlessJsTaskService
import com.facebook.react.bridge.Arguments
import com.facebook.react.jstasks.HeadlessJsTaskConfig

class SessionWidgetHeadlessService : HeadlessJsTaskService() {
    override fun getTaskConfig(intent: Intent?): HeadlessJsTaskConfig? {
        val extras = intent?.extras
        val sessionType = extras?.getString("sessionType") ?: return null
        val isActive = extras.getBoolean("isActive")
        
        val data = Arguments.createMap().apply {
            putString("sessionType", sessionType)
            putBoolean("isActive", isActive)
        }
        
        return HeadlessJsTaskConfig(
            "SessionWidgetToggleTask",
            data,
            5000, // Timeout in ms
            true  // Allowed in foreground
        )
    }
}
