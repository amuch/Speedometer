package ddns.net.muchserver.speedometer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

const val TABLE_TRIP = "trip"
const val COLUMN_ID_TRIP = "idTrip"
const val COLUMN_NAME_TRIP = "nameTrip"

@Entity(tableName = TABLE_TRIP)
class Trip {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID_TRIP)
    var id: Long = 0

    @ColumnInfo(name = COLUMN_NAME_TRIP)
    var name: String = ""

    @Ignore
    constructor()

    constructor(name: String) {
        this.name = name
    }
}