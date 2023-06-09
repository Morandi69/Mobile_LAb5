package com.example.geohz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.lang.Math.abs


private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var true_button:Button
    private lateinit var false_button:Button
    private lateinit var nextButton:ImageButton
    private lateinit var prevButton:ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex


        true_button=findViewById(R.id.true_button)
        false_button=findViewById(R.id.false_button)
        nextButton=findViewById(R.id.next_button)
        prevButton=findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        cheatButton = findViewById(R.id.cheat_button)

        true_button.setOnClickListener {
            if(quizViewModel.questionBank[quizViewModel.currentIndex].usedCheat==false) {
                checkAnswer(true)
                quizViewModel.FalsedEnableButton()
                true_button.setEnabled(quizViewModel.currentQuestionEnableButton)
                false_button.setEnabled(quizViewModel.currentQuestionEnableButton)
                ShowAnswer()
            }
            else{
                Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        false_button.setOnClickListener {
            if(quizViewModel.questionBank[quizViewModel.currentIndex].usedCheat==false) {
                checkAnswer(false)
                quizViewModel.FalsedEnableButton()
                true_button.setEnabled(quizViewModel.currentQuestionEnableButton)
                false_button.setEnabled(quizViewModel.currentQuestionEnableButton)
                ShowAnswer()
            }
            else{
                Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        updateQuestion()

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        prevButton.setOnClickListener {
            quizViewModel.moveToBack()
            updateQuestion()
        }

        questionTextView.setOnClickListener{
            updateQuestion()
        }
        cheatButton.setOnClickListener {
            // Начало CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        updateQuestion()

    }
    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.questionBank[quizViewModel.currentIndex].usedCheat  =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

        true_button.setEnabled(quizViewModel.currentQuestionEnableButton)
        false_button.setEnabled(quizViewModel.currentQuestionEnableButton)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        if (userAnswer == correctAnswer) {
            quizViewModel.resultPoint+=1
        }
    }

    private fun ShowAnswer(){
        val questionBank=quizViewModel.questionBank
        for( (index, element) in questionBank.withIndex()){
            if (questionBank[index].enableButton==true){return}

        }
            Toast.makeText(this, quizViewModel.resultPoint.toString()+" правильных ответов из 6", Toast.LENGTH_SHORT)
                .show()

    }

}