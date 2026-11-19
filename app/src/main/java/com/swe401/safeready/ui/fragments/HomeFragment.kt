package com.swe401.safeready.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.swe401.safeready.R
import com.swe401.safeready.data.ContentRepository
import com.swe401.safeready.data.ProgressStore
import com.swe401.safeready.ui.ImplicitIntents
import com.swe401.safeready.ui.KitChecklistActivity
import com.swe401.safeready.ui.MainActivity
import com.swe401.safeready.ui.Motion
import java.util.Calendar

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.actionLearn).setOnClickListener {
            (activity as? MainActivity)?.selectTab(R.id.nav_categories)
        }
        view.findViewById<View>(R.id.actionKit).setOnClickListener {
            startActivity(Intent(requireContext(), KitChecklistActivity::class.java))
        }
        view.findViewById<View>(R.id.actionProgress).setOnClickListener {
            (activity as? MainActivity)?.selectTab(R.id.nav_progress)
        }
        view.findViewById<View>(R.id.actionCall).setOnClickListener {
            ImplicitIntents.dialEmergency(requireContext())
        }

        val cards = listOf(R.id.actionLearn, R.id.actionKit, R.id.actionProgress, R.id.actionCall)
            .map { view.findViewById<View>(it) }
        cards.forEach { Motion.springPress(it) }
        if (savedInstanceState == null) Motion.staggerIn(cards, 150, 70)

        // A different tip each day of the year
        val tips = ContentRepository.dailyTips
        val day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        view.findViewById<TextView>(R.id.homeTip).text = tips[day % tips.size]
    }

    override fun onResume() {
        super.onResume()
        // Refresh every time the user comes back (e.g. after a quiz or editing the profile)
        val view = view ?: return
        val store = ProgressStore(requireContext())
        val score = store.preparednessScore()

        view.findViewById<TextView>(R.id.homeAvatar).text = store.avatar
        view.findViewById<TextView>(R.id.homeGreeting).text = "Hi, ${store.name.substringBefore(' ')}!"
        view.findViewById<TextView>(R.id.homeSchool).text = "${store.school} · ${store.form}"
        Motion.countUp(view.findViewById(R.id.homeScore), score, suffix = "/100")
        view.findViewById<TextView>(R.id.homeLevel).text = ProgressStore.levelFor(score)
        view.findViewById<LinearProgressIndicator>(R.id.homeScoreBar).setProgressCompat(score, true)
    }
}
