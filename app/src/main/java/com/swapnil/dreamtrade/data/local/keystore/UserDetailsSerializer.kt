package com.swapnil.dreamtrade.data.local.keystore

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.Serializer
import com.swapnil.dreamtrade.domain.model.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

object UserDetailsSerializer: Serializer<UserDetails> {
    override val defaultValue: UserDetails
        get() = UserDetails()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun readFrom(input: InputStream): UserDetails {
        val encryptedBytes = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }
        val encryptedBytesDecoded = Base64.getDecoder().decode(encryptedBytes)
        val decryptedBytes = Crypto.decrypt(encryptedBytesDecoded)
        val decodedJsonString = decryptedBytes.decodeToString()
        return Json.decodeFromString(decodedJsonString)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun writeTo(t: UserDetails, output: OutputStream) {
        val json = Json.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = Crypto.encrypt(bytes)
        val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)
        withContext(Dispatchers.IO) {
            output.use {
                it.write(encryptedBytesBase64)
            }
        }
    }
}