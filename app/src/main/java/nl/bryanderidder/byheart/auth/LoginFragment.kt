package nl.bryanderidder.byheart.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.content_login_bottomsheet.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.BaseBottomSheet
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.SessionViewModel
import nl.bryanderidder.byheart.shared.color
import nl.bryanderidder.byheart.shared.getAttr
import nl.bryanderidder.byheart.shared.utils.goToUrl
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that pops up to login
 * @author Bryan de Ridder
 */
class LoginFragment : BaseBottomSheet() {

    private val authVM: AuthViewModel by sharedViewModel()
    private val sessionVM: SessionViewModel by sharedViewModel()
    private val pileColor get() = sessionVM.pileColor.value ?: requireContext().color(R.color.colorPrimary)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.content_login_bottomsheet, container, false)
        authVM.isTermsAndConditionsChecked.value = false
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clbDescription.text = authVM.loginMessage.value ?: ""
        clbProgressBar.setCircleColor(pileColor)
        clbTermsSwitch.switchColor = pileColor
        addEventHandlers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        authVM.onActivityResult(requireActivity(), requestCode, resultCode, data, ::onAfterLogin, ::onLoginFailed, ::onLoginCanceled)
    }

    private fun addEventHandlers() {
        clbLogin.setOnClickListener { onClickLogin() }
        clbTermsLink.setOnClickListener { goToUrl(requireActivity(), getString(R.string.terms_and_conditions_url)) }
        authVM.isTermsAndConditionsChecked.observe(this, Observer { checked ->
            clbLogin.isEnabled = checked
            clbLogin.textColor = if (checked) requireContext().getAttr(R.attr.mainHeaderTextColor) else requireContext().getAttr(R.attr.mainTextColor)
        })
        clbTermsSwitch.setOnCheckedChanged { checked ->
            authVM.isTermsAndConditionsChecked.value = checked
        }
    }

    private fun onClickLogin() {
        showProgressBar(true)
        if (authVM.isTermsAndConditionsChecked.value == true)
            authVM.signInWithGoogle(requireActivity())
    }

    @SuppressLint("StringFormatInvalid")
    private fun onAfterLogin() {
        clbProgressBar.hide()
        clbWelcomeBack.text = getString(R.string.welcome_back_name, "${authVM.currentUser?.displayName} \uD83D\uDC4B")
        clbWelcomeBack.animate().alpha(1F)
        lifecycleScope.launch {
            delay(1000L)
            requireActivity().runOnUiThread {
                dismiss()
            }
        }
    }

    private fun onLoginFailed(e: ApiException?) = showProgressBar(false)

    private fun onLoginCanceled() = showProgressBar(false)

    private fun showProgressBar(isVisible: Boolean) {
        for (it in listOf(clbTitle, clbTermsSwitch, clbTermsLink, clbLogin, clbDescription))
            it.animate().alpha(if (isVisible) 0F else 1F)
        clbProgressBar.animate().alpha(if (isVisible) 1F else 0F)
    }
}