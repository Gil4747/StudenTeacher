//package
//
//import android.content.ContentValues.TAG
//import android.util.Log
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.ktx.Firebase
//
//class DeleteUser {
//
//    private fun deleteUser() {
//        val user = FirebaseAuth.getInstance().currentUser
//
//        if (user != null) {
//            user.delete()
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d(TAG, "User account deleted.")
//                    }
//                }
//        }
//    }
//}