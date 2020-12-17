package nl.bryanderidder.byheart.card.share

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_pile_share_bottomsheet.*
import kotlinx.coroutines.*
import nl.bryanderidder.byheart.BaseBottomSheet
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.SessionViewModel
import nl.bryanderidder.byheart.shared.setBrightness
import nl.bryanderidder.byheart.shared.firestore.FireStoreViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that pops up to share a pile
 * @author Bryan de Ridder
 */
class ShareFragment : BaseBottomSheet() {

    private val pileVM: PileViewModel by sharedViewModel()
    private val sessionVM: SessionViewModel by sharedViewModel()
    private val cardVM: CardViewModel by sharedViewModel()
    private val fireStoreVM: FireStoreViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_pile_share_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pile = getSessionPile()
        val pileIsPublic = pile?.remoteId?.isNotEmpty() ?: false
        cpsSwitch.isChecked = pileIsPublic
        cpsShare.alpha = if (pileIsPublic) 1F else 0F
        cpsSwitch.text = if (pileIsPublic) getString(R.string.stack_is_public) else getString(R.string.make_stack_public)
        showShareLink(pileIsPublic)
        addEventHandlers(pile)
        sessionVM.pileColor.value?.let { setColors(it) }
    }

    private fun addEventHandlers(pile: Pile?) {
        cpsSwitch.setOnCheckedChanged { checked -> if (checked) onMakePublic() else onRemovePublic() }
        cpsShare.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            share.putExtra(Intent.EXTRA_TEXT, "https://bryanderidder.nl/byheart/${pile?.remoteId}")
            startActivity(Intent.createChooser(share, "Share link!"))
        }
    }

    private fun onMakePublic() {
        showProgressBar(true)
        lifecycleScope.launch {
            delay(2000L)
            val pileId = sessionVM.pileId.value ?: NO_ID
            val cards = cardVM.getCards(pileId)
            pileVM.getPile(pileId)?.let {
                val id = fireStoreVM.insertPileAsync(it, cards).await()
                pileVM.update(it.apply { remoteId = id })
            }
            activity?.runOnUiThread {
                cpsSwitch.text = getString(R.string.stack_is_public)
                showProgressBar(false)
                showShareLink(true)
            }
        }
    }

    private fun onRemovePublic() {
        showProgressBar(true)
        lifecycleScope.launch {
            delay(500L)
            getSessionPile()?.let {
                fireStoreVM.deleteAsync(it.remoteId).await()
                pileVM.update(it.apply { remoteId = "" })
            }
            activity?.runOnUiThread {
                cpsSwitch.text = getString(R.string.make_stack_public)
                showProgressBar(false)
                showShareLink(false)
            }
        }
    }

    private fun showProgressBar(show: Boolean) {
        cpsProgressBar.animate().alpha(if (show) 1F else 0F)
        listOf(cpsDescription, cpsSwitch, cpsTitle, cpsShare).forEach {
            it.animate().alpha(if (show) 0F else 1F)
            it.isEnabled = !show
        }
    }

    private fun showShareLink(show: Boolean) {
        cpsShare.animate().alpha(if (show) 1F else 0F)
        cpsShare.isEnabled = show
    }

    private fun getSessionPile(): Pile? {
        val pileId = sessionVM.pileId.value ?: NO_ID
        return pileVM.getPile(pileId)
    }

    private fun setColors(pileColor: Int) {
        cpsSwitch.switchColor = pileColor
        cpsProgressBar.setCircleColor(pileColor)
        if (Preferences.DARK_MODE)
            cpsShare.textColor = pileColor
        else
            cpsShare.textColor = pileColor.setBrightness(0.55F)
    }
}