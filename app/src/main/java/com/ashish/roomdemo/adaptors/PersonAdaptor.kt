package com.ashish.roomdemo.adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ashish.roomdemo.R
import com.ashish.roomdemo.activities.EditActivity
import com.ashish.roomdemo.adaptors.PersonAdaptor.MyViewHolder
import com.ashish.roomdemo.constants.Constants
import com.ashish.roomdemo.database.AppDatabase
import com.ashish.roomdemo.model.Person

class PersonAdaptor(private val context: Context) : RecyclerView.Adapter<MyViewHolder>() {
    private var mPersonList: List<Person> = listOf()
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.person_item, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {
        myViewHolder.apply {
            name.text = mPersonList[i].name
            email.text = mPersonList[i].email
            number.text = mPersonList[i].number
            pincode.text = mPersonList[i].pincode
            city.text = mPersonList[i].city
        }
    }

    override fun getItemCount(): Int {
        return mPersonList.size
    }

    var tasks: List<Person>
        // if list is null, return an empty list.
        get() = mPersonList
        set(personList) {
            mPersonList = personList
            notifyDataSetChanged()
        }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var email: TextView
        var pincode: TextView
        var number: TextView
        var city: TextView
        var editImage: ImageView
        var mDb: AppDatabase

        init {
            itemView.apply {
                name = findViewById(R.id.person_name)
                email = findViewById(R.id.person_email)
                pincode = findViewById(R.id.person_pincode)
                number = findViewById(R.id.person_number)
                city = findViewById(R.id.person_city)
                editImage = findViewById(R.id.edit_Image)
            }
            mDb = AppDatabase.getInstance(context)
            editImage.setOnClickListener {
                val elementId = mPersonList[adapterPosition].id
                val i = Intent(context, EditActivity::class.java)
                i.putExtra(Constants.UPDATE_Person_Id, elementId)
                context.startActivity(i)
            }
        }
    }

}