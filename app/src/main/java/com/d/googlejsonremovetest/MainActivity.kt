package com.d.googlejsonremovetest

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.d.googlejsonremovetest.Utils.decrypt
import com.d.googlejsonremovetest.Utils.encrypt
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


class MainActivity : AppCompatActivity() {

    lateinit var btnLogReport:Button

    lateinit var keyGenerator: KeyGenerator
    lateinit var secretKey: SecretKey
    var IV = ByteArray(16)
    lateinit var random: SecureRandom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        FirebaseApp.initializeApp(
            this, FirebaseOptions.Builder()
                .setApplicationId("1:885955321773:android:d7a966ac66688736a24b24")
                .setApiKey("AIzaSyCtbXlcun6QajZtTD2Nd3rW1pzPPY5Lg0Q")
                //.setDatabaseUrl(firebase_url)
                .setGcmSenderId("885955321773")
                .setStorageBucket("ecommadmin-2c953.appspot.com")
                .setProjectId("ecommadmin-2c953")
                .build()
        )


        FirebaseMessaging.getInstance().token.addOnSuccessListener { token: String ->
            if (!TextUtils.isEmpty(token)) {
                Log.d("TAG", "retrieve token successful : $token")
            } else {
                Log.w("TAG", "token should not be null...")
            }
        }.addOnFailureListener { e: Exception? -> }.addOnCanceledListener {}
            .addOnCompleteListener { task: Task<String> ->
                Log.v(
                    "TAG",
                    "This is the token : " + task.result
                )
            }

        btnLogReport = findViewById<Button>(R.id.btnLogReport)
        btnLogReport.setOnClickListener {
            var msg = findViewById<EditText>(R.id.edMsg).text.toString()
            var dd = System.currentTimeMillis()
            throw RuntimeException(" $msg - Test Crash - $dd")
        }



        // AES 256 Test section

        try {
            keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(256)
            secretKey = keyGenerator.generateKey()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        random =  SecureRandom();
        random.nextBytes(IV)

        /// Test
        var testText = "Welcome Test"
        var encryptMsg: ByteArray? = null
        try {
            encryptMsg = encrypt(testText.toString().toByteArray(), secretKey, IV)
            val encryptText = String(encryptMsg, "UTF-8")

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        //
        try {
            val decryptMsg = decrypt(encryptMsg, secretKey, IV)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}