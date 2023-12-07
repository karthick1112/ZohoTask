package com.example.test

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

class SplashActivity : AppCompatActivity() {
    var mContext: Context? = null
    lateinit var mactivity: AppCompatActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        mContext = this
        mactivity = this

        Handler().postDelayed({
                val intent = Intent(this, WeatherActivity::class.java)
                startActivity(intent)
                mactivity.finish()

        }, 2000)
    }
}