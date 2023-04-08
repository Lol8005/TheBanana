package com.banedu.thebanana

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class TopicRecordRVAdapter(
    private val topicRecord: ArrayList<TopicRecordFormat>
) : RecyclerView.Adapter<TopicRecordRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.question_rv,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if ((topicRecord != null) and (topicRecord.size > 0)) {
            holder.topic.text = (topicRecord.get(position).topicName)
        }

        holder.btnRemove.setOnClickListener { v ->
            val removedRecord = topicRecord.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)

            val auth = Firebase.auth
            val DB_Reference = DB_Connection().connectRealDB()
            DB_Reference.child("Topic").child(auth.uid.toString()).child(removedRecord.topicID)
                .removeValue()

            Log.d("remove", removedRecord.topicID)
        }

        holder.btnEdit.setOnClickListener { v ->
//            val removedRecord = topicRecord.removeAt(holder.adapterPosition)
//            notifyItemRemoved(holder.adapterPosition)

            val topicRecordFormat = topicRecord[holder.adapterPosition]

            val intent = Intent(v.context, EditTopic::class.java).apply {
                putExtra("topicRecord", topicRecordFormat)
            }

            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return topicRecord.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topic: TextView = itemView.findViewById(R.id.txt)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btnRemove)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
    }
}