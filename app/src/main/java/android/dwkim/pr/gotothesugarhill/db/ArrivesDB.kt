package android.dwkim.pr.gotothesugarhill.db

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import java.util.*

@Database(entities = [Arrive::class], version = 1)
@TypeConverters(Converters::class)
abstract class ArrivesDB : RoomDatabase() {
    abstract fun arriveDao(): ArriveDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}