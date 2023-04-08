package com.banedu.thebanana

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView


class QuestionRecordRVAdapter(
    private val questionRecord: ArrayList<QuestionRecordFormat>,
    private val listener: questionRecordListener,
    private val resultLauncher: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<QuestionRecordRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.question_rv,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if ((questionRecord != null) and (questionRecord.size > 0)) {
            holder.question.text = (questionRecord.get(position).question)
        }

        holder.btnRemove.setOnClickListener { v ->
            val removedRecord = questionRecord.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            listener.returnQuestionRecord(removedRecord)
        }

        holder.btnEdit.setOnClickListener { v ->
            val questionRecordFormat = questionRecord[holder.adapterPosition]
            val intent = Intent(v.context, EditQuestion::class.java).apply {
                putExtra("questionRecord", questionRecordFormat)
            }

            resultLauncher.launch(intent)

            val removedRecord = questionRecord.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            listener.returnQuestionRecord(removedRecord)
        }
    }

    override fun getItemCount(): Int {
        return questionRecord.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question: TextView = itemView.findViewById(R.id.txt)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btnRemove)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
    }
}

interface questionRecordListener {
    fun returnQuestionRecord(record: QuestionRecordFormat)
}