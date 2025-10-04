// feature/rent/NfcScanStrategy.kt
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
import java.util.concurrent.atomic.AtomicBoolean

class NfcScanStrategy(
    private val onTagDetected: () -> Unit // acción a ejecutar (navegar / llamar función)
) : ScanStrategy {

    private var adapter: NfcAdapter? = null
    private var enabled = false

    // Evitar múltiples ejecuciones y falsos positivos
    private val fired = AtomicBoolean(false)
    private val mainHandler = Handler(Looper.getMainLooper())
    private var ignoreFirst = false
    private var lastUid: String? = null
    private var lastTs: Long = 0L

    private fun uidOf(tag: Tag): String =
        tag.id?.joinToString(":") { "%02X".format(it) } ?: "NO_UID"

    private val readerCallback = NfcAdapter.ReaderCallback { tag: Tag? ->
        if (tag == null) return@ReaderCallback

        // 1) Ignorar el primer callback tras habilitar el reader
        if (ignoreFirst) {
            ignoreFirst = false
            Log.d("NFC", "Primer callback ignorado")
            return@ReaderCallback
        }

        // 2) Debounce por UID/tiempo para evitar dobles disparos
        val now = System.currentTimeMillis()
        val uid = uidOf(tag)
        if (uid == lastUid && (now - lastTs) < 800) {
            Log.d("NFC", "Callback duplicado ignorado (uid=$uid)")
            return@ReaderCallback
        }
        lastUid = uid
        lastTs = now

        Log.d("NFC", "Tag detectado (uid=$uid, techs=${tag.techList.joinToString()})")

        // Feedback opcional
        beep()

        // 3) Ejecutar acción SOLO una vez por activación, en el hilo principal
        if (fired.compareAndSet(false, true)) {
            mainHandler.post {
                try { onTagDetected() }
                catch (e: Exception) { Log.e("NFC", "Error en onTagDetected()", e) }
            }
        }
    }

    override fun start(activity: Activity) {
        if (enabled) return
        val adapter = NfcAdapter.getDefaultAdapter(activity)
        requireNotNull(adapter) { "Este dispositivo no soporta NFC" }

        // Flags estrictos: sólo tipo A/B. Quitamos BARCODE y SKIP_NDEF_CHECK para reducir ruido.
        val flags = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B

        val extras = Bundle() // opcional (p.ej. presencia)

        adapter.enableReaderMode(activity, readerCallback, flags, extras)

        // Preparar filtros anti-fantasma y reset de estado
        ignoreFirst = true
        lastUid = null
        lastTs = 0L
        fired.set(false)

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

    @Composable override fun render() { Text("Acerca un tag NFC…") }
    override fun onNewIntent(intent: Intent) { /* no-op (ReaderMode) */ }

    private fun beep() {
        try {
            ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100)
                .startTone(ToneGenerator.TONE_PROP_BEEP, 150)
        } catch (_: Exception) {}
    }
}
