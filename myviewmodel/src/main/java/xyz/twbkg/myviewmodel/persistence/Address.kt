package xyz.twbkg.myviewmodel.persistence

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "address")
data class Address(
        @ColumnInfo(name = "city")
        var city: String = ""
):Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "address_id")
    var id: Long = 0
}
