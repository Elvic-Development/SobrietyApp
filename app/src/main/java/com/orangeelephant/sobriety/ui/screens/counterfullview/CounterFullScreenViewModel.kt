package com.orangeelephant.sobriety.ui.screens.counterfullview

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.models.Reason
import com.orangeelephant.sobriety.storage.models.Relapse
import com.orangeelephant.sobriety.storage.repositories.CounterRepository
import com.orangeelephant.sobriety.storage.repositories.DatabaseCounterRepository
import com.orangeelephant.sobriety.util.DateTimeFormatUtil
import com.orangeelephant.sobriety.util.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.sqlcipher.CursorIndexOutOfBoundsException

class CounterFullScreenViewModel(
    private val counterId: Int,
    private val counterRepository: CounterRepository = DatabaseCounterRepository()
): ViewModel() {
    companion object {
        private val TAG = CounterFullScreenViewModel::class.java.simpleName
    }

    val duration: MutableState<Duration> = mutableStateOf(Duration())
    val counter: MutableState<Counter?> = mutableStateOf(null)
    val reasons: SnapshotStateList<Reason> = mutableStateListOf()
    val relapses: SnapshotStateList<Relapse> = mutableStateListOf()

    init {
        LogEvent.i(TAG, "Creating view model for counter id $counterId")
        counter.value = try {
            counterRepository.getCounter(counterId)
        } catch (e: CursorIndexOutOfBoundsException) {
            null
        }

        counter.value?.let { counter ->
            relapses.apply {
                addAll(counterRepository.getRelapsesForCounter(counter.id))
                // display the initial start time at end if available
                counter.initialStartTime?.let {initialStartTime ->
                    add(Relapse(-1, counterId, initialStartTime, null, counter.creationTime))
                }
            }
            reasons.apply {
                addAll(counterRepository.getReasonsForCounter(counter.id))
            }
        }

        viewModelScope.launch {
            while (true) {
                counter.value?.let {
                    duration.value = DateTimeFormatUtil.getDurationFromStartTime(it.startTimeMillis)
                }
                delay(1000)
            }
        }
    }

    fun onResetCounter(timeOfRelapse: Long, comment: String?) {
        val newRecord = counterRepository.resetCounter(counterId, timeOfRelapse, comment)
        counter.value = counter.value?.copy(
            startTimeMillis = timeOfRelapse,
            recordTimeSoberInMillis = newRecord,
            currentDurationString = DateTimeFormatUtil.formatDurationAsString(timeOfRelapse)
        )

        relapses.apply {
            add(Relapse(-1, counterId, timeOfRelapse, comment, System.currentTimeMillis()))
        }
    }

    fun onDeleteCounter(context: Context, popBack: () -> Unit) {
        counterRepository.deleteCounter(counterId)
        Toast.makeText(context, R.string.deleted_successfully, Toast.LENGTH_LONG).show()
        popBack()
    }

    fun onAddReason(reason: String) {
        counterRepository.addReasonForCounter(counterId, reason)

        reasons.apply {
            add(Reason(-1, counterId, reason))
        }
    }
}