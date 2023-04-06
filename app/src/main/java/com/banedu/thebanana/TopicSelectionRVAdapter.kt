package com.banedu.thebanana

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class TopicSelectionRVAdapter(
    private val topicRecord: ArrayList<TopicRecordFormat>
) : RecyclerView.Adapter<TopicSelectionRVAdapter.ViewHolder>(){

    var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.question_selection_rv,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(topicRecord.size > 0){
            holder.topicRadio.text = topicRecord[holder.adapterPosition].topicName
            holder.topicRadio.id = topicRecord[holder.adapterPosition].topicID.hashCode()
        }

        // Set the checked state of the RadioButton
        holder.topicRadio.isChecked = selectedPosition == holder.adapterPosition

        holder.topicRadio.setOnClickListener {
            if (selectedPosition != holder.adapterPosition) {
                // Uncheck all other RadioButtons
                notifyItemChanged(selectedPosition)
                selectedPosition = holder.adapterPosition

                // Check the clicked RadioButton
                notifyItemChanged(holder.adapterPosition)
            }
        }
    }

    override fun getItemCount(): Int {
        return topicRecord.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topicRadio: RadioButton = itemView.findViewById(R.id.radio)
    }

    fun getSelectedItemId(): String {
        if (selectedPosition != -1){
            return topicRecord[selectedPosition].topicID
        }else{
            return ""
        }
    }

    fun getSelectedItemAuthor(): String {
        if (selectedPosition != -1){
            return topicRecord[selectedPosition].authorUID
        }else{
            return ""
        }
    }
}