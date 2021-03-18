package com.example.myapplication.data.model

import android.os.Parcel
import android.os.Parcelable

data class Data(
    val children: Children?,
    val id: String?,
    val media_type: String?,
    val media_url: String?
):Parcelable{
    constructor(parcel: Parcel) : this(
        children=null,
        id=parcel.readString(),
        media_type=parcel.readString(),
        media_url=parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(media_type)
        parcel.writeString(media_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }

}