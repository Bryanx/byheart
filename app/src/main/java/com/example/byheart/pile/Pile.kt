package com.example.byheart.pile

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pile(@ColumnInfo(name = "name") val name: String?) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(parcel: Parcel) : this(parcel.readString()) {
        id = parcel.readLong()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pile> {
        override fun createFromParcel(parcel: Parcel): Pile {
            return Pile(parcel)
        }

        override fun newArray(size: Int): Array<Pile?> {
            return arrayOfNulls(size)
        }
    }
}