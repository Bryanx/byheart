package nl.bryanderidder.byheart.shared.utils

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


/**
 * Utils for sharing/creating screenshots
 * @author Bryan de Ridder
 */
object ScreenShotUtil {

    private fun takeScreenshot(view: View): Bitmap? {
        view.isDrawingCacheEnabled = true
        return view.drawingCache
    }

    private fun saveBitmap(activity: Activity, bitmap: Bitmap): File {
        val image = File.createTempFile("${System.currentTimeMillis()}", ".png", activity.cacheDir)
        FileOutputStream(image)
            .use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        return image
    }

    fun createAndShare(activity: Activity, view: View = activity.window.decorView.findViewById<View>(android.R.id.content)) {
        val bitmap = takeScreenshot(view)
        val file = saveBitmap(activity, bitmap!!)
        val uri = FileProvider.getUriForFile(activity, activity.packageName, file)
        IoUtils.exportData(activity, uri, "image")
    }
}