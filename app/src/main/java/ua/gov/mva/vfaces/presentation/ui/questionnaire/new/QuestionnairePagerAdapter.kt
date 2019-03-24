package ua.gov.mva.vfaces.presentation.ui.questionnaire.new

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ua.gov.mva.vfaces.domain.model.Block

class QuestionnairePagerAdapter(private val data: ArrayList<Block>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {



    override fun getItem(position: Int): Fragment {
        // TODO
        return QuestionnaireFragment.newInstance()
    }

    override fun getCount(): Int {
        return data.size
    }
}