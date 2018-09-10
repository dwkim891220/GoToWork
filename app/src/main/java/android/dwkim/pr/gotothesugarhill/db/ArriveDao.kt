package android.dwkim.pr.gotothesugarhill.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface ArriveDao {
    @Query("SELECT * FROM arrives")
    fun loadAll() : LiveData<List<Arrive>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(arrive: Arrive)
}