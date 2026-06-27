package ddns.net.muchserver.speedometer.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ddns.net.muchserver.speedometer.database.CheckPoint
import ddns.net.muchserver.speedometer.database.TripDatabase
import ddns.net.muchserver.speedometer.database.TripRepository
import ddns.net.muchserver.speedometer.database.Trip
import java.util.Date


class ViewModelTrip(application: Application): ViewModel() {

    private val tripRepository: TripRepository
    val trip: MutableLiveData<Trip>
    val trips: LiveData<List<Trip>>
    val checkPoints: LiveData<List<CheckPoint>>
    val checkPointsCurrent: LiveData<List<CheckPoint>>
    val tripInProcess: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        val tripDatabase = TripDatabase.getInstance(application)
        val tripDao = tripDatabase.tripDao()
        val checkPointDao = tripDatabase.checkPointDao()

        tripRepository = TripRepository(tripDao, checkPointDao)
        trip = tripRepository.trip
        trips = tripRepository.trips
        checkPoints = tripRepository.checkPoints
        checkPointsCurrent = tripRepository.checkPointsCurrent
    }

    fun insertTrip(trip: Trip) {
       tripRepository.insertTrip(trip)
    }

    fun startTrip() {
        val date = Date()
        val trip = Trip(date.toString())
        insertTrip(trip)
        tripInProcess.value = true
        println("Start Trip")
    }

    fun stopTrip() {
        tripInProcess.value = false
        println("Stop Trip")
    }

    fun insertCheckPoint(checkPoint: CheckPoint) {
        tripRepository.insertCheckPoint(checkPoint)
    }

    fun deleteTrip(idTrip: Long) {
        tripRepository.deleteTrip(idTrip)
    }

    fun findTrip(idTrip: Long) {
        tripRepository.findTrip(idTrip)
    }

    fun findTrip(name: String) {
        tripRepository.findTrip(name)
    }

    fun findCheckPoint(idCheckPoint: Long) {
        tripRepository.findCheckPoint(idCheckPoint)
    }
}