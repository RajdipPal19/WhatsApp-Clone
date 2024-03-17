package com.example.whatsup.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.whatsup.MainActivity
import com.example.whatsup.databinding.ActivityOtpactivityBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var verificationId : String
    private lateinit var dialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Please Wait...")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
        val phoneNumber = "+91"+intent.getStringExtra("number")
        val options = PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS).setActivity(this)
            .setCallbacks(object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    dialog.dismiss()
                    Toast.makeText(this@OTPActivity,"OTP sent on Registered Mobile Number",Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                   Toast.makeText(this@OTPActivity,"Verification Failed ${p0}",Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    dialog.dismiss()
                    verificationId = p0
                }

            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
        binding.button.setOnClickListener {
            if(binding.OTP.text!!.isEmpty()){
                Toast.makeText(this,"Please Enter OTP to proceed",Toast.LENGTH_SHORT).show()
            }else{
                dialog.show()
                val credential = PhoneAuthProvider.getCredential(verificationId,binding.OTP.text.toString())
                auth.signInWithCredential(credential)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            dialog.dismiss()
                            startActivity(Intent(this,ProfileActivity::class.java))
                            finish()
                            Toast.makeText(this,"Successfully Logged In",Toast.LENGTH_SHORT).show()
                        }else{
                            dialog.dismiss()
                            Toast.makeText(this,"Error Occur",Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }

    }
}