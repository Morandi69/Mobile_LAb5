package com.example.geohz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

private const val TAG = "CheatActivity"
private const val KEY="AnswerShowned"
private const val ANSWER_KEY="Answer"

private const val EXTRA_ANSWER_IS_TRUE =
    "com.example.geoHz.answer_is_true"

const val EXTRA_ANSWER_SHOWN = "com.example.geoHZ.answer_shown"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private var answerIsTrue = false
    private var shownAnswer=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        answerTextView = findViewById(R.id.answer_text_view)

        shownAnswer = savedInstanceState?.getBoolean(KEY, false) ?: false
        answerTextView.text = savedInstanceState?.getString(ANSWER_KEY, "")
        setAnswerShownResult(shownAnswer)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            shownAnswer=true
            setAnswerShownResult(shownAnswer)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putBoolean(KEY,shownAnswer)
            putString(ANSWER_KEY,answerTextView.text.toString())
        }
        super.onSaveInstanceState(outState)

    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}