package com.swe401.safeready.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swe401.safeready.R
import com.swe401.safeready.adapters.CategoryAdapter
import com.swe401.safeready.data.ContentRepository
import com.swe401.safeready.data.ProgressStore
import com.swe401.safeready.ui.LessonActivity

/** Disaster category selection shown as a RecyclerView list. */
class CategoriesFragment : Fragment(R.layout.fragment_categories) {

    private lateinit var adapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CategoryAdapter(ContentRepository.categories, ProgressStore(requireContext())) { category ->
            // Explicit intent carrying the chosen category id
            val intent = Intent(requireContext(), LessonActivity::class.java)
                .putExtra(LessonActivity.EXTRA_CATEGORY_ID, category.id)
            startActivity(intent)
        }

        val list = view.findViewById<RecyclerView>(R.id.categoryList)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.refresh() // show updated scores after returning from a lesson or exercise
    }
}
