package com.visight.adondevamos.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "FavouritePlace")
class FavouritePlace() : Parcelable {
    @PrimaryKey(autoGenerate = true) var id: Long? = null
    @ColumnInfo(name = "name")var name: String? = ""
    @ColumnInfo(name = "icon")var icon: String? = ""
    @ColumnInfo(name = "address")var address: String? = null
    @ColumnInfo(name = "rating")var rating: Double? = 0.0
    @ColumnInfo(name = "placeId") var placeId: String? = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Long::class.java.classLoader) as? Long
        name = parcel.readString()
        icon = parcel.readString()
        address = parcel.readString()
        rating = parcel.readValue(Double::class.java.classLoader) as? Double
        placeId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeString(address)
        parcel.writeValue(rating)
        parcel.writeString(placeId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavouritePlace> {
        override fun createFromParcel(parcel: Parcel): FavouritePlace {
            return FavouritePlace(parcel)
        }

        override fun newArray(size: Int): Array<FavouritePlace?> {
            return arrayOfNulls(size)
        }
    }

}