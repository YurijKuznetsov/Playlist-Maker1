package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView


class SearchActivity : AppCompatActivity() {
    companion object {
        private const val INPUT_SEARCH = "INPUT_SEARCH"
    }
    private var inputSearch: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)
        val toolbarSearch = findViewById<Toolbar>(R.id.toolbar_search)
        val recyclerviewSearch = findViewById<RecyclerView>(R.id.recyclerview_search)
        val searchAdapter = SearchAdapter(
            listOf(
                Track(getString(R.string.track_name_1), getString(R.string.artist_name_1), getString(R.string.track_time_1), getString(R.string.artwork_url_1)),
                Track(getString(R.string.track_name_2), getString(R.string.artist_name_2), getString(R.string.track_time_2), getString(R.string.artwork_url_2)),
                Track(getString(R.string.track_name_3), getString(R.string.artist_name_3), getString(R.string.track_time_3), getString(R.string.artwork_url_3)),
                Track(getString(R.string.track_name_4), getString(R.string.artist_name_4), getString(R.string.track_time_4), getString(R.string.artwork_url_4)),
                Track(getString(R.string.track_name_5), getString(R.string.artist_name_5), getString(R.string.track_time_5), getString(R.string.artwork_url_5))
            )
        )
        recyclerviewSearch.adapter = searchAdapter
        toolbarSearch.setNavigationOnClickListener {
            finish()
        }
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
                clearButton.isVisible = !s.isNullOrEmpty()
                inputSearch =s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setText(inputSearch)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_SEARCH, inputSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearch = savedInstanceState.getString(INPUT_SEARCH, "")
    }

}