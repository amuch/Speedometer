package ddns.net.muchserver.speedometer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

const val TABLE_CHECKPOINT = "checkPoint"
const val COLUMN_ID_CHECKPOINT = "idCheckPoint"
const val COLUMN_ID_TRIP_FOREIGN = "idTrip"
const val COLUMN_LATITUDE = "latitude"
const val COLUMN_LONGITUDE = "longitude"
const val COLUMN_DATE = "date"

@Entity(
    tableName = TABLE_CHECKPOINT,
    foreignKeys = [ForeignKey(
        entity = Trip::class,
        parentColumns = arrayOf(COLUMN_ID_TRIP),
        childColumns = arrayOf(COLUMN_ID_TRIP_FOREIGN),
        onDelete = ForeignKey.CASCADE
    )]
)
class CheckPoint {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID_CHECKPOINT)
    var id: Long = 0L

    @ColumnInfo(name = COLUMN_ID_TRIP_FOREIGN)
    var idTrip: Long = 0L
//    @Embedded
//    var trip: Trip = Trip("Default")

    @ColumnInfo(name = COLUMN_LATITUDE)
    var latitude: Double = 0.0

    @ColumnInfo(name = COLUMN_LONGITUDE)
    var longitude: Double = 0.0

    @ColumnInfo(name = COLUMN_DATE)
    var date: Date = Date()

    @Ignore
    constructor()

    constructor(
        idTrip: Long,
        latitude: Double,
        longitude: Double,
        date: Date
    ) {
        this.idTrip = idTrip
        this.latitude = latitude
        this.longitude = longitude
        this.date = date
    }
}