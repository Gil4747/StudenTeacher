package com.example.helloworld.models
import android.os.Parcel
import android.os.Parcelable

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    var allProfession: ArrayList<String> = ArrayList(),
    val mobile: Long = 0,
    val image: String = "",
    val fcmToken: String = "",
    val area: String = "",
    val gender: String = ""

//    val profession2: String = "",
//    val profession3: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
//        parcel.readString()!!,
//        parcel.readString()!!
    )

   
    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(email)
        writeStringList(allProfession)
        writeLong(mobile)
        writeString(image)
        writeString(fcmToken)
        writeString(area)
        writeString(gender)

//        writeString(profession2)
//        writeString(profession3)
    }

//    companion object CREATOR : Parcelable.Creator<User> {
//        override fun createFromParcel(parcel: Parcel): User {
//            return User(parcel)
//        }
//
//        override fun newArray(size: Int): Array<User?> {
//            return arrayOfNulls(size)
//        }
//    }


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}