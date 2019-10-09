package com.visight.adondevamos.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class User() : Parcelable {
    @SerializedName("Id")
    var id: String = ""
    @SerializedName("Nombre")
    var name: String = ""
    @SerializedName("Apellido")
    var surname: String = ""
    @SerializedName("Email")
    var email: String = ""
    @SerializedName("Password")
    var password: String = ""
    var profileImage: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        name = parcel.readString()
        surname = parcel.readString()
        email = parcel.readString()
        password = parcel.readString()
        profileImage = parcel.readString()
    }

    constructor(user: User) : this() {
        id = user.id
        name = user.name
        surname = user.surname
        email = user.email
        password = user.password
    }

    constructor(name: String, surname: String, profileImage: String) : this() {
        this.name = name
        this.surname = surname
        this.profileImage = profileImage
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(surname)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(profileImage)
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