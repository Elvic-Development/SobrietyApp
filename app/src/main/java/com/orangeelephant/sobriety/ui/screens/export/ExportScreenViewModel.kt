package com.orangeelephant.sobriety.ui.screens.export

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.orangeelephant.sobriety.ApplicationDependencies

class ExportScreenViewModel: ViewModel() {

    fun onLaunchSelectFolder(launcher: ManagedActivityResultLauncher<String, Uri?>) {
        launcher.launch(suggestBackupFileName())
    }

    fun onLaunchSelectImportFile(launcher: ManagedActivityResultLauncher<Array<String>, Uri?>) {
        launcher.launch(arrayOf("application/*"))
    }


    // TODO run in coroutine and maybe add loading UI incase file size gets bigger in future...
    // also add toasts for if things worked / didnt
    fun onExportPlaintextDatabase(uri: Uri, context: Context) {
        val outputStream = context.contentResolver.openOutputStream(uri)
        outputStream?.let {
            ApplicationDependencies.getDatabase().exportPlaintextDatabase(context, it)
            it.close()
        }
    }

    fun onImportPlaintextDatabase(uri: Uri, context: Context) {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.let {
            ApplicationDependencies.getDatabase().importPlaintextDatabase(context, it)
            it.close()
        }
    }

    private fun suggestBackupFileName(): String {
        // TODO make each unique
        return "lotus_db_export.db"
    }
}