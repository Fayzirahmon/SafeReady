package com.swe401.safeready.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.swe401.safeready.R
import com.swe401.safeready.adapters.ProgressAdapter
import com.swe401.safeready.data.ContentRepository
import com.swe401.safeready.data.ProgressStore
import com.swe401.safeready.ui.BarChartView
import com.swe401.safeready.ui.Motion

/** Preparedness score analysis: overall ring, breakdown bars, bar chart and advice. */
class ProgressFragment : Fragment(R.layout.fragment_progress) {

    private lateinit var adapter: ProgressAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProgressAdapter(ContentRepository.categories, ProgressStore(requireContext()))
        val list = view.findViewById<RecyclerView>(R.id.progressList)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val view = view ?: return
        val store = ProgressStore(requireContext())
        val score = store.preparednessScore()

        view.findViewById<CircularProgressIndicator>(R.id.scoreRing).setProgressCompat(score, true)
        Motion.countUp(view.findViewById(R.id.scoreText), score)
        view.findViewById<TextView>(R.id.levelText).text = ProgressStore.levelFor(score)

        setRow(view, R.id.quizValue, R.id.quizBar, store.quizComponent())
        setRow(view, R.id.lessonsValue, R.id.lessonsBar, store.lessonComponent())
        setRow(view, R.id.kitValue, R.id.kitBar, store.kitComponent())

        // Bar chart of the best exercise score per disaster
        val categories = ContentRepository.categories
        view.findViewById<BarChartView>(R.id.barChart).setData(
            categories.map { shortLabel(it.id, it.name) },
            categories.map { store.bestScore(it.id).coerceAtLeast(0) },
            categories.map { ContextCompat.getColor(requireContext(), it.colorRes) }
        )

        adapter.refresh()
        view.findViewById<TextView>(R.id.adviceText).text = buildAdvice(store)
    }

    private fun shortLabel(id: String, name: String): String = when (id) {
        "earthquake" -> "Quake"
        "landslide" -> "Slide"
        "storm" -> "Storm"
        else -> name
    }

    private fun setRow(root: View, valueId: Int, barId: Int, value: Int) {
        root.findViewById<TextView>(valueId).text = "$value%"
        root.findViewById<LinearProgressIndicator>(barId).setProgressCompat(value, true)
    }

    /** Simple analysis that points the student to their weakest area. */
    private fun buildAdvice(store: ProgressStore): String {
        val categories = ContentRepository.categories
        val notTried = categories.filter { store.bestScore(it.id) < 0 }
        val weakest = categories.filter { store.bestScore(it.id) >= 0 }.minByOrNull { store.bestScore(it.id) }
        val notLearned = categories.filter { !store.isLessonComplete(it.id) }

        return when {
            notTried.size == categories.size ->
                "Start with the ${categories.first().name} lesson and try its safety exercise."
            notTried.isNotEmpty() ->
                "You have not tried the ${notTried.first().name} exercise yet. Give it a go!"
            weakest != null && store.bestScore(weakest.id) < ProgressStore.MASTERY_SCORE ->
                "Your weakest area is ${weakest.name} (${store.bestScore(weakest.id)}%). Re-read the lesson and try again."
            notLearned.isNotEmpty() ->
                "Mark the ${notLearned.first().name} lesson as learned to raise your score."
            store.kitComponent() < 100 ->
                "Finish packing your Go-Bag to complete your preparedness."
            else -> "Excellent! You are fully prepared. Share what you learned with your family."
        }
    }
}
