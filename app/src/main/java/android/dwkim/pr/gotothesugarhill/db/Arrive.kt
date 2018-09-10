package android.dwkim.pr.gotothesugarhill.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "arrives")
data class Arrive(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var date: Date,
        var wifiName: String
)