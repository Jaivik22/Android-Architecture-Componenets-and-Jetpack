package com.example.loginmvvm.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.os.CancellationSignal
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManagerCallback
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.example.loginmvvm.model.UserDetailsModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.concurrent.Executor
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume

class GoogleSignInClient(val context: Context) {

    fun signOut(){
        val credentialManager = CredentialManager.create(context)
        val request = ClearCredentialStateRequest() // Replace with actual request
        val executor =
            Executor { command -> Thread(command).start() } // Use appropriate Executor
        val cancellationSignal = CancellationSignal()
        credentialManager.clearCredentialStateAsync(request,
            cancellationSignal,
            executor,
            object : CredentialManagerCallback<Void?, ClearCredentialException> {
                override fun onResult(result: Void?) {
                    println("Credential state cleared successfully")
                }

                override fun onError(exception: ClearCredentialException) {
                    println("Failed to clear credential state: ${exception.message}")
                }
            })
    }

    suspend fun googleSignIn(): UserDetailsModel = suspendCancellableCoroutine { continuation ->
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("628870865791-89segq932avqc3873e0e9guced6cv6b6.apps.googleusercontent.com")
            .setAutoSelectEnabled(true)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(context)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = credentialManager.getCredential(context,request )
                val userDetails = retriveGoogleUserData(result)
//                Log.d("googleSignIn",flag.toString())
                continuation.resume(userDetails)
            } catch (e: GetCredentialException) {
                handleFailure(e)
                continuation.resume(UserDetailsModel("","","",false))
            }
            catch (e: CancellationException) {
                Log.e("GoogleSignInClient", "Google Sign-In was cancelled by the user")
                continuation.resume(UserDetailsModel("","","",false)) // Still return a result to the continuation
            }
        }
    }

    private fun handleFailure(e: GetCredentialException)/*: Any */ {
        Log.e("handleFailure", e.errorMessage.toString())
    }

    private suspend fun retriveGoogleUserData(result: GetCredentialResponse):UserDetailsModel{
        var userDetails: UserDetailsModel? = null
        return withContext(Dispatchers.Default) {
            var isVerify = false;
            // Handle the successfully returned credential.
            val credential = result.credential
            var username = ""
            var password = ""

            when (credential) {

                // Passkey credential
                is PublicKeyCredential -> {
                    // Share responseJson such as a GetCredentialResponse on your server to
                    // validate and authenticate
                    var responseJson = credential.authenticationResponseJson
                }

                // Password credential
                is PasswordCredential -> {
                    // Send ID and password to your server to validate and authenticate.
                    username = credential.id
                    password = credential.password
                }

                // GoogleIdToken credential
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            // Use googleIdTokenCredential and extract the ID to validate and
                            // authenticate on your server.
                            val googleIdTokenCredential = GoogleIdTokenCredential
                                .createFrom(credential.data)
                            val idToken = googleIdTokenCredential.idToken

                            isVerify = verifyIdToken(idToken)
                            userDetails = UserDetailsModel(
                                googleIdTokenCredential.displayName!!,
                                googleIdTokenCredential.id,
                                password,
                                isVerify
                            )
                        } catch (e: GoogleIdTokenParsingException) {

                            Log.e(TAG, "Received an invalid google id token response", e)
                        }
                    } else {
                        // Catch any unrecognized custom credential type here.
                        Log.e(TAG, "Unexpected type of credential")
                    }
                }

                else -> {
                    // Catch any unrecognized credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }
            Log.d("retriveGoogleUserData", isVerify.toString())
            userDetails!!
        }
    }

    fun verifyIdToken(idTokenString: String): Boolean {
        try {
            val transport: HttpTransport = GoogleNetHttpTransport.newTrustedTransport()
            val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()
            // Load Google credentials
            val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(listOf("628870865791-89segq932avqc3873e0e9guced6cv6b6.apps.googleusercontent.com")) // Replace with your client ID
                .setIssuer("https://accounts.google.com")
                .build()

            // Verify the token
            val idToken = verifier.verify(idTokenString)

            if (idToken != null) {
                println("ID Token is valid.")
                // Get user information from the token
                val payload = idToken.payload
                val userId = payload.subject
                val email = payload.email
                println("User ID: $userId")
                println("Email: $email")
                return true
            } else {
                println("Invalid ID Token.")
                return false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

}