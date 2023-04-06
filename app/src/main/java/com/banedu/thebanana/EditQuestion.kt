package com.banedu.thebanana

import android.app.Activity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class EditQuestion : AppCompatActivity() {
    lateinit var btnDoneEditQuestion: ImageButton
    lateinit var btn_return: ImageButton

    lateinit var edtQuestion: EditText
    lateinit var edtCorrectAnswer: EditText
    lateinit var edtWrongAnswer1: EditText
    lateinit var edtWrongAnswer2: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_question)

        btn_return = findViewById(R.id.btn_return)
        btnDoneEditQuestion = findViewById(R.id.btnDoneEditQuestion)
        edtQuestion = findViewById(R.id.edtQuestion)
        edtCorrectAnswer = findViewById(R.id.edtCorrectAnswer)
        edtWrongAnswer1 = findViewById(R.id.edtWrongAnswer1)
        edtWrongAnswer2 = findViewById(R.id.edtWrongAnswer2)

        val resultIntent = Intent()

        val questionRecord = intent.getParcelableExtra<QuestionRecordFormat>("questionRecord")
        if (questionRecord != null){
            edtQuestion.setText(questionRecord.question)
            edtCorrectAnswer.setText(questionRecord.correctAns)
            edtWrongAnswer1.setText(questionRecord.wrongAns1)
            edtWrongAnswer2.setText(questionRecord.wrongAns2)
        }

        btn_return.setOnClickListener{
            finish()
        }

        btnDoneEditQuestion.setOnClickListener{
            if(edtQuestion.text.isEmpty() or edtCorrectAnswer.text.isEmpty() or edtWrongAnswer1.text.isEmpty() or edtWrongAnswer2.text.isEmpty()
            ){
                Toast.makeText(this, "Please fill in all the text field", Toast.LENGTH_LONG).show()
            }else{
                val questionRecord = QuestionRecordFormat(
                    edtQuestion.text.toString(),
                    edtCorrectAnswer.text.toString(),
                    edtWrongAnswer1.text.toString(),
                    edtWrongAnswer2.text.toString()
                )
                resultIntent.putExtra("QuestionRecordFormat", questionRecord)
                setResult(Activity.RESULT_OK, resultIntent)

                finish()
            }
        }
    }
}
