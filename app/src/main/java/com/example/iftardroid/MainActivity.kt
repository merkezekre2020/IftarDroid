package com.example.iftardroid

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iftardroid.databinding.ActivityMainBinding
import com.example.iftardroid.model.City
import com.example.iftardroid.model.PrayerTimings
import com.example.iftardroid.ui.MainViewModel
import com.example.iftardroid.ui.PrayerTimeAdapter
import com.example.iftardroid.ui.PrayerTimeItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val prayerAdapter = PrayerTimeAdapter()

    private var currentCityList: List<City> = emptyList()
    private var countdownJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        setupListeners()
    }

    private fun setupRecyclerView() {
        binding.rvPrayerTimes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = prayerAdapter
        }
    }

    private fun setupListeners() {
        binding.actvCity.setOnItemClickListener { _, _, position, _ ->
            val selectedCity = currentCityList[position]
            viewModel.onCitySelected(selectedCity)
            binding.actvDistrict.text.clear() // Clear district selection when city changes
            binding.tvCountdownTime.text = "00:00:00"
            prayerAdapter.submitList(emptyList())
        }

        binding.actvDistrict.setOnItemClickListener { _, _, _, _ ->
             // For now we don't fetch separately by district as Aladhan API might only support cities reliably
             // But UI is ready to support it if needed.
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.cities.collect { cities ->
                        currentCityList = cities
                        val cityNames = cities.map { it.name }
                        val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_dropdown_item_1line, cityNames)
                        binding.actvCity.setAdapter(adapter)
                    }
                }

                launch {
                    viewModel.districts.collect { districts ->
                        val districtNames = districts.map { it.name }
                        val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_dropdown_item_1line, districtNames)
                        binding.actvDistrict.setAdapter(adapter)
                    }
                }

                launch {
                    viewModel.prayerTimes.collect { response ->
                        response?.let {
                            val timings = it.data.timings
                            updatePrayerList(timings)
                            startCountdown(timings)
                        }
                    }
                }

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
                    }
                }

                launch {
                    viewModel.errorMessage.collect { error ->
                        error?.let {
                            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                            viewModel.clearError()
                        }
                    }
                }
            }
        }
    }

    private fun updatePrayerList(timings: PrayerTimings) {
        val list = listOf(
            PrayerTimeItem(getString(R.string.fajr), timings.fajr),
            PrayerTimeItem(getString(R.string.sunrise), timings.sunrise),
            PrayerTimeItem(getString(R.string.dhuhr), timings.dhuhr),
            PrayerTimeItem(getString(R.string.asr), timings.asr),
            PrayerTimeItem(getString(R.string.maghrib), timings.maghrib),
            PrayerTimeItem(getString(R.string.isha), timings.isha)
        )
        prayerAdapter.submitList(list)
    }

    private fun startCountdown(timings: PrayerTimings) {
        countdownJob?.cancel()
        countdownJob = lifecycleScope.launch {
            while (true) {
                val now = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                // Parse timings
                val iftarTime = dateFormat.parse(timings.maghrib)
                val sahurTime = dateFormat.parse(timings.fajr)

                if (iftarTime == null || sahurTime == null) break

                val iftarCal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, iftarTime.hours)
                    set(Calendar.MINUTE, iftarTime.minutes)
                    set(Calendar.SECOND, 0)
                }

                val sahurCal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, sahurTime.hours)
                    set(Calendar.MINUTE, sahurTime.minutes)
                    set(Calendar.SECOND, 0)
                }

                // Determine target
                var targetCal: Calendar
                var isIftarNext: Boolean

                if (now.before(iftarCal) && now.after(sahurCal)) {
                    // It's after sahur but before iftar -> count down to iftar
                    targetCal = iftarCal
                    isIftarNext = true
                } else {
                    // It's after iftar or before sahur -> count down to sahur
                    targetCal = sahurCal
                    isIftarNext = false
                    if (now.after(iftarCal)) {
                        targetCal.add(Calendar.DAY_OF_YEAR, 1) // Next day's sahur
                    }
                }

                val diffMillis = targetCal.timeInMillis - now.timeInMillis

                if (diffMillis > 0) {
                    val seconds = (diffMillis / 1000) % 60
                    val minutes = (diffMillis / (1000 * 60)) % 60
                    val hours = (diffMillis / (1000 * 60 * 60)) % 24

                    binding.tvCountdownTime.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    binding.tvCountdownLabel.text = if (isIftarNext) getString(R.string.iftar_countdown) else getString(R.string.sahur_countdown)
                }

                delay(1000)
            }
        }
    }
}
