package com.orangeelephant.sobriety.ui.screens.counterfullview

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.models.Relapse
import com.orangeelephant.sobriety.storage.repositories.CounterRepository
import com.orangeelephant.sobriety.storage.repositories.DatabaseCounterRepository
import com.orangeelephant.sobriety.util.CounterViewUtil
import com.orangeelephant.sobriety.util.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.sqlcipher.CursorIndexOutOfBoundsException
import java.util.Calendar

class CounterFullScreenViewModel(
    private val counterId: Long,
    private val counterRepository: CounterRepository = DatabaseCounterRepository()
): ViewModel() {
    companion object {
        private val TAG = CounterFullScreenViewModel::class.java.simpleName
    }

    val duration: MutableState<Duration> = mutableStateOf(Duration())
    val counter: MutableState<Counter?> = mutableStateOf(null)
    val relapses: SnapshotStateList<Relapse> = mutableStateListOf()

    init {
        LogEvent.i(TAG, "Creating view model for counter id $counterId")
        counter.value = try {
            counterRepository.getCounter(counterId)
        } catch (e: CursorIndexOutOfBoundsException) {
            null
        }

        counter.value?.let {
            relapses.apply {
                addAll(counterRepository.getRelapsesForCounter(it.id))
            }
        }

        viewModelScope.launch {
            while (true) {
                counter.value?.let {
                    duration.value = CounterViewUtil.getDurationFromStartTime(it.startTimeMillis)
                }
                delay(1000)
            }
        }
    }

    fun onResetCounter(comment: String?) {
        val startTimeMillis = Calendar.getInstance().timeInMillis
        val newRecord = counterRepository.resetCounter(counterId, comment)
        counter.value = counter.value?.copy(
            startTimeMillis = startTimeMillis,
            recordTimeSoberInMillis = newRecord,
            currentDurationString = CounterViewUtil.formatDurationAsString(startTimeMillis)
        )

        relapses.apply {
            add(Relapse(-1, counterId, startTimeMillis, comment))
        }
    }

    fun onDeleteCounter(context: Context, navController: NavController) {
        counterRepository.deleteCounter(counterId)
        Toast.makeText(context, R.string.deleted_successfully, Toast.LENGTH_LONG).show()
        navController.popBackStack()
    }
}