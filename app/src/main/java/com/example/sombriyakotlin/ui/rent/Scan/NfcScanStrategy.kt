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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import com.example.sombriyakotlin.ui.navigation.Routes


class NfcScanStrategy(
    private val onTagDetected: (stationId: String) -> Unit
) : ScanStrategy {

    private var adapter: NfcAdapter? = null
    private var enabled = false
    private val mainHandler = Handler(Looper.getMainLooper())

    private fun uidOf(tag: Tag): String =
        tag.id?.joinToString(":") { "%02X".format(it) } ?: "NO_UID"


    private val readerCallback = NfcAdapter.ReaderCallback { tag: Tag? ->
        if (tag == null) return@ReaderCallback

        val uid = "acadc4ef-f5b3-4ab8-9ab5-58f1161f0799" //uidOf(tag)
        Log.d("NFC", "Tag detectado (uid=$uid, techs=${tag.techList.joinToString()})")
        beep()

        // Ejecutar acción en hilo principal
        mainHandler.post {
            try {
                Log.d("RENT", "Se manda a crear la reserva")

                onTagDetected(uid)
            } catch (e: Exception) {
                Log.e("NFC", "Error en onTagDetected(uid)", e)
            }
        }
    }

    override fun start(activity: Activity) {
        if (enabled) return
        val adapter = NfcAdapter.getDefaultAdapter(activity)
        requireNotNull(adapter) { "Este dispositivo no soporta NFC" }

        val flags = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B
        val extras = Bundle()

        adapter.enableReaderMode(activity, readerCallback, flags, extras)

        this.adapter = adapter
        enabled = true
        Log.d("NFC", "ReaderMode ENABLED")
    }

    override fun stop(activity: Activity) {
        if (!enabled) return
        try {
            adapter?.disableReaderMode(activity)
            Log.d("NFC", "ReaderMode DISABLED")
        } catch (_: Exception) { /* no-op */ }
        enabled = false
    }

    @Composable
    override fun render() {
        Text("Acerca un tag NFC…")
    }

    override fun onNewIntent(intent: Intent) {
        // No-op en ReaderMode
    }

    private fun beep() {
        try {
            ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
                .startTone(ToneGenerator.TONE_PROP_BEEP, 150)
        } catch (_: Exception) {}
    }
}
