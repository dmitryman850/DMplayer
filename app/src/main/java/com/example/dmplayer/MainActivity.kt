package com.example.dmplayer

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var mBtnRegistration: Button? = null
    private var mBtnLogin: Button? = null
    private var mEditTxtPassword: EditText? = null
    private var mEditTxtEmail: EditText? = null
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mProgressBar: ProgressBar? = null
    private var mMainTxtProgressBar: TextView? = null
    private var mBtnWithoutLogin: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBtnRegistration = findViewById(R.id.btn_registration)
        mBtnLogin = findViewById(R.id.btn_login)
        mBtnWithoutLogin = findViewById(R.id.btn_without_login)
        mEditTxtEmail = findViewById(R.id.edit_txt_email)
        mEditTxtPassword = findViewById(R.id.edit_txt_password)
        mBtnLogin?.setOnClickListener(this)
        mBtnRegistration?.setOnClickListener(this)
        mProgressBar = findViewById(R.id.main_progress_bar)
        mMainTxtProgressBar = findViewById(R.id.txt_main_progress_bar)


        mFirebaseAuth = FirebaseAuth.getInstance()

        mBtnLogin?.setOnClickListener(this)
        mBtnRegistration?.setOnClickListener(this)
        mBtnWithoutLogin?.setOnClickListener(this)

        authenticationUser()
    }

    override fun onClick(view: View?) {
        when (view) {
            mBtnRegistration -> {
                registrationUser()
            }
            mBtnLogin -> {
                loginUser()
            }
            mBtnWithoutLogin -> {
                withoutLoginAndSms()
            }
        }
    }

    private fun withoutLoginAndSms() {
        startActivity(Intent(this, ViewPagerActivity::class.java)
            .addFlags(FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(FLAG_ACTIVITY_NEW_TASK))
    }

    private fun authenticationUser() {

        if (mFirebaseAuth?.currentUser != null) {
            startActivity(Intent(this, ViewPagerActivity::class.java)
                .addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(FLAG_ACTIVITY_NEW_TASK))
        } else {
            return
        }
    }

    private fun loginUser() {
        val email = mEditTxtEmail?.text.toString().trim()
        val password = mEditTxtPassword?.text.toString().trim()

        when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Введите Email", Toast.LENGTH_SHORT).show()
                return
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
                return
            }
        }
        mProgressBar?.visibility = ProgressBar.VISIBLE
        mMainTxtProgressBar?.visibility = TextView.VISIBLE
        mFirebaseAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Вход выполнен", Toast.LENGTH_LONG).show()
                    startActivity(
                        Intent(this, ViewPagerActivity::class.java)
                            .addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(FLAG_ACTIVITY_NEW_TASK)
                    )
                } else {
                    Toast.makeText(this, "Неверный Email или пароль", Toast.LENGTH_SHORT)
                        .show()
                }
                mProgressBar?.visibility = ProgressBar.INVISIBLE
                mMainTxtProgressBar?.visibility = TextView.INVISIBLE
            }
    }

    private fun registrationUser() {
        val email = mEditTxtEmail?.text.toString().trim()
        val password = mEditTxtPassword?.text.toString().trim()

        when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Введите Email", Toast.LENGTH_SHORT).show()
                return
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Введите пароль", Toast.LENGTH_SHORT).show()
                return
            }
            password.length < 6 -> {
                Toast.makeText(this, "Пароль должен состоять минимум из 6 символов",
                Toast.LENGTH_SHORT).show()
                return
            }
        }

        mProgressBar?.visibility = ProgressBar.VISIBLE
        mMainTxtProgressBar?.visibility = TextView.VISIBLE
        mFirebaseAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "Регистрация выполнена", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, ViewPagerActivity::class.java)
                        .addFlags(FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(FLAG_ACTIVITY_NEW_TASK))
                } else {
                    Toast.makeText(this, "Регистрация провалена, повторите попытку",
                        Toast.LENGTH_SHORT).show()
                }
                mProgressBar?.visibility = ProgressBar.INVISIBLE
                mMainTxtProgressBar?.visibility = TextView.INVISIBLE
            }
    }
}
