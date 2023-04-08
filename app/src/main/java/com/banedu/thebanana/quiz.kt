package com.banedu.thebanana

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*


// A class representing a question with 3 different options.

class quiz : AppCompatActivity() {

    lateinit var questionTextView: TextView
    lateinit var option1Button: Button
    lateinit var option2Button: Button
    lateinit var option3Button: Button
    lateinit var resultTextView: TextView
    lateinit var scoreTextView: TextView
    lateinit var btnBackSSFromQuiz: ImageButton
    lateinit var nextquesButton: Button
    lateinit var currentquesTextView: TextView
    lateinit var btnBackHomeFromSS: ImageButton

    private var score = 0
    var currentQuestionIndex = 0

    // Retrieve user's selected subject from intent
    lateinit var selectedSubject: String
    lateinit var selectedSubjectAuthorUID: String

    private lateinit var auth: FirebaseAuth
    private lateinit var DB_Reference: DatabaseReference

    private var SubjectQuestion = ArrayList<question>()
    var topicName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

        auth = Firebase.auth
        DB_Reference = DB_Connection().connectRealDB()

        selectedSubject = intent.getStringExtra("selected_subject") ?: ""
        selectedSubjectAuthorUID = intent.getStringExtra("authorUID") ?: ""


        // Initialize Views
        questionTextView = findViewById(R.id.questionTextView)
        option1Button = findViewById(R.id.option1Button)
        option2Button = findViewById(R.id.option2Button)
        option3Button = findViewById(R.id.option3Button)
        resultTextView = findViewById(R.id.resultTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        btnBackSSFromQuiz = findViewById(R.id.btnBackHomeFromSS)
        nextquesButton = findViewById(R.id.nextquesButton)
        currentquesTextView = findViewById(R.id.currentquesTextView)

        btnBackHomeFromSS = findViewById(R.id.btnBackHomeFromSS)

        DB_Reference.child("Topic").child(selectedSubjectAuthorUID).child(selectedSubject).get()
            .addOnSuccessListener {
                for (questionIndex in it.children) {
                    if (questionIndex.key != "TopicName") {
                        val _answer = answer(
                            questionIndex.child("correctAns").value.toString(),
                            questionIndex.child("wrongAns1").value.toString(),
                            questionIndex.child("wrongAns2").value.toString()
                        )
                        ShuffleAnswerPos(_answer, questionIndex.child("question").value.toString())
                    }
                }

                SubjectQuestion = SubjectQuestion.shuffled() as ArrayList<question>
                updateQuestion()
            }

        nextquesButton.visibility = View.INVISIBLE

        option1Button.setOnClickListener {
            checkAnswer(0)
        }

        option2Button.setOnClickListener {
            checkAnswer(1)
        }

        option3Button.setOnClickListener {
            checkAnswer(2)
        }

        nextquesButton.setOnClickListener {
            nextQuestion()
        }

        btnBackHomeFromSS.setOnClickListener {
            finish()
        }
    }

    fun updateQuestion() {
        currentquesTextView.text = "Question: ${currentQuestionIndex + 1} / ${SubjectQuestion.size}"
        questionTextView.text = SubjectQuestion[currentQuestionIndex].question
        option1Button.text = SubjectQuestion[currentQuestionIndex].option.option1
        option2Button.text = SubjectQuestion[currentQuestionIndex].option.option2
        option3Button.text = SubjectQuestion[currentQuestionIndex].option.option3

        option1Button.backgroundTintList = getColorStateList(android.R.color.holo_blue_dark)
        option2Button.backgroundTintList = getColorStateList(android.R.color.holo_blue_dark)
        option3Button.backgroundTintList = getColorStateList(android.R.color.holo_blue_dark)

        nextquesButton.visibility = View.INVISIBLE

        resultTextView.text = ""

        option1Button.isClickable = true
        option2Button.isClickable = true
        option3Button.isClickable = true
    }

    fun checkAnswer(pos: Int) {
        option1Button.isClickable = false
        option2Button.isClickable = false
        option3Button.isClickable = false

        if (pos == SubjectQuestion[currentQuestionIndex].ansPos) {
            //correct
            resultTextView.text = "Correct!"
            resultTextView.setTextColor(getColorStateList(android.R.color.holo_green_light))
            score++

            if (pos == 0) {
                option1Button.backgroundTintList =
                    getColorStateList(android.R.color.holo_green_light)
            } else if (pos == 1) {
                option2Button.backgroundTintList =
                    getColorStateList(android.R.color.holo_green_light)
            } else {
                option3Button.backgroundTintList =
                    getColorStateList(android.R.color.holo_green_light)
            }
        } else {
            //wrong
            resultTextView.text = "Incorrect."
            resultTextView.setTextColor(getColorStateList(android.R.color.holo_red_light))

            if (pos == 0) {
                option1Button.backgroundTintList = getColorStateList(android.R.color.holo_red_light)
            } else if (pos == 1) {
                option2Button.backgroundTintList = getColorStateList(android.R.color.holo_red_light)
            } else {
                option3Button.backgroundTintList = getColorStateList(android.R.color.holo_red_light)
            }
        }

        scoreTextView.text = "Score: $score"

        if (currentQuestionIndex + 1 != SubjectQuestion.size) {
            nextquesButton.visibility = View.VISIBLE
        } else {
            //end of question
            resultTextView.text =
                "Quiz complete! Your final score is $score out of ${SubjectQuestion.size}."
            option1Button.isEnabled = false
            option2Button.isEnabled = false
            option3Button.isEnabled = false
            nextquesButton.visibility = View.INVISIBLE

            resultTextView.setTextColor(getColorStateList(android.R.color.black))

            var SLD = SaveLoadData()
            SLD.LoadData(this)

            Log.d("role", SLD.role)

            if (SLD.role == "student") {
                //Updating score to database
                val database = Firebase.database
                val uid = (FirebaseAuth.getInstance().currentUser?.uid).toString()
                val currentDate = Calendar.getInstance().time

                // format the current date as a string using the ISO date format
                val qdate = SimpleDateFormat("yyyy-MM-dd").format(currentDate)

                val banana_earned = score
                var Ref = database.reference.child("Quiz Records").child(uid)
                var Ref2 = database.reference.child("Users").child(uid)

                Ref.get().addOnSuccessListener {
                    var index = it.childrenCount.toInt() + 1
                    Ref.child(index.toString()).child("Banana_Earned").setValue(banana_earned)
                    Ref.child(index.toString()).child("Qdate").setValue(qdate)
                    Ref.child(index.toString()).child("Subject").setValue(selectedSubject)
                }

                Ref2.child("Total_Banana_Earned").get().addOnSuccessListener {
                    var total = it.value.toString().toInt() + banana_earned
                    Ref2.child("Total_Banana_Earned").setValue(total)
                }

                // handler to delay the transition to the results page by 3 seconds
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    val intent = Intent(this, results::class.java)
                    intent.putExtra("score", score)
                    intent.putExtra("totalQuestion", SubjectQuestion.size)
                    startActivity(intent)
                    finish()
                }, 3000)
            } else {
                finish()
            }

        }
    }

    fun nextQuestion() {
        currentQuestionIndex++

        updateQuestion()
    }

    private fun ShuffleAnswerPos(ansList: answer, _question: String) {
        var correctAns = ansList.option1

        var _tempList = listOf(
            ansList.option1,
            ansList.option2,
            ansList.option3
        ).shuffled()

        var ansPos = 0
        for (index in 0.._tempList.size - 1) {
            if (_tempList[index] == correctAns) {
                ansPos = index
                break
            }
        }

        val ShuffledAnsList = answer(_tempList[0], _tempList[1], _tempList[2])
        val newQuestion = question(_question, ShuffledAnsList, ansPos)

        SubjectQuestion.add(newQuestion)
    }
}

private data class answer(var option1: String, var option2: String, var option3: String)
private data class question(var question: String, var option: answer, var ansPos: Int)