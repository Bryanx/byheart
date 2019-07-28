package nl.bryanderidder.byheart.shared

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun exportData(uri: Uri) {
        val extension = uri.toString().substring(uri.toString().lastIndexOf(".")+1)
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/$extension"
            putExtra(Intent.EXTRA_STREAM, uri)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        activity?.startActivity(intent)
    }
}