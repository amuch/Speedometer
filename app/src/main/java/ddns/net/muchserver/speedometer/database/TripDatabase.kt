package ddns.net.muchserver.speedometer.database

import android.content.Context
import androidx.room.Database
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


const val VERSION_DATABASE = 1
const val NAME_DATABASE = "tripDatabase"

@Database(entities = [CheckPoint::class, Trip::class], version = VERSION_DATABASE)
@TypeConverters(Converters::class)
abstract class TripDatabase: RoomDatabase() {
    abstract fun checkPointDao(): CheckPointDao
    abstract fun tripDao(): TripDao

    companion object {
        private var INSTANCE: TripDatabase? = null

        fun getInstance(context: Context): TripDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TripDatabase::class.java,
                        NAME_DATABASE
                    ).fallbackToDestructiveMigration(false)
                    .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}