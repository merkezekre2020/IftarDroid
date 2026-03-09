package com.example.iftardroid.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.iftardroid.model.AladhanResponse
import com.example.iftardroid.model.City
import com.example.iftardroid.model.District
import com.example.iftardroid.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = AppRepository()

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities.asStateFlow()

    private val _districts = MutableStateFlow<List<District>>(emptyList())
    val districts: StateFlow<List<District>> = _districts.asStateFlow()

    private val _prayerTimes = MutableStateFlow<AladhanResponse?>(null)
    val prayerTimes: StateFlow<AladhanResponse?> = _prayerTimes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchCities()
    }

    private fun fetchCities() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getCities()
            if (result.isSuccess) {
                // Sort cities alphabetically
                val sortedCities = result.getOrNull()?.sortedBy { it.name } ?: emptyList()
                _cities.value = sortedCities
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Bilinmeyen bir hata oluştu."
            }
            _isLoading.value = false
        }
    }

    fun onCitySelected(city: City) {
        // Sort districts alphabetically
        _districts.value = city.districts.sortedBy { it.name }
        fetchPrayerTimes(city.name)
    }

    private fun fetchPrayerTimes(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getPrayerTimes(city)
            if (result.isSuccess) {
                _prayerTimes.value = result.getOrNull()
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Vakitler alınamadı."
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
