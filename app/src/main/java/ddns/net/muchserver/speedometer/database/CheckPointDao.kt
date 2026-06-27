package ddns.net.muchserver.speedometer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CheckPointDao {
    @Insert
    fun insertCheckPoint(checkPoint: CheckPoint)

    @Query("SELECT * FROM $TABLE_CHECKPOINT WHERE $COLUMN_ID_CHECKPOINT = :idCheckPoint")
    fun findCheckPoint(idCheckPoint: Long): CheckPoint

    @Query("DELETE FROM $TABLE_CHECKPOINT WHERE $COLUMN_ID_CHECKPOINT = :idCheckPoint")
    fun deleteCheckPoint(idCheckPoint: Long)

    @Query("SELECT * FROM $TABLE_CHECKPOINT")
    fun getAllCheckPoints(): LiveData<List<CheckPoint>>

    @Query("SELECT * FROM $TABLE_CHECKPOINT WHERE $COLUMN_ID_TRIP_FOREIGN = :idTrip")
    fun getCheckPointsCurrent(idTrip: Long): LiveData<List<CheckPoint>>

}