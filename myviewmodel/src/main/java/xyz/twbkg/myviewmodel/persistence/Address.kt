package xyz.twbkg.myviewmodel.persistence

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "address")
data class Address(
        var state: String = "",
        @ColumnInfo(name = "city")
        var city: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "address_id")
    var id: Long = 0
}
