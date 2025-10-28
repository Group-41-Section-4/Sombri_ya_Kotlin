package com.example.sombriyakotlin.ui.account
/*
import android.credentials.GetCredentialRequest
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import java.security.SecureRandom
import android.util.Base64
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

fun generateNonce(): String {
    val randomBytes = ByteArray(32) // 32 bytes = 256 bits
    SecureRandom().nextBytes(randomBytes)
    return Base64.encodeToString(randomBytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
}


private val WEB_CLIENT_ID = "751256331187-odgsd856p1nmo6ul9ec84odio8snnju1.apps.googleusercontent.com"


//// imports según arriba
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun beginSignInWithGoogle(activity: FragmentActivity, WEB_CLIENT_ID: String) {

    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(WEB_CLIENT_ID)
        .setAutoSelectEnabled(true)
        // nonce string to use when generating a Google ID token
        .setNonce(generateNonce())
        .build()


    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()


}

*/
//val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
//    serverClientId = WEB_CLIENT_ID
//).setNonce(generateNonce())
//    .build()


//@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
//val request: GetCredentialRequest = GetCredentialRequest.Builder()
//    .addCredentialOption(signInWithGoogleOption)
//    .build()




//fun handleSignIn(result: GetCredentialResponse) {
//    // Handle the successfully returned credential.
//    val credential = result.credential
//    val responseJson: String
//
//    when (credential) {
//
//        // Passkey credential
//        is PublicKeyCredential -> {
//            // Share responseJson such as a GetCredentialResponse to your server to validate and
//            // authenticate
//            responseJson = credential.authenticationResponseJson
//        }
//
//        // Password credential
//        is PasswordCredential -> {
//            // Send ID and password to your server to validate and authenticate.
//            val username = credential.id
//            val password = credential.password
//        }
//
//        // GoogleIdToken credential
//        is CustomCredential -> {
//            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//                try {
//                    // Use googleIdTokenCredential and extract the ID to validate and
//                    // authenticate on your server.
//                    val googleIdTokenCredential = GoogleIdTokenCredential
//                        .createFrom(credential.data)
//                    // You can use the members of googleIdTokenCredential directly for UX
//                    // purposes, but don't use them to store or control access to user
//                    // data. For that you first need to validate the token:
//                    // pass googleIdTokenCredential.getIdToken() to the backend server.
//                    // see [validation instructions](https://developers.google.com/identity/gsi/web/guides/verify-google-id-token)
//                } catch (e: GoogleIdTokenParsingException) {
//                    Log.e(TAG, "Received an invalid google id token response", e)
//                }
//            } else {
//                // Catch any unrecognized custom credential type here.
//                Log.e(TAG, "Unexpected type of credential")
//            }
//        }
//
//        else -> {
//            // Catch any unrecognized credential type here.
//            Log.e(TAG, "Unexpected type of credential")
//        }
//    }
//}