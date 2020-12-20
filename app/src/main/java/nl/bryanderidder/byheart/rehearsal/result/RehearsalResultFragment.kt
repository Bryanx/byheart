package nl.bryanderidder.byheart.rehearsal.result

import android.os.Bundle
import android.view.*
import kotlinx.android.synthetic.main.content_rehearsal_result.*
import nl.bryanderidder.byheart.BaseBottomSheet
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.rehearsal.RehearsalViewModel
import nl.bryanderidder.byheart.rehearsal.setup.RehearsalSetupFragment
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.utils.ScreenShotUtil
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that is shown upon finishing a rehearsal
 * @author Bryan de Ridder
 */
class RehearsalResultFragment : BaseBottomSheet(), IOnBackPressed {

    private val rehearsalVM: RehearsalViewModel by sharedViewModel()
    private val sessionVM: SessionViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout =  inflater.inflate(R.layout.content_rehearsal_result, container, false)
        addToolbar(title = "")
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crrScore.text = rehearsalVM.score
        crrScorePercent.text = "${rehearsalVM.percentageCorrect}%"
        crrElapsedTime.text = rehearsalVM.elapsedTime
        crrMedal.setDrawable(rehearsalVM.getMedal())
        crrTitle.text = rehearsalVM.getTitle(context!!)
        crrPileName.text = sessionVM.pileName.value ?: ""
        crrTryAgain.textColor = sessionVM.pileColor.value ?: context!!.color(R.color.colorPrimary)
        rehearsalVM.cancelTimer()
        addEventHandlers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rehearsal_result_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_share_screenshot -> true.apply { ScreenShotUtil.createAndShare(activity!!) }
        else -> super.onOptionsItemSelected(item)
    }

    private fun addEventHandlers() {
        crrTryAgain.setOnClickListener {
            RehearsalSetupFragment().also { it.show(activity!!.supportFragmentManager, it.tag) }
        }
    }

    override fun onBackPressed(): Boolean = startFragment(CardFragment()).run { true }
}