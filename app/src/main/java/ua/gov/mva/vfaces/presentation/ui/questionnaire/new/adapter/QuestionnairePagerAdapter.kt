package ua.gov.mva.vfaces.presentation.ui.questionnaire.new.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ua.gov.mva.vfaces.domain.model.Block
import ua.gov.mva.vfaces.presentation.ui.questionnaire.new.QuestionnaireFragment

class QuestionnairePagerAdapter(private val data: ArrayList<Block>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val block = data[position]
        return QuestionnaireFragment.newInstance(block)
    }

    override fun getCount(): Int {
        return data.size
    }
}