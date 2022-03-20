package nl.bryanderidder.byheart.auth

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import nl.bryanderidder.byheart.BaseActivity.Companion.REQUEST_SIGN_IN
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.Preferences.KEY_USER_ID

/**
 * ViewModel that contains all auth information.
 * @author Bryan de Ridder
 */
class AuthViewModel(
    application: Application,
    private val googleSignInOptions: GoogleSignInOptions,
    private val firebaseAuth: FirebaseAuth
) : AndroidViewModel(application) {

    var isSignedIn: MutableLiveData<Boolean> = MutableLiveData()
    var loginMessage: MutableLiveData<String> = MutableLiveData()
    var isTermsAndConditionsChecked: MutableLiveData<Boolean> = MutableLiveData()

    var googleSignIn : GoogleSignInClient? = null

    val currentUser: FirebaseUser? get() = firebaseAuth.currentUser

    init {
        firebaseAuth.addAuthStateListener {
            isSignedIn.postValue(it.currentUser?.uid != null)
            Preferences.write(KEY_USER_ID, it.currentUser?.uid ?: "")
        }
    }

    fun signInWithGoogle(activity: Activity) {
        val intent = getGoogleSignIn(activity).signInIntent
        activity.startActivityForResult(intent, REQUEST_SIGN_IN)
    }

    fun signOut(activity: Activity) {
        activity.settingsProgressBar.show()
        viewModelScope.launch {
            delay(500L) // some delay to prevent spamming
            getGoogleSignIn(activity).signOut().await()
            firebaseAuth.signOut()
            activity.runOnUiThread { activity.settingsProgressBar.hide() }
        }
    }

    fun onActivityResult(
        activity: Activity,
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        onAfterLogin: () -> Unit = {},
        onFailed: (e: ApiException?) -> Unit = {},
        onCanceled: () -> Unit = {}
    ) {
        if (requestCode == REQUEST_SIGN_IN && resultCode != Activity.RESULT_OK)
            onCanceled()
        else if (requestCode == REQUEST_SIGN_IN && resultCode == Activity.RESULT_OK) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)
                handleSignInResult(account, activity, onAfterLogin)
            } catch (e: ApiException) {
                onFailed(e)
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        } else onFailed(null)
    }

    private fun handleSignInResult(account: GoogleSignInAccount?, activity: Activity, onAfterLogin: () -> Unit) = viewModelScope.launch {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential).await()
        activity.runOnUiThread { onAfterLogin() }
    }

    private fun getGoogleSignIn(activity: Activity): GoogleSignInClient {
        if (googleSignIn == null)
            googleSignIn = GoogleSignIn.getClient(activity, googleSignInOptions)
        return googleSignIn!!
    }
}