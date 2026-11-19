package com.swe401.safeready.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.swe401.safeready.R
import com.swe401.safeready.adapters.KitAdapter
import com.swe401.safeready.data.ContentRepository
import com.swe401.safeready.data.ProgressStore

/** Interactive Go-Bag checklist exercise. */
class KitChecklistActivity : AppCompatActivity() {

    private lateinit var store: ProgressStore
    private lateinit var countText: TextView
    private lateinit var progress: LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kit)
        store = ProgressStore(this)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.title = getString(R.string.kit_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        countText = findViewById(R.id.kitCount)
        progress = findViewById(R.id.kitProgress)

        val items = ContentRepository.kitItems
        val checked = store.checkedKitItems().toMutableSet()
        val list = findViewById<RecyclerView>(R.id.kitList)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = KitAdapter(items, checked) { item, isChecked ->
            store.setKitItemChecked(item.id, isChecked)
            updateSummary(animate = true)
            if (isChecked && store.kitComponent() == 100) {
                Toast.makeText(this, "🎒 Go-Bag Ready badge unlocked!", Toast.LENGTH_LONG).show()
            }
        }
        updateSummary(animate = false)
    }

    private fun updateSummary(animate: Boolean) {
        val total = ContentRepository.kitItems.size
        val done = ContentRepository.kitItems.count { it.id in store.checkedKitItems() }
        countText.text = "$done of $total items packed"
        progress.setProgressCompat(store.kitComponent(), animate)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
