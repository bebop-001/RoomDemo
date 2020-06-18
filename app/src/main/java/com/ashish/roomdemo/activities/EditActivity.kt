package com.ashish.roomdemo.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ashish.roomdemo.R
import com.ashish.roomdemo.constants.Constants
import com.ashish.roomdemo.database.AppDatabase
import com.ashish.roomdemo.database.AppExecutors
import com.ashish.roomdemo.model.Person
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {
    private var mPersonId = 0
    private lateinit var editIntent: Intent
    private lateinit var mDb: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        edit_button.setOnClickListener({ onSaveButtonClicked() })
        mDb = AppDatabase.getInstance(applicationContext)
        editIntent = intent
        if (editIntent.hasExtra(Constants.UPDATE_Person_Id)) {
            edit_button!!.text = getString(R.string.update_btn_text)
            mPersonId = editIntent.getIntExtra(Constants.UPDATE_Person_Id, -1)
            AppExecutors.instance.diskIO().execute {
                val person = mDb.personDao().loadPersonById(mPersonId)
                populateUI(person)
            }
        }
    }

    private fun populateUI(person: Person) {
        edit_name!!.setText(person.name)
        edit_email!!.setText(person.email)
        edit_phone_number!!.setText(person.number)
        edit_pincode!!.setText(person.pincode)
        edit_city!!.setText(person.city)
    }

    private fun onSaveButtonClicked() {
        val person = Person(
                edit_name!!.text.toString(),
                edit_email!!.text.toString(),
                edit_phone_number!!.text.toString(),
                edit_pincode!!.text.toString(),
                edit_city!!.text.toString())
        AppExecutors.instance.diskIO().execute {
            if (!editIntent.hasExtra(Constants.UPDATE_Person_Id)) {
                mDb.personDao().insertPerson(person)
            } else {
                person.id = mPersonId
                mDb.personDao().updatePerson(person)
            }
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}