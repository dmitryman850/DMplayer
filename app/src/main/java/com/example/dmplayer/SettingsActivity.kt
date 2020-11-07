package com.example.dmplayer

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private var mFirebaseAuth: FirebaseAuth? = null
    private var mTxtWelcomeUserName: TextView? = null
    private var mBtnLogOut: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mTxtWelcomeUserName = findViewById(R.id.txt_welcome_userName)
        mBtnLogOut = findViewById(R.id.btn_logout)
        mFirebaseAuth = FirebaseAuth.getInstance()
        mBtnLogOut?.setOnClickListener(this)

        val user = mFirebaseAuth?.currentUser
        mTxtWelcomeUserName?.text = "Привет ${user?.email}"

    }

    private fun logOut() {
        if (mFirebaseAuth?.currentUser != null) {
            mFirebaseAuth?.signOut()
            startActivity(Intent(this, MainActivity::class.java)
                    .addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                    .addFlags(FLAG_ACTIVITY_NEW_TASK))
        } else {
            return
        }
    }

    override fun onClick(view: View?) {
        if(view == mBtnLogOut) {
            logOut()
        }
    }
}