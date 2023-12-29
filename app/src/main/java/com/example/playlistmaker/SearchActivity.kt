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
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class SearchActivity : AppCompatActivity(), SearchAdapter.OnclickTrackInterface {
    companion object {
        const val SERVER_CODE_200 = 200
        private const val INPUT_SEARCH = "INPUT_SEARCH"
        const val SEARCH_HISTORY_SIZE = 10
    }
    private val sharedPreferences = App.PrefsProvider.sharedPreferences
    private val searchHistory = SearchHistory(sharedPreferences)
    var trackHistory = searchHistory.trackHistory
    private var inputSearch: String = ""
    private val searchITunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(searchITunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)
    private val tracks = mutableListOf<Track>()
    private val searchAdapter = SearchAdapter(tracks, this)
    private lateinit var buttonClearHistory : Button
    private lateinit var notFoundPlaceholder : LinearLayout
    private lateinit var notInternetPlaceholder : LinearLayout
    private lateinit var recyclerViewSearch : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        notFoundPlaceholder = findViewById(R.id.not_found_placeholder)
        notInternetPlaceholder = findViewById(R.id.not_internet_placeholder)
        recyclerViewSearch = findViewById(R.id.recyclerview_search)
        buttonClearHistory = findViewById(R.id.button_clear_history)
        val searchHistoryField = findViewById<NestedScrollView>(R.id.search_history_field)
        val recyclerViewSearchHistory = findViewById<RecyclerView>(R.id.search_history_recyclerview)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)
        val toolbarSearch = findViewById<Toolbar>(R.id.toolbar_search)
        val buttonUpdate = findViewById<Button>(R.id.button_update)
        if (sharedPreferences.getString(SEARCH_HISTORY_KEY, null) != null) {
            trackHistory.clear()
            trackHistory.addAll(searchHistory.read().tracks)
        }
        val searchHistoryAdapter = SearchAdapter(trackHistory, this)
        searchAdapter.tracks = tracks
        recyclerViewSearch.adapter = searchAdapter
        recyclerViewSearchHistory.adapter = searchHistoryAdapter
        buttonUpdate.setOnClickListener {
            notInternetPlaceholder.visibility = View.GONE
            search(inputSearch)
        }
        inputEditText.setOnFocusChangeListener {_, hasFocus ->
            if (hasFocus && inputEditText.text.isNullOrEmpty() && trackHistory.isNotEmpty()) {
                searchHistoryField.visibility = View.VISIBLE
                recyclerViewSearchHistory.visibility = View.VISIBLE
                recyclerViewSearch.visibility = View.GONE
            }
            else {
                searchHistoryField.visibility = View.GONE
                recyclerViewSearchHistory.visibility = View.VISIBLE
                recyclerViewSearch.visibility = View.GONE
            }
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
            trackHistory.clear()
            searchHistory.clear()
            searchHistoryField.visibility = View.GONE
        }
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            inputEditText.clearFocus()
            recyclerViewSearch.visibility = View.GONE
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                inputSearch =s.toString()
                if (inputEditText.hasFocus() && s?.isEmpty() == true && trackHistory.isNotEmpty()) {
                    searchHistoryField.visibility = View.VISIBLE
                    recyclerViewSearchHistory.visibility = View.VISIBLE
                    recyclerViewSearch.visibility = View.GONE
                }
                else {
                    searchHistoryField.visibility = View.GONE
                    recyclerViewSearchHistory.visibility = View.VISIBLE
                    recyclerViewSearch.visibility = View.GONE
                }
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
                if (response.code() == SERVER_CODE_200) {
                    tracks.clear()
                    if (response.body()?.resultCount == 0) {
                        recyclerViewSearch.visibility = View.GONE
                        notFoundPlaceholder.visibility = View.VISIBLE
                    } else {
                        recyclerViewSearch.visibility = View.VISIBLE
                        tracks.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()
                        buttonClearHistory.visibility = View.VISIBLE
                    }
                } else {
                    recyclerViewSearch.visibility = View.GONE
                    notInternetPlaceholder.visibility = View.VISIBLE
                }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                recyclerViewSearch.visibility = View.GONE
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

    override fun onClickTrack(track: Track) {
        when {
            trackHistory.isEmpty() -> {
                trackHistory.add(track)
                searchHistory.write(Tracks(trackHistory))
            }
            trackHistory.contains(track) -> {
                trackHistory.remove(track)
                trackHistory.add(0, track)
                searchHistory.write(Tracks(trackHistory))
            }
            trackHistory.size == SEARCH_HISTORY_SIZE -> {
                trackHistory.removeLast()
                trackHistory.add(0, track)
                searchHistory.write(Tracks(trackHistory))
            }
            else -> {
                trackHistory.add(0, track)
                searchHistory.write(Tracks(trackHistory))
            }
        }
    }
}




