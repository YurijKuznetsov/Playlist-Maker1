package com.example.playlistmaker


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbarSetting = findViewById<android.widget.Toolbar>(R.id.toolbar_setting)
        toolbarSetting.setNavigationOnClickListener {
            finish()
        }
        val switcherNightTheme = findViewById<SwitchCompat>(R.id.switch_night_theme)
        switcherNightTheme.setOnCheckedChangeListener {_,  isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        val buttonShare = findViewById<FrameLayout>(R.id.button_share)
        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plane"
           shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_adress))
           startActivity(shareIntent)
        }
        val buttonSupport = findViewById<FrameLayout>(R.id.button_write_support)
        buttonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email_adress)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_support))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_support))
            startActivity(supportIntent)
        }
        val buttonUserAgreement = findViewById<FrameLayout>(R.id.button_user_agreement)
        buttonUserAgreement.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            userAgreementIntent.data = Uri.parse(getString(R.string.url_practicum_offer))
            startActivity(userAgreementIntent)

        }


    }
}