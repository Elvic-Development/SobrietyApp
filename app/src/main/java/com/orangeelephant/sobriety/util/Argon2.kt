package com.orangeelephant.sobriety.util

import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2KtResult
import com.lambdapioneer.argon2kt.Argon2Mode
import com.lambdapioneer.argon2kt.Argon2Version

class Argon2 {
    companion object {
        private val VERSION = Argon2Version.V13
        private val MODE = Argon2Mode.ARGON2_I
        private const val LENGTH = 32
        private const val ITERATIONS = 5
        private const val MEMORY = 65536
    }

    fun hash(password: ByteArray, salt: ByteArray): Argon2KtResult {
        val argon2Kt = Argon2Kt()

        return argon2Kt.hash(
            mode = MODE,
            password = password,
            salt = salt,
            tCostInIterations = ITERATIONS,
            mCostInKibibyte = MEMORY,
            hashLengthInBytes = LENGTH,
            version = VERSION
        )
    }
}