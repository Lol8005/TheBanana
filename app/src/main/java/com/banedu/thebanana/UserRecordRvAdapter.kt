package com.banedu.thebanana

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class UserRecordRvAdapter(
    private val userRecord: ArrayList<UserRecordFormat>
) : RecyclerView.Adapter<UserRecordRvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.user_record_rv_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if ((userRecord != null) and (userRecord.size > 0)) {
            holder.urIndex.text = (userRecord.get(position).rid.toString())
            holder.urSubject.text = (userRecord.get(position).subject)
            holder.urQDate.text = (userRecord.get(position).date)
            holder.urBanana.text = (userRecord.get(position).bananaEarned.toString())
        }
    }

    override fun getItemCount(): Int {
        return userRecord.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val urIndex: TextView = itemView.findViewById(R.id.idTVIndex)
        val urSubject: TextView = itemView.findViewById(R.id.idTVSubject)
        val urQDate: TextView = itemView.findViewById(R.id.idTVQdate)
        val urBanana: TextView = itemView.findViewById(R.id.idTVBananaEarned)
    }
}
