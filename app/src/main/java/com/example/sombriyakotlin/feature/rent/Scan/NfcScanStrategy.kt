package com.example.sombriyakotlin.feature.rent.Scan

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.nio.charset.Charset

class NfcScanStrategy : ScanStrategy {

    private var adapter: NfcAdapter? = null
    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result

    private val reader = NfcAdapter.ReaderCallback { tag: Tag? ->
        try {
            val ndef = Ndef.get(tag)
            if (ndef != null) {
                ndef.connect()
                val msg = ndef.ndefMessage
                ndef.close()
                _result.value = msg?.records?.joinToString("\n") { rec ->
                    if (rec.tnf.toInt() == 1 && rec.type.contentEquals(byteArrayOf(0x54)))
                        decodeText(rec.payload)
                    else "Otro record"
                } ?: "NDEF vacío"
            } else {
                _result.value = "Etiqueta no NDEF"
            }
        } catch (e: Exception) {
            _result.value = "Error: ${e.message}"
        }
    }

    override fun start(activity: Activity) {
        println("Hello World")
        adapter = NfcAdapter.getDefaultAdapter(activity)
        adapter?.enableReaderMode(
            activity, reader,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V,
            Bundle()
        )
    }

    override fun stop(activity: Activity) {
        adapter?.disableReaderMode(activity)
    }

    @Composable
    override fun Render() {
        val res by result.collectAsState(null)
        Text(res ?: "Acerca una tarjeta NFC…")
    }

    private fun decodeText(payload: ByteArray): String {
        val status = payload[0].toInt()
        val langLen = status and 0x3F
        val utf8 = (status and 0x80) == 0
        val charset = if (utf8) Charsets.UTF_8 else Charset.forName("UTF-16")
        return String(payload, 1 + langLen, payload.size - 1 - langLen, charset)
    }
}
