package nl.bryanderidder.byheart.settings

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * ViewModel that contains all auth information.
 * @author Bryan de Ridder
 */
const val RC_SIGN_IN = 1

class AuthViewModel(
    application: Application,
    private val googleSignInOptions: GoogleSignInOptions,
    private val firebaseAuth: FirebaseAuth
) : AndroidViewModel(application) {

    var isSignedIn: MutableLiveData<Boolean> = MutableLiveData()

    var googleSignIn : GoogleSignInClient? = null

    init {
        firebaseAuth.addAuthStateListener {
            isSignedIn.postValue(it.currentUser?.uid != null)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun signInWithGoogle(activity: Activity) {
        activity.settingsProgressBar.show()
        val intent = getGoogleSignIn(activity).signInIntent
        activity.startActivityForResult(intent, RC_SIGN_IN)
    }

    fun signOut(activity: Activity) {
        activity.settingsProgressBar.show()
        viewModelScope.launch {
            delay(500L)
            getGoogleSignIn(activity).signOut().await()
            firebaseAuth.signOut()
            activity.runOnUiThread { activity.settingsProgressBar.hide() }
        }
    }

    fun onActivityResult(activity: Activity, requestCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task, activity)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>, activity: Activity) = viewModelScope.launch {
        val account = completedTask.getResult(ApiException::class.java) ?: return@launch
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).await()
        activity.runOnUiThread { activity.settingsProgressBar.hide() }
    }

    private fun getGoogleSignIn(activity: Activity): GoogleSignInClient {
        if (googleSignIn == null)
            googleSignIn = GoogleSignIn.getClient(activity, googleSignInOptions)
        return googleSignIn!!
    }
}