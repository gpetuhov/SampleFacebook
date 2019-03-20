package com.gpetuhov.android.samplefacebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.facebook.*
import com.facebook.login.LoginResult
import com.pawegio.kandroid.toast
import java.util.Arrays.asList
import kotlinx.android.synthetic.main.activity_main.*
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker




class MainActivity : AppCompatActivity() {

    companion object {
        const val EMAIL = "email"
    }

    private var callbackManager = CallbackManager.Factory.create()
    private var accessTokenTracker: AccessTokenTracker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton.setReadPermissions(asList(EMAIL))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                toast("Login success")
            }

            override fun onCancel() {
                toast("Login cancel")
            }

            override fun onError(exception: FacebookException) {
                toast("Login error")
            }
        })

        checkLoginButtton.setOnClickListener { toast("Logged in = ${isLoggedIn()}") }

        // We can listen to token changes like this.
        // Don't forget to stop tracking in onDestroy
        accessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(
                oldAccessToken: AccessToken?,
                currentAccessToken: AccessToken?
            ) {
                val text = "Logged in = ${currentAccessToken != null && !currentAccessToken.isExpired}"
                loginStatus.text = text
                // Notice, that text will change only when the token changes (not on the app start)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // This is required to pass results to the LoginManager
        // (and for the callback to be triggered).
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Stop tracking token
        accessTokenTracker?.stopTracking()
    }

    // We can manually check if the user is logged in like this
    private fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }
}
