package com.banedu.thebanana

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.util.Log
import android.view.View
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


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

    private var score = 0
    var currentQuestionIndex = 0

    // Retrieve user's selected subject from intent
    lateinit var selectedSubject: String
    lateinit var selectedSubjectAuthorUID: String


//    private val mathQuestions = listOf(
//        Question("What is 2 + 2?", "4", "3", "2", "4"),
//        Question("What is the square root of 64?", "8", "32", "16", "8"),
//        Question("What is the value of pi?", "3.14", "3.12", "3.11", "3.14"),
//        Question("What is the difference between 18 and 7?", "11", "0", "1", "11"),
//        Question("What is the product of 5 and 6?", "65", "30", "34", "30"),
//        Question("What is the quotient of 20 and 4?", "0", "6", "5", "5"),
//        Question("What is the value of e, the mathematical constant?", "2.71828", "3.12482", "4.18673", "2.71828"),
//        Question("What is the area of a rectangle with length 10 and width 5?", "25", "15", "50", "50"),
//        Question("What is the circumference of a circle with radius 4?", "24.2", "25.13", "26.78", "25.13"),
//        Question("What is the sum of the first 10 positive integers?", "55", "50", "52", "55"),
//        Question("What is the absolute value of -5", "5", "-5", "0", "5"),
//        Question("What is the slope of a line that passes through the points (2,3) and (4,7)?", "3","2", "10", "2"),
//        Question("What is the y-intercept of the line y = 2x + 3?", "3", "3x", "2x", "3"),
//        Question("What is the perimeter of a triangle with sides of length 5, 7, and 9?", "315", "44", "21", "21"),
//        Question("What is the derivative of 3?", "3", "1", "0", "0")
//    )
//    private val scienceQuestions = listOf(
//        Question("What is the chemical symbol for water?", "H2O", "H", "H3O", "H2O"),
//        Question("What is the largest planet in our solar system?", "Jupiter", "Earth", "Pluto", "Jupiter"),
//        Question("What is the smallest particle that an element can be divided into?", "atom", "ion" ,"molecule", "atom"),
//        Question("Which gas is responsible for the greenhouse effect on Earth?", "oxygen", "carbon dioxide", "nitrogen", "carbon dioxide"),
//        Question("What is the name of the process by which plants convert sunlight into energy?", "combustion", "respiration", "photosynthesis", "photosynthesis"),
//        Question("Which planet in our solar system has the most moons?", "Jupiter", "Mars", "Saturn", "Saturn"),
//        Question("What is the name of the force that causes objects to fall towards the Earth?", "inertia", "gravity", "acceleration", "gravity"),
//        Question("What is the process by which a solid changes directly into a gas called?", "sublimation", "freezing", "photosynthesis", "sublimation"),
//        Question("What is the process by which a gas changes directly into a solid called?", "sublimation", "freezing", "evaporation", "sublimation"),
//        Question("Which gas is necessary for all living organisms to breathe?", "carbon dioxide", "oxygen", "helium", "oxygen"),
//        Question("What is the name of the process by which a cell divides into two identical daughter cells?", "mitosis", "metamorphosis", "meiosis", "mitosis"),
//        Question("What is the name of the layer of gas that surrounds the Earth and protects us from the sun's harmful radiation?", "ozone layer", "ultraviolet ray layer", "milky way", "ozone layer"),
//        Question("What is the name of the theory that explains the movement of the tectonic plates on the Earth's surface?", "Plate theory", "Plate Tectonics", "Tectonic Plates", "Plate Tectonics"),
//        Question("What is the name of the process by which an animal changes form during its life cycle?", "Metamorphosis", "Mitosis", "Meiosis", "Metamorphosis"),
//        Question("What is the name of the process by which water evaporates from the leaves of plants", "Evaporation", "Condensation", "Transpiration", "Transpiration")
//    )

    private lateinit var auth: FirebaseAuth
    private lateinit var DB_Reference: DatabaseReference

    private var SubjectQuestion = ArrayList<question>()

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

        DB_Reference.child("Topic").child(selectedSubjectAuthorUID).child(selectedSubject).get().addOnSuccessListener {
            for (questionIndex in it.children){

                val _answer = answer(
                    questionIndex.child("correctAns").value.toString(),
                    questionIndex.child("wrongAns1").value.toString(),
                    questionIndex.child("wrongAns2").value.toString()
                )
                ShuffleAnswerPos(_answer, questionIndex.child("question").value.toString())
            }

            updateQuestion()
            Log.d("question", SubjectQuestion.toString())
        }

        nextquesButton.visibility = View.INVISIBLE

        option1Button.setOnClickListener{
            checkAnswer(0)
        }

        option2Button.setOnClickListener{
            checkAnswer(1)
        }

        option3Button.setOnClickListener{
            checkAnswer(2)
        }

        nextquesButton.setOnClickListener{
            nextQuestion()
        }
    }

    fun updateQuestion(){
        currentquesTextView.text = "${currentQuestionIndex + 1} / ${SubjectQuestion.size - 1}"
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

    fun checkAnswer(pos: Int){
        option1Button.isClickable = false
        option2Button.isClickable = false
        option3Button.isClickable = false

        if(pos == SubjectQuestion[currentQuestionIndex].ansPos){
            //correct
            resultTextView.text = "Correct!"
            score++

            if(pos == 0){
                option1Button.backgroundTintList = getColorStateList(android.R.color.holo_green_light)
            }else if(pos == 1){
                option2Button.backgroundTintList = getColorStateList(android.R.color.holo_green_light)
            }else{
                option3Button.backgroundTintList = getColorStateList(android.R.color.holo_green_light)
            }
        }else{
            //wrong
            resultTextView.text = "Incorrect."

            if(pos == 0){
                option1Button.backgroundTintList = getColorStateList(android.R.color.holo_red_light)
            }else if(pos == 1){
                option2Button.backgroundTintList = getColorStateList(android.R.color.holo_red_light)
            }else{
                option3Button.backgroundTintList = getColorStateList(android.R.color.holo_red_light)
            }
        }

        scoreTextView.text = "Score: $score"

        if(currentQuestionIndex + 1 != SubjectQuestion.size - 1){
            nextquesButton.visibility = View.VISIBLE
        }else{
            //end of question
            resultTextView.text = "Quiz complete! Your final score is $score out of ${SubjectQuestion.size}."
            option1Button.isEnabled = false
            option2Button.isEnabled = false
            option3Button.isEnabled = false
            nextquesButton.visibility = View.INVISIBLE


            //Updating score to database
            val database = Firebase.database
            val uid = (FirebaseAuth.getInstance().currentUser?.uid).toString()
            val currentDate = Calendar.getInstance().time

            // format the current date as a string using the ISO date format
            val qdate = SimpleDateFormat("yyyy-MM-dd").format(currentDate)

            val subject = selectedSubject
            val banana_earned = score
            var Ref =  database.reference.child("Quiz Records").child(uid)
            var Ref2 = database.reference.child("Users").child(uid)

            Ref.get().addOnSuccessListener {
                var index = it.childrenCount.toInt() + 1
                Ref.child(index.toString()).child("Banana_Earned").setValue(banana_earned)
                Ref.child(index.toString()).child("Qdate").setValue(qdate)
                Ref.child(index.toString()).child("Subject").setValue(subject)
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
                startActivity(intent)
                finish()
            }, 3000)
        }
    }

    fun nextQuestion(){
        currentQuestionIndex++

        updateQuestion()
    }

    private fun ShuffleAnswerPos(ansList: answer, _question: String){
        var correctAns = ansList.option1

        var _tempList = listOf(
            ansList.option1,
            ansList.option2,
            ansList.option3
        ).shuffled()

        var ansPos = 0
        for(index in 0.._tempList.size - 1){
            if(_tempList[index] == correctAns){
                ansPos = index
                break
            }
        }

        val ShuffledAnsList = answer(_tempList[0], _tempList[1], _tempList[2])
        val newQuestion = question(_question, ShuffledAnsList, ansPos)

        SubjectQuestion.add(newQuestion)
    }
}

private data class answer(var option1: String, var  option2: String, var  option3: String)
private data class question(var question: String, var option: answer, var ansPos: Int)