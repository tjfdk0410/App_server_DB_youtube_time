package com.example.madcamp_2nd.local_app.tab1_local

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_2nd.R
import kotlinx.android.synthetic.main.activity_main.*

class MainRvAdapter(val context: Context, val itemList: ArrayList<Item>): RecyclerView.Adapter<MainRvAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userName = itemView.findViewById<TextView>(R.id.user_name)
        val phoneNumber = itemView. findViewById<TextView>(R.id.phone_number)

        fun bind(item: Item) {
            userName?.text = item.userName
            phoneNumber?.text = item.phoneNumber
        }
    }
}