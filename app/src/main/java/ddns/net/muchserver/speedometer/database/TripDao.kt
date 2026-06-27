package ddns.net.muchserver.speedometer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TripDao {

    @Insert
    fun insertTrip(trip: Trip)

    @Query("SELECT * FROM $TABLE_TRIP WHERE $COLUMN_ID_TRIP = :idTrip")
    fun findTrip(idTrip: Long): Trip
    @Query("SELECT * FROM $TABLE_TRIP WHERE $COLUMN_NAME_TRIP = :name")
    fun findTrip(name: String): Trip

    @Query("DELETE FROM $TABLE_TRIP WHERE $COLUMN_ID_TRIP = :idTrip")
    fun deleteTrip(idTrip: Long)

    @Query("SELECT * FROM $TABLE_TRIP")
    fun getAllTrips(): LiveData<List<Trip>>
}