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
    import com.google.firebase.database.ktx.database
    import com.google.firebase.ktx.Firebase
    import java.util.*


    // A class representing a question with 3 different options.
    class Question(val question: String, val option1: String, val option2: String, val option3: String, val answer: String)

    class quiz : AppCompatActivity() {

        private lateinit var questionTextView: TextView
        private lateinit var option1Button: Button
        private lateinit var option2Button: Button
        private lateinit var option3Button: Button
        private lateinit var resultTextView: TextView
        private lateinit var scoreTextView: TextView
        lateinit var btnBackSSFromQuiz: ImageButton
        private lateinit var nextquesButton: Button
        private lateinit var currentquesTextView: TextView

        private var currentQuestionIndex = 0
        private lateinit var currentQuestion: Question
        private lateinit var currentQuestionOptions: List<String>
        private lateinit var subjectQuestions: MutableList<Question>
        private var score = 0
        // Retrieve user's selected subject from intent
        lateinit var selectedSubject: String
        val handler = Handler(Looper.getMainLooper())

        // Create the math and science flashcards
        //TODO:finish list of questions
        private val mathQuestions = listOf(
            Question("What is 2 + 2?", "4", "3", "2", "4"),
            Question("What is the square root of 64?", "8", "32", "16", "8"),
            Question("What is the value of pi?", "3.14", "3.12", "3.11", "3.14"),
            Question("What is the difference between 18 and 7?", "11", "0", "1", "11"),
            Question("What is the product of 5 and 6?", "65", "30", "34", "30"),
            Question("What is the quotient of 20 and 4?", "0", "6", "5", "5"),
            Question("What is the value of e, the mathematical constant?", "2.71828", "3.12482", "4.18673", "2.71828"),
            Question("What is the area of a rectangle with length 10 and width 5?", "25", "15", "50", "50"),
            Question("What is the circumference of a circle with radius 4?", "24.2", "25.13", "26.78", "25.13"),
            Question("What is the sum of the first 10 positive integers?", "55", "50", "52", "55"),
            Question("What is the absolute value of -5", "5", "-5", "0", "5"),
            Question("What is the slope of a line that passes through the points (2,3) and (4,7)?", "3","2", "10", "2"),
            Question("What is the y-intercept of the line y = 2x + 3?", "3", "3x", "2x", "3"),
            Question("What is the perimeter of a triangle with sides of length 5, 7, and 9?", "315", "44", "21", "21"),
            Question("What is the derivative of 3?", "3", "1", "0", "0")
        )
        private val scienceQuestions = listOf(
            Question("What is the chemical symbol for water?", "H2O", "H", "H3O", "H2O"),
            Question("What is the largest planet in our solar system?", "Jupiter", "Earth", "Pluto", "Jupiter"),
            Question("What is the smallest particle that an element can be divided into?", "atom", "ion" ,"molecule", "atom"),
            Question("Which gas is responsible for the greenhouse effect on Earth?", "oxygen", "carbon dioxide", "nitrogen", "carbon dioxide"),
            Question("What is the name of the process by which plants convert sunlight into energy?", "combustion", "respiration", "photosynthesis", "photosynthesis"),
            Question("Which planet in our solar system has the most moons?", "Jupiter", "Mars", "Saturn", "Saturn"),
            Question("What is the name of the force that causes objects to fall towards the Earth?", "inertia", "gravity", "acceleration", "gravity"),
            Question("What is the process by which a solid changes directly into a gas called?", "sublimation", "freezing", "photosynthesis", "sublimation"),
            Question("What is the process by which a gas changes directly into a solid called?", "sublimation", "freezing", "evaporation", "sublimation"),
            Question("Which gas is necessary for all living organisms to breathe?", "carbon dioxide", "oxygen", "helium", "oxygen"),
            Question("What is the name of the process by which a cell divides into two identical daughter cells?", "mitosis", "metamorphosis", "meiosis", "mitosis"),
            Question("What is the name of the layer of gas that surrounds the Earth and protects us from the sun's harmful radiation?", "ozone layer", "ultraviolet ray layer", "milky way", "ozone layer"),
            Question("What is the name of the theory that explains the movement of the tectonic plates on the Earth's surface?", "Plate theory", "Plate Tectonics", "Tectonic Plates", "Plate Tectonics"),
            Question("What is the name of the process by which an animal changes form during its life cycle?", "Metamorphosis", "Mitosis", "Meiosis", "Metamorphosis"),
            Question("What is the name of the process by which water evaporates from the leaves of plants", "Evaporation", "Condensation", "Transpiration", "Transpiration")
        )

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_quiz)

            //region Hide toolbar

            val HSB = HideSystemBar()
            HSB.hide(window)

            //endregion

            selectedSubject = intent.getStringExtra("selected_subject") ?: "default_value"

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

            //Hides Button while answering Question
            nextquesButton.visibility = View.INVISIBLE

            // Assign value to subjectQuestions based on user's selected subject, then shuffle and take 10 questions only
            subjectQuestions = if (selectedSubject == "Math") {
                mathQuestions.shuffled().take(10).toMutableList()
            } else {
                scienceQuestions.shuffled().take(10).toMutableList()
            }

            //Back Button
            btnBackSSFromQuiz.setOnClickListener {
                finish()
            }

            // Set up initial question
            currentQuestion = subjectQuestions[currentQuestionIndex]
            currentQuestionOptions = listOf(
                currentQuestion.option1,
                currentQuestion.option2,
                currentQuestion.option3
            )

            setNextQuestion(currentQuestion)

            // Set up button click listeners
            option1Button.setOnClickListener {
                checkAnswer(option1Button.text.toString())
            }
            option2Button.setOnClickListener {
                checkAnswer(option2Button.text.toString())
            }
            option3Button.setOnClickListener {
                checkAnswer(option3Button.text.toString())
            }
        }

        @SuppressLint("SetTextI18n")
        private fun setNextQuestion(question: Question) {
            currentQuestion = question
            currentquesTextView.text = "Question: ${currentQuestionIndex + 1}/10"
            questionTextView.text = currentQuestion.question
            Log.d("TAG", currentQuestion.question)
            currentQuestionOptions = listOf(
                currentQuestion.option1,
                currentQuestion.option2,
                currentQuestion.option3
            )
            updateButtonOptions()
        }

        private fun updateButtonOptions() {
            option1Button.backgroundTintList = getColorStateList(android.R.color.holo_blue_dark)
            option2Button.backgroundTintList = getColorStateList(android.R.color.holo_blue_dark)
            option3Button.backgroundTintList = getColorStateList(android.R.color.holo_blue_dark)
            option1Button.text = currentQuestionOptions[0]
            option2Button.text = currentQuestionOptions[1]
            option3Button.text = currentQuestionOptions[2]
        }

        private fun checkAnswer(selectedAnswer: String) {

            val correctAnswer = currentQuestion.answer

            resultTextView.visibility = View.VISIBLE

            Log.d("option", "${currentQuestion.option1} ${currentQuestion.option2} ${currentQuestion.option3}")
            Log.d("optionBtnText", "${option1Button.text} ${option1Button.text} ${option1Button.text}")
            Log.d("option1", (currentQuestion.option1 == selectedAnswer).toString())
            Log.d("option2", (currentQuestion.option2 == selectedAnswer).toString())
            Log.d("option3", (currentQuestion.option3 == selectedAnswer).toString())
            Log.d("userAns", selectedAnswer)

            if (selectedAnswer == correctAnswer) {
                resultTextView.text = "Correct!"
                score++

                if(currentQuestion.option1 == selectedAnswer){
                    option1Button.backgroundTintList = getColorStateList(android.R.color.holo_green_light)
                }else if(currentQuestion.option2 == selectedAnswer){
                    option2Button.backgroundTintList = getColorStateList(android.R.color.holo_green_light)
                }else{
                    option3Button.backgroundTintList = getColorStateList(android.R.color.holo_green_light)
                }
            } else {
                resultTextView.text = "Incorrect."

                if(currentQuestion.option1 == selectedAnswer){
                    option1Button.backgroundTintList = getColorStateList(android.R.color.holo_red_light)
                }else if(currentQuestion.option2 == selectedAnswer){
                    option2Button.backgroundTintList = getColorStateList(android.R.color.holo_red_light)
                }else{
                    option3Button.backgroundTintList = getColorStateList(android.R.color.holo_red_light)
                }
            }

            option1Button.isClickable = false
            option2Button.isClickable = false
            option3Button.isClickable = false

            nextquesButton.visibility = View.VISIBLE

            // Update score and score text view
            scoreTextView.text = "Score: $score"

            if (currentQuestionIndex == 9) {
                // Quiz is over
                resultTextView.text = "Quiz complete! Your final score is $score out of 10."
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
                var Ref =  database.getReference().child("Quiz Records").child(uid)
                var Ref2 = database.getReference().child("Users").child(uid)

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

            } else {
                nextquesButton.setOnClickListener {
                    setNextQuestion(getNextQuestion())
                    nextquesButton.visibility = View.INVISIBLE
                    resultTextView.visibility = View.INVISIBLE
                }
            }

        }

        private fun getNextQuestion(): Question {
            option1Button.isClickable = true
            option2Button.isClickable = true
            option3Button.isClickable = true
            currentQuestionIndex = (currentQuestionIndex + 1) % 10
            return subjectQuestions[currentQuestionIndex]
        }
    }