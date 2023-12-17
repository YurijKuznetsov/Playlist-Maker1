package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class SearchActivity : AppCompatActivity() {
    companion object {
        private const val INPUT_SEARCH = "INPUT_SEARCH"
    }
    private var inputSearch: String = ""
    private val searchITunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(searchITunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)
    private val tracks = ArrayList<Track>()
    private val searchAdapter = SearchAdapter()
    private lateinit var buttonClearHistory : Button
    private lateinit var notFoundPlaceholder : LinearLayout
    private lateinit var notInternetPlaceholder : LinearLayout
    private lateinit var recyclerviewSearch : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        notFoundPlaceholder = findViewById(R.id.not_found_placeholder)
        notInternetPlaceholder = findViewById(R.id.not_internet_placeholder)
        recyclerviewSearch = findViewById(R.id.recyclerview_search)
        buttonClearHistory = findViewById(R.id.button_clear_history)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)
        val toolbarSearch = findViewById<Toolbar>(R.id.toolbar_search)
        val buttonUpdate = findViewById<Button>(R.id.button_update)
        searchAdapter.tracks = tracks
        recyclerviewSearch.adapter = searchAdapter
        buttonUpdate.setOnClickListener {
            notInternetPlaceholder.visibility = View.GONE
            search(inputSearch)
        }
        inputEditText.setOnEditorActionListener {_, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                notFoundPlaceholder.visibility = View.GONE
                notInternetPlaceholder.visibility = View.GONE
                search(inputSearch)
            }
            false
        }
        toolbarSearch.setNavigationOnClickListener {
            finish()
        }
        buttonClearHistory.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            inputEditText.clearFocus()
            recyclerviewSearch.visibility = View.GONE
            buttonClearHistory.visibility = View.GONE
        }
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            inputEditText.clearFocus()
            recyclerviewSearch.visibility = View.GONE
            buttonClearHistory.visibility = View.GONE
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

    private fun search(text: String) {
        iTunesService.search(text).enqueue(object : Callback<TrackResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) =
                if (response.code() == 200) {
                    tracks.clear()
                    if (response.body()?.resultCount == 0) {
                        recyclerviewSearch.visibility = View.GONE
                        notFoundPlaceholder.visibility = View.VISIBLE
                    } else {
                        recyclerviewSearch.visibility = View.VISIBLE
                        tracks.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()
                        buttonClearHistory.visibility = View.VISIBLE
                    }
                } else {
                    recyclerviewSearch.visibility = View.GONE
                    notInternetPlaceholder.visibility = View.VISIBLE

                }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                recyclerviewSearch.visibility = View.GONE
                notInternetPlaceholder.visibility = View.VISIBLE
            }

        })
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