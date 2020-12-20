package nl.bryanderidder.byheart.rehearsal.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_rehearsal_result.*
import nl.bryanderidder.byheart.BaseBottomSheet
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.rehearsal.RehearsalViewModel
import nl.bryanderidder.byheart.shared.IOnBackPressed
import nl.bryanderidder.byheart.shared.SessionViewModel
import nl.bryanderidder.byheart.shared.firestore.FireStoreViewModel
import nl.bryanderidder.byheart.shared.setDrawable
import nl.bryanderidder.byheart.shared.startFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that pops up to share a pile
 * @author Bryan de Ridder
 */
class RehearsalResultFragment : BaseBottomSheet(), IOnBackPressed {

    private val pileVM: PileViewModel by sharedViewModel()
    private val sessionVM: SessionViewModel by sharedViewModel()
    private val cardVM: CardViewModel by sharedViewModel()
    private val fireStoreVM: FireStoreViewModel by sharedViewModel()
    private val rehearsalViewModel: RehearsalViewModel by sharedViewModel()
    private val pileId get() = sessionVM.pileId.value ?: NO_ID

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_rehearsal_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crrScore.text = rehearsalViewModel.score
        crrScorePercent.text = "${rehearsalViewModel.percentageCorrect}%"
        crrElapsedTime.text = rehearsalViewModel.elapsedTime
        crrMedal.setDrawable(rehearsalViewModel.getMedal())
        crrTitle.text = rehearsalViewModel.getTitle(context!!)
    }

    override fun onBackPressed(): Boolean = startFragment(CardFragment()).run { true }
}