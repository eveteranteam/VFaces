package ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ua.gov.mva.vfaces.domain.model.Questionnaire
import ua.gov.mva.vfaces.presentation.ui.questionnaire.new.QuestionnaireFragment

class QuestionnairePagerAdapter(private val data: Questionnaire, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    /**
     * Currently visible to user Fragment.
     */
    var currentFragment: QuestionnaireFragment? = null

    override fun getItem(position: Int): Fragment {
        return QuestionnaireFragment.newInstance(data, position)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (currentFragment !== `object`) {
            currentFragment = `object` as QuestionnaireFragment
        }
        super.setPrimaryItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return data.blocks!!.size
    }
}