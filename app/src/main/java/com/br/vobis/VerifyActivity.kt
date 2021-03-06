package com.br.vobis

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_verify.*
import java.util.concurrent.TimeUnit

class VerifyActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var mcallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var nameUser = ""
    private var numberPhone = ""
    private var verificationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        val extras = intent.extras
        if (extras != null) {
            numberPhone = extras.getString("phone")!!
            nameUser = extras.getString("name")!!
        }

        verify()

        btn_verificar.setOnClickListener {
            authenticate()
        }
    }

    private fun authenticate() {
        val verifyCode = verify_phone.text.toString()

        if (verifyCode.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verificationId, verifyCode)

            signIn(credential)
        } else {
            Toast.makeText(this, "Insira o código", Toast.LENGTH_LONG).show()
        }
    }

    private fun verifyCallbacks() {
        mcallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                signIn(phoneAuthCredential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@VerifyActivity, p0.message.toString(), Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                verificationId = p0
            }
        }
    }

    private fun signIn(credential: PhoneAuthCredential?) {
        if (credential != null) {
            mAuth.signInWithCredential(credential).addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val profileUpdates = UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(nameUser)
                            .build()

                    user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, getString(R.string.login_sucess), Toast.LENGTH_LONG).show()

                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                } else {
                    Toast.makeText(this, "Sem código", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun verify() {
        verifyCallbacks()
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+55 $numberPhone", 60, TimeUnit.SECONDS, this, mcallbacks)
    }
}



