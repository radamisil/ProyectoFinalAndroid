package com.visight.adondevamos.ui.main.place.report

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import io.reactivex.disposables.CompositeDisposable
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class ReportFromPlaceActivityPresenter: ReportFromPlaceActivityContract.Presenter {
    var mView: ReportFromPlaceActivityContract.View? = null
    var absolutePath = ""
    var mCompositeDisposable: CompositeDisposable? = null

    override fun startView(@NonNull view: ReportFromPlaceActivityContract.View) {
        mView = view
    }

    override fun onDestroy() {
        mView = null
    }

    override fun takePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(mView!!.getContext().packageManager) != null) {
            val photoFile = createImageFile()
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val mOutputFileUri = FileProvider.getUriForFile(mView!!.getContext(),
                        mView!!.getContext().packageName + ".provider", photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri)
                cameraIntent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                mView!!.takePhotoIntent(cameraIntent)
            }
        }
    }

    override fun takePhotoResult() {
        mView!!.displayImage(absolutePath)
    }

    override fun sendReport() {

    }

    // Create the File where the photo should go
    fun createImageFile(): File? {
        val root = File(mView!!.getContext().filesDir, "photos")
        if (!(root.mkdirs() || root.isDirectory())) {
            mView!!.displayMessage("Unable to save file")
            return null
        }

        val fname = "TMP_" + Calendar.getInstance().getTimeInMillis() + ".jpeg"
        val storageDirectory = File(root, fname)
        absolutePath = storageDirectory.getAbsolutePath()
        return storageDirectory
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toBase64(image: Bitmap): String {
        val output = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 65, output)
        val bytes = output.toByteArray()
        return "data:image/jpeg;base64," + Base64.getEncoder().encode(bytes)
    }
}