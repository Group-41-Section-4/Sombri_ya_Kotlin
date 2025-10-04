package com.example.sombriyakotlin.feature.rent

import android.app.Activity
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.sombriyakotlin.ui.rent.Scan.ScanStrategy
import java.lang.ref.WeakReference

class NfcScanStrategy(
    private val onTagDetected: (stationId: String) -> Unit
) : ScanStrategy {

    private var adapter: NfcAdapter? = null
    private var enabled = false
    private val mainHandler = Handler(Looper.getMainLooper())
    private var activityRef: WeakReference<Activity>? = null
    private var firing = false

    private val toneGen by lazy { ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100) }

    private fun uidOf(tag: Tag): String =
        tag.id?.joinToString(":") { "%02X".format(it) } ?: "NO_UID"

    // 🔔 Beep
    private fun beep(ms: Int = 150) {
        mainHandler.post {
            try { toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, ms) } catch (_: Exception) {}
        }
    }

    // 📳 Vibración corta
    private fun buzz(activity: Activity?, ms: Long = 60) {
        if (activity == null) return
        try {
            val v = activity.getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                v.vibrate(android.os.VibrationEffect.createOneShot(ms, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION") v.vibrate(ms)
            }
        } catch (_: Exception) { }
    }

    private val readerCallback = NfcAdapter.ReaderCallback { tag: Tag? ->
        Log.d("NFC", "📡 ReaderCallback triggered... tag=$tag")
        val act = activityRef?.get()

        if (tag == null) {
            Log.d("NFC", "❌ Tag es null")
            buzz(act, 80)
            return@ReaderCallback
        }

        if (firing) {
            Log.d("NFC", "⚠️ Ya se está procesando otro tag, ignorando…")
            return@ReaderCallback
        }
        firing = true

        val uid = uidOf(tag)
        val techs = tag.techList.joinToString()
        Log.d("NFC", "✅ Tag detectado - UID=$uid")
        Log.d("NFC", "💾 Tecnologías disponibles: $techs")

        // Feedback inmediato al detectar
        beep(180); buzz(act, 70)

        // Estilo FlutterNfcKit: poll() -> delay(1s) -> finish()
        mainHandler.postDelayed({
            activityRef?.get()?.let { safeStop(it) }
            try {
                Log.d("ZZZZZZZZZZZZZZZZZZZZZZZ", "🚀 Ejecutando onTagDetected con uid=$uid")
                onTagDetected(uid)
            } catch (e: Exception) {
                Log.e("NFC", "❌ Error en onTagDetected(uid)", e)
            } finally {
                firing = false
            }
        }, 1000L)
    }

    override fun start(activity: Activity) {
        Log.d("NFC", "🔛 start() llamado")
        if (enabled) {
            Log.d("NFC", "⚠️ ReaderMode ya estaba habilitado")
            // Feedback igual para el usuario
            beep(100); buzz(activity, 40)
            return
        }

        val adapter = NfcAdapter.getDefaultAdapter(activity)
            ?: run {
                Log.d("NFC", "❌ getDefaultAdapter() = null")
                throw IllegalStateException("Este dispositivo no soporta NFC")
            }

        if (!adapter.isEnabled) {
            Log.d("NFC", "⚠️ NFC está APAGADO en el sistema")
            throw IllegalStateException("NFC deshabilitado en el sistema")
        }

        activityRef = WeakReference(activity)

        // Dejamos sonidos de sistema ACTIVOS para escuchar el beep del sistema al detectar tag
        val flags =
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NFC_BARCODE or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
        // NO usamos FLAG_READER_NO_PLATFORM_SOUNDS mientras diagnosticamos

        val extras = Bundle().apply {
            putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
        }

        adapter.enableReaderMode(activity, readerCallback, flags, extras)

        this.adapter = adapter
        enabled = true
        firing = false
        Log.d("NFC", "✅ ReaderMode ENABLED (A|B|F|V|BARCODE)")

        // Confirmación audible/táctil al activar
        beep(120); buzz(activity, 50)
    }

    override fun stop(activity: Activity) {
        safeStop(activity)
    }

    @Composable
    override fun render() {
        Text("Acerca una tarjeta o tag NFC…")
    }

    override fun onNewIntent(intent: Intent) {
        Log.d("NFC", "ℹ️ onNewIntent llamado (no usado en ReaderMode)")
    }

    private fun safeStop(activity: Activity) {
        if (!enabled) return
        try {
            adapter?.disableReaderMode(activity)
            Log.d("NFC", "🛑 ReaderMode DISABLED")
        } catch (e: Exception) {
            Log.e("NFC", "❌ Error al desactivar ReaderMode", e)
        } finally {
            enabled = false
        }
    }
}
