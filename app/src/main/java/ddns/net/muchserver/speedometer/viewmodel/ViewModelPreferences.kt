package ddns.net.muchserver.speedometer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import ddns.net.muchserver.speedometer.preferences.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelPreferences(application: Application): AndroidViewModel(application) {
    private val repository = DataStoreRepository(application)

    val readFromDataStore = repository.readFromDataStore.asLiveData()

    fun saveToDataStore(key: String, any: Any) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveToDataStore(key, any)
    }
}