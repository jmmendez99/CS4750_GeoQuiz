package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    //Index variable to iterate through the question bank
    var currentIndex = 0
    var isCheater = false

    //List of Question Objects
    private val questionBank = listOf(
        Question(R.string.question_mountain, true),
        Question(R.string.question_country, false),
        Question(R.string.question_canada, false),
        Question(R.string.question_antarctica, true),
        Question(R.string.question_ocean, false),
        Question(R.string.question_egypt, true))

    //Methods to access questionBank answers and to get the questionID
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    fun moveToPrevious() {
        currentIndex = (currentIndex - 1) % questionBank.size
    }
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}