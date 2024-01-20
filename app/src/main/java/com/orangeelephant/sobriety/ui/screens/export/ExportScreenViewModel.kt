package com.orangeelephant.sobriety.ui.screens.export

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.util.DateTimeFormatUtil

class ExportScreenViewModel: ViewModel() {

    fun onLaunchSelectFolder(launcher: ManagedActivityResultLauncher<String, Uri?>, context: Context) {
        launcher.launch(suggestBackupFileName(context))
    }

    /* TODO run in coroutine and maybe add loading UI incase file size gets bigger in future...
     * also add toasts for if things worked / didnt
     */
    fun onExportPlaintextDatabase(uri: Uri, context: Context) {
        val outputStream = context.contentResolver.openOutputStream(uri)
        outputStream?.let {
            ApplicationDependencies.getDatabase().exportPlaintextDatabase(context, it)
            it.close()
        }
    }

    private fun suggestBackupFileName(context: Context): String {
        return "lotus_export_${DateTimeFormatUtil.currentTimeStampForBackupFile(context)}.db"
    }
}