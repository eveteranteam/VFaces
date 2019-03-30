package ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ua.gov.mva.vfaces.domain.model.Block
import ua.gov.mva.vfaces.presentation.ui.questionnaire.new.QuestionnaireFragment

class QuestionnairePagerAdapter(private val data: ArrayList<Block>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    /**
     * Currently visible to user Fragment.
     */
    var currentFragment: QuestionnaireFragment? = null

    override fun getItem(position: Int): Fragment {
        val block = data[position]
        return QuestionnaireFragment.newInstance(block)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (currentFragment !== `object`) {
            currentFragment = `object` as QuestionnaireFragment
        }
        super.setPrimaryItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return data.size
    }
}