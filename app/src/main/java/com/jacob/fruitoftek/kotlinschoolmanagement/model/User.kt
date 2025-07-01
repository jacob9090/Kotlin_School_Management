package com.jacob.fruitoftek.kotlinschoolmanagement.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val email: String,
    val phone: String?,
    val district: String,
    val community: String,
    val cooperative: String,
    val address: String?,
    val photo: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(role)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(district)
        parcel.writeString(community)
        parcel.writeString(cooperative)
        parcel.writeString(address)
        parcel.writeString(photo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}