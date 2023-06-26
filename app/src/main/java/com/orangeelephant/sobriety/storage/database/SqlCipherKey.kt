package com.orangeelephant.sobriety.storage.database

import com.orangeelephant.sobriety.util.Argon2

class SqlCipherKey(
    isEncrypted: Boolean,
    password: ByteArray? = null,
    salt: ByteArray? = null
) {
    private var key: ByteArray?
    val keyBytes: ByteArray?
        get() = key

    init {
        key = if (!isEncrypted) {
            null
        } else if (password != null && salt != null) {
            val argon2 = Argon2()
            argon2.hash(password, salt).encodedOutputAsByteArray()
        } else {
            throw IllegalArgumentException("Tried to set key for encrypted database without providing password or salt")
        }
    }
}