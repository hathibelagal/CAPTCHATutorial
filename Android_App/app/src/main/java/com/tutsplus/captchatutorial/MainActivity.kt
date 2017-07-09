package com.tutsplus.captchatutorial

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.httpGet
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myClient: SafetyNetClient = SafetyNet.getClient(this)

        are_you_human_button.setOnClickListener {
            myClient.verifyWithRecaptcha(resources.getString(R.string.my_site_key))
                    .addOnSuccessListener { successEvent ->
                        val token: String = successEvent.tokenResult

                        val serverURL: String = "http://10.0.2.2:8000/validate"

                        serverURL.httpGet(listOf("user_token" to token))
                                .responseString { request, response, result ->
                                    result.fold({ data ->
                                        if(data.contains("PASS"))
                                            Toast.makeText(baseContext,
                                                    "You seem to be a human.",
                                                    Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(baseContext,
                                                    "You seem to be a bot!",
                                                    Toast.LENGTH_LONG).show();
                                    }, { error ->
                                        Log.d("ERROR", "Error connecting to the server")
                                    })
                                }
                    }
        }
    }
}
