package com.example.helloworld.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.helloworld.activities.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.example.helloworld.models.User
import com.example.helloworld.utils.Constants
import com.google.firebase.database.*

/**
 * A custom class where we will add the operation performed for the firestore database.
 */
class FirestoreClass {

    // Create a instance of Firebase Firestore
    private val mFireStore = FirebaseFirestore.getInstance()
    private lateinit var mDbRef: DatabaseReference

//    lateinit var currentUser:User
    /**
     * A function to make an entry of the registered user in the firestore database.
     */

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        if(userInfo.allProfession.isEmpty())
            addUserToDatabase(userInfo.name,userInfo.email, FirebaseAuth.getInstance().currentUser?.uid!!,userInfo.allProfession,userInfo.mobile,userInfo.area,userInfo.gender,userInfo.age,userInfo.price,userInfo.chatList,userInfo.image)

        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(getCurrentUserID())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegisteredSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error writing document", e
                )
            }


        if(userInfo.allProfession.isNotEmpty()) {
            addUserToDatabase(
                userInfo.name,
                userInfo.email,
                FirebaseAuth.getInstance().currentUser?.uid!!,
                userInfo.allProfession,
                userInfo.mobile,
                userInfo.area,
                userInfo.gender,
                userInfo.age,
                userInfo.price,
                userInfo.chatList,
                userInfo.image
            )
            mFireStore.collection(Constants.TEACHERS)
                // Document ID for users fields. Here the document it is the User ID.
                .document(getCurrentUserID())
                // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener {

                    // Here call a function of base activity for transferring the result to it.
                    activity.userRegisteredSuccess()
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(
                        activity.javaClass.simpleName,
                        "Error writing document", e
                    )
                }
        }
    }
    private fun addUserToDatabase(name: String, email: String, uid: String, allProfessions: ArrayList<String>, phone: Long, area: String, gender: String, age: Int, price: HashMap<String, Int>, chatList: HashMap<String, User>, image: String) {
       val RDF = FirebaseDatabase.getInstance().reference
        RDF.child("user").child(uid).setValue(User(uid,name,email,allProfessions,phone,area,gender,image,age,price,chatList))
    }
    //    private fun addUserToDatabase(name: String, email: String, uid: String, phone:Long, area: String, gender: String,age:Int, price:Int) {
//        mDbRef = FirebaseDatabase.getInstance().reference
//        mDbRef.child("user").child(uid).setValue(User(uid,name,email))
//    }
    fun updateUserToDatabase(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
        mDbRef = FirebaseDatabase.getInstance().reference
        for (i in 0 until userHashMap.size) {
            mDbRef.child("user").child(getCurrentUserID()).child(userHashMap.keys.elementAt(i)).setValue(userHashMap.getValue(userHashMap.keys.elementAt(i))).addOnSuccessListener {
            }
        }
    }
    fun updateUserClassesToDatabase(activity: MyProfileActivity, userHashMap: HashMap<String, ArrayList<String>>) {
        mDbRef = FirebaseDatabase.getInstance().reference
        for (i in 0 until userHashMap.size) {
            mDbRef.child("user").child(getCurrentUserID()).child("allProfession").setValue(userHashMap.getValue("allProfession")).addOnSuccessListener {
            }
        }
    }
    fun updateUserPriceToDatabase(activity: MyProfileActivity, userHashMap: HashMap<String, HashMap<String, Int>>) {
        mDbRef = FirebaseDatabase.getInstance().reference
        for (i in 0 until userHashMap.size) {
            mDbRef.child("user").child(getCurrentUserID()).child("price").setValue(userHashMap.getValue("price")).addOnSuccessListener {
            }
        }
    }
    // TODO (Create a function to update the user profile data into the database.)
    // START
    /**
     * A function to update the user profile data into the database.
     */
    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
//        updateUserToDatabase(activity,userHashMap)
        mFireStore.collection(Constants.USERS) // Collection Name
            .document(getCurrentUserID()) // Document ID
            .update(userHashMap) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                // Profile data is updated successfully.
                Log.e(activity.javaClass.simpleName, "Profile Data updated successfully!")

                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                // Notify the success result.
                activity.profileUpdateSuccess()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board.", e)
                Toast.makeText(activity, "Error when updating the profile!", Toast.LENGTH_SHORT).show()

            }

    }
    // END
    fun updateUserProfileData2(activity: MyProfileActivity, userHashMap: HashMap<String, ArrayList<String>>) {
        mFireStore.collection(Constants.USERS) // Collection Name
            .document(getCurrentUserID()) // Document ID
            .update(userHashMap as Map<String, Any>) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                // Profile data is updated successfully.
                Log.e(activity.javaClass.simpleName, "Profile Data updated successfully!")

                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                // Notify the success result.
                activity.profileUpdateSuccess()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board.", e)
                Toast.makeText(activity, "Error when updating the profile!", Toast.LENGTH_SHORT).show()

            }

    }
    fun updateUserPriceData(activity: MyProfileActivity, userHashMap: HashMap<String, HashMap<String,Int>>) {
        mFireStore.collection(Constants.USERS) // Collection Name
            .document(getCurrentUserID()) // Document ID
            .update(userHashMap as Map<String, Any>) // A hashmap of fields which are to be updated.
            .addOnSuccessListener {
                // Profile data is updated successfully.
                Log.e(activity.javaClass.simpleName, "Profile Data updated successfully!")

                Toast.makeText(activity, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                // Notify the success result.
                activity.profileUpdateSuccess()
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board.", e)
                Toast.makeText(activity, "Error when updating the profile!", Toast.LENGTH_SHORT).show()

            }

    }



    // TODO (We can use the same function to get the current logged in user details. As we need to modify only few things here.)
    // START
    /**
     * A function to SignIn using firebase and get the user details from Firestore Database.
     */
    fun loudUserData(activity: Activity) {
        val docRef= mFireStore.collection(Constants.USERS).document(getCurrentUserID())
        // Here we pass the collection name from which we wants the data.
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.e(activity.javaClass.simpleName, document.toString())

                    // Here we have received the document snapshot which is converted into the User Data model object.
                    val loggedInUser = document.toObject(User::class.java)!!
//                    currentUser=User(loggedInUser.id,loggedInUser.name,loggedInUser.email,loggedInUser.profession1)

                    // TODO(Modify the parameter and check the instance of activity and send the success result to it.)
                    // START
                    // Here call a function of base activity for transferring the result to it.
                    when (activity) {
                        is SignInActivityG -> {
                            activity.signInSuccess(loggedInUser)
                        }
                        is HomePageActivity -> {
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                        is MyProfileActivity -> {
                            activity.setUserDataInUI(loggedInUser)
                        }
                        is UserProfileActivity -> {
                            activity.setUserDataInUI(loggedInUser)
                        }
                        // END
                    }
                    // END
                }
                else {
                    Log.d("loadUser", "No such document")
                }
            }
            .addOnFailureListener { e ->
                // TODO(Hide the progress dialog in failure function based on instance of activity.)
                // START
                // Here call a function of base activity for transferring the result to it.
                when (activity) {
                    is SignInActivityG -> {
                        activity.hideProgressDialog()
                    }
                    is HomePageActivity -> {
                        activity.hideProgressDialog()
                    }
//                    לבדוק למה זה נתקע ברגע שאני מפעילה את שתי השורות למעלה

                    // END
                }

                // END
                Log.e(
                    "SignInUser",
                    "Error writing document",
                    e
                )
            }
    }

    /**
     * A function for getting the user id of current logged user.
     */
//    fun getCurrentUserID(): String {
//        return FirebaseAuth.getInstance().currentUser!!.uid
//    }
    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
}