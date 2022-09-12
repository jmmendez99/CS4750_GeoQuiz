package com.bignerdranch.android.geoquiz

import androidx.annotation.StringRes

//This will be a data class that can hold multiple questions
data class Question (@StringRes val textResId: Int, val answer: Boolean)
