package com.example.sombriyakotlin.data.steps

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.sombriyakotlin.domain.model.PedometerRepository
import com.example.sombriyakotlin.domain.model.StepEvent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Singleton

@Singleton
class AndroidPedometerRepository(
    @ApplicationContext private val context: Context,
    private val sensorManager: SensorManager
) : PedometerRepository
{
    private val _events = MutableSharedFlow<StepEvent>(replay = 1)
    override fun observeStepEvents(): Flow<StepEvent> = _events.asSharedFlow()

    private var listener: SensorEventListener? = null
    private var started = false

    // Preferimos detector; si no existe usamos counter (y manejamos offset)
    private val detector: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    private val counter: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    private val prefs: SharedPreferences =
        context.getSharedPreferences("pedometer_prefs", Context.MODE_PRIVATE)

    private var lastCounterAbsolute: Int? = null
    private var savedOffset: Int
        get() = prefs.getInt("step_offset", 0)
        set(value) = prefs.edit().putInt("step_offset", value).apply()

    override suspend fun start() {
        if (started) return

        val chosenSensor = detector ?: counter
        chosenSensor ?: throw IllegalStateException("No step sensor available on device")

        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val now = System.currentTimeMillis()
                val steps = when (event.sensor.type) {
                    Sensor.TYPE_STEP_DETECTOR -> {
                        // detector típicamente reporta 1.0 por paso; sumamos posibles valores
                        event.values.sumOf { it.toInt() }
                    }
                    Sensor.TYPE_STEP_COUNTER -> {
                        val absolute = event.values[0].toInt()
                        // si es primera lectura, guardamos base
                        if (lastCounterAbsolute == null) lastCounterAbsolute = absolute
                        val relative = absolute - (lastCounterAbsolute ?: absolute) - savedOffset
                        relative.coerceAtLeast(0)
                    }
                    else -> 0
                }
                // emitir en el mismo hilo del callback de forma ligera
                _events.tryEmit(StepEvent(timestamp = now, steps = steps))
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, chosenSensor, SensorManager.SENSOR_DELAY_NORMAL)
        started = true
    }

    override suspend fun stop() {
        listener?.let { sensorManager.unregisterListener(it) }
        listener = null
        started = false
        lastCounterAbsolute = null
    }

    override suspend fun reset() {
        // Para TYPE_STEP_COUNTER ajustamos offset para que el próximo periodo empiece en 0
        // Si no hay lectura absoluta aún, guardamos offset = 0 y la primera lectura será base
        val currentAbsolute = lastCounterAbsolute ?: 0
        savedOffset = currentAbsolute
    }

    override fun isRunning(): Boolean = started
}

@Module
@InstallIn(SingletonComponent::class)
object PedometerDataModule {
    @Provides
    @Singleton
    fun provideSensorManager(@ApplicationContext ctx: Context): SensorManager =
        ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Provides
    @Singleton
    fun providePedometerRepository(
        @ApplicationContext ctx: Context,
        sensorManager: SensorManager
    ): PedometerRepository = AndroidPedometerRepository(ctx, sensorManager)
}
