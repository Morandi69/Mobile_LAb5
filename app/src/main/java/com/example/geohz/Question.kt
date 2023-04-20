package com.example.geohz

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean, var enableButton:Boolean=true,var usedCheat:Boolean=false)