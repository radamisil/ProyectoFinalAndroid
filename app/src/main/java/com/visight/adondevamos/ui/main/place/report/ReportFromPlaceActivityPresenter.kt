package com.visight.adondevamos.ui.main.place.report

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import com.visight.adondevamos.data.remote.AppServices
import com.visight.adondevamos.data.remote.requests.SendPlacePhotoRequest
import com.visight.adondevamos.data.remote.responses.AnalizedPhotoResponse
import com.visight.adondevamos.utils.Availability
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import android.util.Base64InputStream
import android.R.attr.bitmap
import android.util.Base64OutputStream
import com.google.android.gms.common.util.IOUtils.copyStream
import com.visight.adondevamos.utils.AppConstants
import java.io.*


class ReportFromPlaceActivityPresenter: ReportFromPlaceActivityContract.Presenter {
    var mView: ReportFromPlaceActivityContract.View? = null
    var absolutePath = ""
    var mDisposable: Disposable? = null
    var mSurveySelectedOption: String? = null

    var mPath = "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"

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

    override fun choosePhoto() {
        var galleryIntent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        mView!!.choosePhotoIntent(galleryIntent)
    }

    override fun takePhotoResult() {
        mView!!.displayImage(absolutePath)
    }

    override fun choosePhotoResult(data: Intent?) {
        //var inputStream = mView!!.getContext().getContentResolver().openInputStream(data.getData())
        //mView!!.displayImage(absolutePath)
        if(data != null){
            try {
                val imageUri = data.data
                val inputImageStream = mView!!.getContext().applicationContext
                        .contentResolver.openInputStream(imageUri!!)
                val photoFile = createImageFile()
                val fileOutputStream = FileOutputStream(photoFile)
                // Copying
                try {
                    inputImageStream!!.copyTo(fileOutputStream)
                    fileOutputStream.close()
                    inputImageStream.close()
                }catch (e: IOException) {
                    e.printStackTrace()
                }

                mView!!.displayImage(absolutePath)
            } catch (e: FileNotFoundException ) {
                e.printStackTrace()
            }
        }
    }

    override fun setSurveySelectedOption(selectedOption: String) {
        mSurveySelectedOption = selectedOption
    }

    override fun getSurveySelectedOption(): String? {
        return mSurveySelectedOption
    }

    override fun sendReport(placeId: String, shouldSendSurveySelectedOption: Boolean, capacity: Int) {
        val base64Image = setBase64ImageFromPath(absolutePath)

        val request: SendPlacePhotoRequest
        if(base64Image != null){
            if(!shouldSendSurveySelectedOption){
                //request = SendPlacePhotoRequest(placeId = placeId, imagenbase64 = base64Image, encuesta = null)
                request = SendPlacePhotoRequest(placeId = placeId, imagenbase64 = base64Image, encuesta = null, capacity = capacity)
            }else{
                    var selectedValue = ""
                    when(mSurveySelectedOption){
                        Availability.LOW.name -> selectedValue = Availability.LOW.value
                        Availability.MEDIUM.name -> selectedValue = Availability.MEDIUM.value
                        Availability.HIGH.name -> selectedValue = Availability.HIGH.value
                    }
                    request = SendPlacePhotoRequest(placeId, base64Image, selectedValue, capacity = capacity)
            }

            if(setBase64ImageFromPath(absolutePath) != null){
                mDisposable = AppServices().getClient().sendPhoto(request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe ( { onSuccess(it, shouldSendSurveySelectedOption)}, { onError(it) })
            }
        }else{
            mView!!.onResponseReport("Es necesario que tomes una captura primero")
        }
    }


    fun onSuccess(response: AnalizedPhotoResponse, shouldSendSurveySelectedOption: Boolean){
        //if(!shouldSendSurveySelectedOption) mView!!.onResponseSendPhoto(response.people!!)
        //else mView!!.onResponseReport(null)
        mView!!.onResponseReport(null, response.people!!)
    }

    fun onError(t: Throwable){
        Log.d("ERROR_REPORT", t.message)
        mView!!.onResponseReport("Ha ocurrido un error, por favor intentalo nuevamente")
    }

    private fun setBase64ImageFromPath(absolutePath: String): String? {
        val bitmap = BitmapFactory.decodeFile(absolutePath)

        val path: String?
        if (bitmap != null) {
            if (bitmap.width > bitmap.height) {
                val matrix = Matrix()
                matrix.postRotate(90f)

                val imageBitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width,
                        bitmap.height, matrix, true)
                path = toBase64(imageBitmapRotated)
            } else {
                path = toBase64(bitmap)
            }
        } else {
            path = null
        }

        return path
        //return mPath
    }

    // Create the File where the photo should go
    fun createImageFile(): File? {
        val root = File(mView!!.getContext().filesDir, "photos")
        if (!(root.mkdirs() || root.isDirectory)) {
            mView!!.displayMessage("Unable to save file")
            return null
        }

        val fname = "TMP_" + Calendar.getInstance().timeInMillis + ".jpeg"
        val storageDirectory = File(root, fname)
        absolutePath = storageDirectory.absolutePath
        return storageDirectory
    }


    fun toBase64(image: Bitmap): String {
        /*val output = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 65, output)
        val bytes = output.toByteArray()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(bytes)
        } else {
            return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)
        }*/
        //return android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT)

        return FileInputStream(absolutePath).use { inputStream ->
            ByteArrayOutputStream().use { outputStream ->
                Base64OutputStream(outputStream, android.util.Base64.NO_WRAP).use { base64FilterStream ->
                    inputStream.copyTo(base64FilterStream)
                    base64FilterStream.close()
                    outputStream.toString()
                }
            }
        }
    }
}