package com.swe401.safeready.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.swe401.safeready.R
import com.swe401.safeready.adapters.BadgeAdapter
import com.swe401.safeready.data.ProgressStore
import com.swe401.safeready.ui.ImplicitIntents
import com.swe401.safeready.ui.Motion
import java.text.DateFormat
import java.util.Date

/** Safety Champion award system: certificate card plus a grid of badges. */
class AwardsFragment : Fragment(R.layout.fragment_awards) {

    private val adapter = BadgeAdapter()
    private var championShown = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val grid = view.findViewById<RecyclerView>(R.id.badgeGrid)
        grid.layoutManager = GridLayoutManager(requireContext(), 2)
        grid.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val view = view ?: return
        val store = ProgressStore(requireContext())
        val badges = store.badges()
        adapter.submit(badges)

        val unlocked = badges.count { it.unlocked }
        view.findViewById<TextView>(R.id.badgeCount).text =
            getString(R.string.awards_badges) + " ($unlocked/${badges.size})"

        val score = store.preparednessScore()
        val champion = store.isSafetyChampion()
        val title = view.findViewById<TextView>(R.id.championTitle)
        val text = view.findViewById<TextView>(R.id.championText)
        val progress = view.findViewById<LinearProgressIndicator>(R.id.championProgress)
        val shareButton = view.findViewById<Button>(R.id.shareCertificateButton)

        progress.setProgressCompat(score.coerceAtMost(ProgressStore.CHAMPION_SCORE), true)
        view.findViewById<View>(R.id.championEmoji).alpha = if (champion) 1f else 0.35f

        if (champion) {
            if (!championShown) {
                // Animate and attach the spring once, not every time the tab resumes
                championShown = true
                Motion.popIn(view.findViewById(R.id.championEmoji), 150)
                Motion.springPress(shareButton)
            }
            val date = DateFormat.getDateInstance(DateFormat.LONG).format(Date())
            title.text = "Certificate of Safety Champion"
            text.text = "Awarded to ${store.name} of ${store.school} for reaching a preparedness score of $score.\n$date"
            shareButton.visibility = View.VISIBLE
            shareButton.setOnClickListener {
                ImplicitIntents.shareText(
                    requireContext(),
                    "🏆 I am now a SafeReady Safety Champion with a preparedness score of $score/100! " +
                            "I know what to do in floods, fires, earthquakes and more."
                )
            }
        } else {
            title.text = "Safety Champion"
            text.text = getString(R.string.awards_champion_locked) + "\nYour score: $score/100"
            shareButton.visibility = View.GONE
        }
    }
}
