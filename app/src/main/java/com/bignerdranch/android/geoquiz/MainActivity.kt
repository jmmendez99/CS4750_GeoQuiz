package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.CorrectionInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0 //can't use anymore b/c of deprecation

class MainActivity : AppCompatActivity() {

    //These are lateinit because we promise to set them to
    //a non-null value before using them
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    //Add a lazily initialized property to stash the QuizViewModel
    //instance associated with the activity
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    //Life cycle function that starts the activity and initializes everything
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        //Check if currentIndex variable is in the Bundle
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        //Getting the resource id's for the buttons in activity_main.xml
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)

        //Setting the listeners for true and false button clicks.
        //This is a lambda expression: -> is the syntax.
        //The toast class shows a popup for correct/incorrect answers.
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener() {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            //Start Cheat activity and also is a button click listener
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            resultLauncher.launch(intent)
            //startActivityForResult(intent, REQUEST_CODE_CHEAT) ---deprecated
        }

        updateQuestion()
    }

    //pulling the value out of the result sent back from CheatActivity
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            }
        }
//      ------This part of the code didnt work because of a deprecation------
//    override fun onActivityResult(requestCode: Int,
//                                  resultCode: Int,
//                                  data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode != Activity.RESULT_OK) {
//            return
//        }
//
//        if(requestCode == REQUEST_CODE_CHEAT) {
//            quizViewModel.isCheater =
//                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
//        }
//    }

    //More life cycle functions with logs attached to them
    override fun onStart() {
        super.onStart()
        Log.d(TAG,"onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy() called")
    }

    //Encapsulates changing questions and puts it in a method
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    //Encapsulates checking correct/incorrect answers to questions so we
    //don't have to write the same code in 2 different places
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show()
    }

}
