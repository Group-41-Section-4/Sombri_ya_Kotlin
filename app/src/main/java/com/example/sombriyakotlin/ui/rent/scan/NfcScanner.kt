package com.example.sombriyakotlin.feature.rent

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.lang.ref.WeakReference

class NfcScanner(
    private val onTagDetected: (tagId: String) -> Unit,
    private val onError: (errorMsg: String) -> Unit
) {

    private var adapter: NfcAdapter? = null
    private var enabled = false
    private val mainHandler = Handler(Looper.getMainLooper())
    private var activityRef: WeakReference<Activity>? = null
    private var firing = false

    private val toneGen by lazy { ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100) }

    private fun uidOf(tag: Tag): String =
        tag.id?.joinToString(":") { "%02X".format(it) } ?: "NO_UID"

    // 🔔 Sonido corto
    private fun beep(ms: Int = 150) {
        mainHandler.post {
            try {
                toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, ms)
            } catch (_: Exception) { }
        }
    }

    // 📳 Vibración corta
    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun buzz(activity: Activity?, ms: Long = 60) {
        if (activity == null) return
        try {
            val v = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION") v.vibrate(ms)
            }
        } catch (_: Exception) { }
    }

    // 🏷️ Callback principal cuando se detecta un tag NFC
    private val readerCallback = NfcAdapter.ReaderCallback @androidx.annotation.RequiresPermission(
        android.Manifest.permission.VIBRATE
    ) { tag: Tag? ->
        Log.d("NFC", "LEYENDO TAG = $tag")
        val act = activityRef?.get()

        if (tag == null) {
            Log.d("NFC", "Tag es null")
            return@ReaderCallback
        }

        if (firing) {
            Log.d("NFC", "Ya se está procesando otro tag, ignorando…")
            return@ReaderCallback
        }
        firing = true

        val uid = uidOf(tag)
        val techs = tag.techList.joinToString()
        Log.d("NFC", "Tag detectado - UID=$uid")

        // Feedback inmediato
        beep(180)
        // Delay corto y ejecutar callback externo
        mainHandler.postDelayed({
            activityRef?.get()?.let { safeStop(it) }
            try {
                Log.d("NFC", "Ejecutando onTagDetected con uid=$uid")
                onTagDetected(uid)
            } catch (e: Exception) {
                Log.e("NFC", "Error en onTagDetected(uid)", e)
            } finally {
                firing = false
            }
        }, 1000L)
    }

    // 🚀 Inicia el modo de lectura NFC
    @RequiresPermission(Manifest.permission.VIBRATE)
    fun start(activity: Activity) {
        Log.d("NFC", "start() llamado")
        if (enabled) {
            Log.d("NFC", "ReaderMode ya estaba habilitado")
            beep(100)
            buzz(activity, 40)
            return
        }

        val adapter = NfcAdapter.getDefaultAdapter(activity)
            ?: throw IllegalStateException("Este dispositivo no soporta NFC")

        if (!adapter.isEnabled) {
            throw IllegalStateException("NFC está deshabilitado en el sistema")
        }

        activityRef = WeakReference(activity)

        val flags =
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NFC_BARCODE or
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK

        val extras = Bundle().apply {
            putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
        }

        adapter.enableReaderMode(activity, readerCallback, flags, extras)

        this.adapter = adapter
        enabled = true
        firing = false
        Log.d("NFC", "ReaderMode ENABLED")

        beep(120)
        buzz(activity, 50)
    }

    // 🛑 Detiene la lectura NFC
    fun stop(activity: Activity) {
        safeStop(activity)
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
