package com.banedu.thebanana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlin.random.Random

// A class representing a flashcard with a question and an answer.
class Flashcard(val question: String, val answer: String)

class FlashcardClass : AppCompatActivity() {

    //     representing a flashcard activity with a spinner to choose the subject, a question text view,
    //     an answer button, a next button, a shuffle button, and a list of current flashcards.
    private lateinit var flashcardSpinner: Spinner
    private lateinit var questionTextView: TextView
    private lateinit var answerButton: Button
    private lateinit var nextButton: Button
    private lateinit var shuffleButton: Button
    private lateinit var buttonGoToSecondActivity: Button

    //    The list of flashcards for the current subject. Defaults to an empty list.
    private var currentFlashcards: List<Flashcard> = emptyList()
    //    The index of the current flashcard in the list of flashcards. Defaults to 0.
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)

        //region Hide toolbar

        val HSB = HideSystemBar()
        HSB.hide(window)

        //endregion

//        button to next page
        buttonGoToSecondActivity = findViewById(R.id.buttonGoToSecondActivity)
        buttonGoToSecondActivity.setOnClickListener {
            val intent = Intent(this, subjectselection::class.java)
            startActivity(intent)
        }

        // Initialize views
        flashcardSpinner = findViewById(R.id.flashcardSpinner)
        questionTextView = findViewById(R.id.questionTxt)
        answerButton = findViewById(R.id.answerButton)
        nextButton = findViewById(R.id.nextButton)
        shuffleButton = findViewById(R.id.shuffleButton)

        // Create the math and science flashcards
        val mathFlashcards = listOf(
            Flashcard("What is 2 + 2?", "4"),
            Flashcard("What is the square root of 64?", "8"),
            Flashcard("What is the value of pi?", "3.14"),
            Flashcard("What is the difference between 18 and 7?", "11"),
            Flashcard("What is the product of 5 and 6?", "30"),
            Flashcard("What is the quotient of 20 and 4", "5"),
            Flashcard("What is the value of e, tha mathematical constant?", "2.71828"),
            Flashcard("What is the area of a rectangle with length 10 and width 5?", "50"),
            Flashcard("What is the circumference of a circle with radius 4?", "25.13"),
            Flashcard("What is the sum of the first 10 positive integers?", "55"),
            Flashcard("What is teh absolute value of -5", "5"),
            Flashcard("What is the slope of a line that passes through the points (2,3) and (4,7)?", "2"),
            Flashcard("What is the perimeter of a triangle with sides of length 5, 7, and 9?", "21"),
            Flashcard("What is the derivative of 3?", "0"),
            Flashcard("What is the y-intercept of the line y= 2x +3?", "3")
        )
        val scienceFlashcards = listOf(
            Flashcard("What is the chemical symbol for water?", "H2O"),
            Flashcard("What is the largest planet in our solar system?", "Jupiter"),
            Flashcard("What is the smallest particle that an element can be divided into?", "Atom"),
            Flashcard("Which gas is responsible for the greenhouse effect on Earth?", "Carbon dioxide"),
            Flashcard("What is the name of the process by which plants convert sunlight into energy?", "Photosynthesis"),
            Flashcard("Which planet in our solar system has the most moons?", "Saturn"),
            Flashcard("What is the name of the force that causes objects to fall towards the Earth", "Gravity"),
            Flashcard("What is the process by which a solid changes directly into a gas called?", "Sublimation"),
            Flashcard("What is the process by which a gas changes directly into a solid called?", "Sublimation"),
            Flashcard("Which gas is necessary for all living organisms to breathe?", "Oxygen"),
            Flashcard("What is the name of the process by which a  cell divides into two identical daughter cells?", "Mitosis"),
            Flashcard("What is the name of the layer of gas that surrounds the Earth and protects us from the sun's harmful radiation?", "Ozone layer"),
            Flashcard("What is the name of the theory that explains the movement of the tectonic plates on the Earth's surface", "Plate Tectonics"),
            Flashcard("What is the name of the process by which an animal changes form during its life cycle?", "Metamorphosis"),
            Flashcard("What is the name of the process by which water evaporates from the leaves of plants?", "Transpiration")
        )

        // Creates a list of options for the flashcard subject spinner
        val flashcardOptions = listOf("Math", "Science")
        // Create an ArrayAdapter to display the options in the spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, flashcardOptions)
        // Sets the ArrayAdapter as the adapter for the flashcard subject spinner
        flashcardSpinner.adapter = adapter
        // Sets an item selected listener for the flashcard subject spinner
        flashcardSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            // Defines behavior for when an item is selected in the spinner
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // When a subject is selected from the spinner, set the current flashcards and reset the index
                if (position == 0) {
                    currentFlashcards = mathFlashcards
                } else {
                    currentFlashcards = scienceFlashcards
                }
                currentIndex = 0
                showCurrentFlashcard()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Set up the answer button
        answerButton.setOnClickListener {
            val currentFlashcard = currentFlashcards[currentIndex]
            questionTextView.text = currentFlashcard.answer
        }

        // Set up the next button
        nextButton.setOnClickListener {
            // Increase the index by 1, or reset it to 0 if it goes beyond the end of the list
            currentIndex = (currentIndex + 1) % currentFlashcards.size
            showCurrentFlashcard()
        }

        // Set up the shuffle button
        shuffleButton.setOnClickListener {
            // Shuffle the list of flashcards using the random function
            currentFlashcards = currentFlashcards.sortedBy { Random.nextInt() }
            currentIndex = 0
            showCurrentFlashcard()
        }
    }

    private fun showCurrentFlashcard() {
        // Show the question for the current flashcard
        val currentFlashcard = currentFlashcards[currentIndex]
        questionTextView.text = currentFlashcard.question
    }
}