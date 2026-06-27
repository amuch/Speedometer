package ddns.net.muchserver.speedometer.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TripRepository(private val tripDao: TripDao, private val checkPointDao: CheckPointDao) {
    val trip = MutableLiveData<Trip>()
    val checkPoints: LiveData<List<CheckPoint>> = checkPointDao.getAllCheckPoints()
    var checkPointsCurrent: LiveData<List<CheckPoint>> = checkPointDao.getCheckPointsCurrent(0)

    val trips: LiveData<List<Trip>> = tripDao.getAllTrips()
    val checkPoint = MutableLiveData<CheckPoint>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertTrip(trip: Trip) {
        coroutineScope.launch(Dispatchers.IO) {
            tripDao.insertTrip(trip)
        }
    }

    fun insertCheckPoint(checkPoint: CheckPoint) {
        coroutineScope.launch(Dispatchers.IO) {
            checkPointDao.insertCheckPoint(checkPoint)
        }
    }

    fun deleteTrip(idTrip: Long) {
        coroutineScope.launch(Dispatchers.IO) {
            tripDao.deleteTrip(idTrip)
        }
    }

    fun deleteCheckPoint(idCheckPoint: Long) {
        coroutineScope.launch(Dispatchers.IO) {
            checkPointDao.deleteCheckPoint(idCheckPoint)
        }
    }

    fun findTrip(name: String) {
        coroutineScope.launch(Dispatchers.Main) {
            trip.value = asyncFindTrip(name).await()
        }
    }

    fun findTrip(idTrip: Long) {
        coroutineScope.launch(Dispatchers.Main) {
            trip.value = asyncFindTrip(idTrip).await()
//            println("Trip ${trip.value!!.id}")
            checkPointsCurrent = checkPointDao.getCheckPointsCurrent(idTrip)
//            println("Size: ${checkPointsCurrent.value!!.size}")
//            for(checkPointCurrent in checkPointsCurrent.value!!) {
//                println("${checkPointCurrent.id} ${checkPointCurrent.date}")
//            }
        }
    }

    fun findCheckPoint(idCheckPoint: Long) {
        coroutineScope.launch(Dispatchers.Main) {
            checkPoint.value = asyncFindCheckPoint(idCheckPoint).await()
        }
    }

    private fun asyncFindTrip(idTrip: Long): Deferred<Trip?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async tripDao.findTrip(idTrip)
        }
    private fun asyncFindTrip(name: String): Deferred<Trip?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async tripDao.findTrip(name)
        }

    private fun asyncFindCheckPoint(idCheckPoint: Long): Deferred<CheckPoint?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async checkPointDao.findCheckPoint(idCheckPoint)
        }
}