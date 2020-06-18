package com.ashish.roomdemo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ashish.roomdemo.R
import com.ashish.roomdemo.adaptors.PersonAdaptor
import com.ashish.roomdemo.database.AppDatabase
import com.ashish.roomdemo.database.AppExecutors
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAdapter: PersonAdaptor
    private lateinit var mDb: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFAB.setOnClickListener {
            Log.d("MainActivity", "FAB onClick")
            startActivity(Intent(this@MainActivity, EditActivity::class.java))
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = PersonAdaptor(this)
        recyclerView.setAdapter(mAdapter)
        mDb = AppDatabase.getInstance(applicationContext)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            // Called when a user swipes left or right on a ViewHolder
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                // Here is where you'll implement swipe to delete
                AppExecutors.instance.diskIO().execute {
                    val position = viewHolder.adapterPosition
                    val tasks = mAdapter.tasks
                    mDb.personDao().delete(tasks[position])
                    retrieveTasks()
                }
            }
        }).attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        retrieveTasks()
    }

    private fun retrieveTasks() {
        AppExecutors.instance.diskIO().execute {
            val persons = mDb.personDao().loadAllPersons()
            runOnUiThread { mAdapter.tasks = persons }
        }
    }
}