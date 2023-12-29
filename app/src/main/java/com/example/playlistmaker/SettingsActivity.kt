package com.example.playlistmaker


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.switch_night_theme)
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> themeSwitcher.isChecked = true
            Configuration.UI_MODE_NIGHT_NO -> themeSwitcher.isChecked = false
        }
        val  sharePrefs = App.PrefsProvider.sharedPreferences
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            if (checked) {
                sharePrefs
                    .edit()
                    .putBoolean(SWITCHER_KEY, true)
                    .apply()
            } else {
                sharePrefs
                    .edit()
                    .putBoolean(SWITCHER_KEY, false)
                    .apply()
            }
        }
        val toolbarSetting = findViewById<android.widget.Toolbar>(R.id.toolbar_setting)
        toolbarSetting.setNavigationOnClickListener {
            finish()
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