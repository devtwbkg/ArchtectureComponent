package xyz.twbkg.myviewmodel.persistence

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "users")
data class User(
        @ColumnInfo(name = "username")
        var user_name: String,
        @ColumnInfo(name = "password")
        var password: String = "",
        @Embedded
        var userInfo: UserDetail
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Long = 0
}
