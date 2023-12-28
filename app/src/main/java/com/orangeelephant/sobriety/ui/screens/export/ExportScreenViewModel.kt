package com.orangeelephant.sobriety.ui.screens.export

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.R

class ExportScreenViewModel: ViewModel() {

    fun onLaunchSelectFolder(launcher: ManagedActivityResultLauncher<String, Uri?>) {
        launcher.launch(suggestBackupFileName())
    }

    fun onLaunchSelectImportFile(launcher: ManagedActivityResultLauncher<Array<String>, Uri?>) {
        launcher.launch(arrayOf("*/*"))
    }


    fun onExportPlaintextDatabase(uri: Uri, context: Context) {
        var success = false
        val outputStream = context.contentResolver.openOutputStream(uri)
        outputStream?.let {
            success = ApplicationDependencies.getDatabase().exportPlaintextDatabase(context, it)
            it.close()
        }

        if (success) {
            Toast.makeText(context, context.getString(R.string.exported_database_successfully), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, context.getString(R.string.failed_to_write_file), Toast.LENGTH_LONG).show()
        }
    }

    fun onImportPlaintextDatabase(uri: Uri, context: Context) {
        var success = false
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.let {
            success = ApplicationDependencies.getDatabase().importPlaintextDatabase(context, it)
            it.close()
        }
        // TODO toasts and maybe redirect
    }

    private fun suggestBackupFileName(): String {
        return "lotus_db_export.db"
    }
}