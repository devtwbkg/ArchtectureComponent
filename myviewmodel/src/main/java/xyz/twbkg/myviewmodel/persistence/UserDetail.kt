package xyz.twbkg.myviewmodel.persistence

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class UserDetail(
        @ColumnInfo(name = "first_name")
        var firstName: String = "",
        @ColumnInfo(name = "last_name")
        var lastName: String = "",
        @Embedded
        var address: Address
) :Parcelable{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_detail_id")
    var id: Long = 0
}