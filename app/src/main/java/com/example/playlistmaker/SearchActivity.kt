package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val toolbarSearch = findViewById<Toolbar>(R.id.toolbar_search)
        toolbarSearch.setNavigationOnClickListener {
            finish()
        }
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            inputEditText.clearFocus()
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }
    private fun clearButtonVisibility(s: CharSequence?) : Int {
        return if (s.isNullOrEmpty()) {
          View.GONE
        } else {
            View.VISIBLE
        }
    }
}