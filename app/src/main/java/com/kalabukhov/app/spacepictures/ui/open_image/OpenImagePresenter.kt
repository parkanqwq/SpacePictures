package com.kalabukhov.app.spacepictures.ui.open_image

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.terrakok.cicerone.Router
import com.kalabukhov.app.spacepictures.domain.ImageSpaceRepo
import com.kalabukhov.app.spacepictures.domain.entity.ImageSpaceDbEntity
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDate
import java.util.*

class OpenImagePresenter(
    private val router: Router,
    private val context: Context
) : OpenImageContract.Presenter() {

    override fun onDelete(imageRepo: ImageSpaceRepo, imageSpaceDbEntity: ImageSpaceDbEntity) {
        imageRepo.delete(imageSpaceDbEntity)
        viewState.setState(OpenImageContract.ViewState.DELETE)
        router.exit()
    }

    override fun onSaveImageToPhone(
        bitmap: Bitmap, contentResolver: ContentResolver, intent: Intent
    ) {
        fun requestPermission(): Boolean {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            val isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            if (!isGranted) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(permission),
                    OpenImage.PERMISSION_CODE
                )
            }
            return isGranted
        }

        val fos: OutputStream
        try {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues()
                contentValues.put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    intent.getStringExtra(OpenImage.TITTLE).toString()
                            + " (" + LocalDate.now().toString()
                            + ").jpg"
                )
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH, Environment
                        .DIRECTORY_PICTURES + File.separator + "ImageSpaceFolder"
                )

                val imageUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
                )
                fos = contentResolver.openOutputStream(Objects.requireNonNull(imageUri!!))!!
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                Objects.requireNonNull(fos)
                viewState.setState(OpenImageContract.ViewState.SAVE)
            } else {
                if (requestPermission()) {
                    val imagesDir = Environment
                        .getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                        ).toString()
                    val image = File(imagesDir, intent
                        .getStringExtra(OpenImage.TITTLE).toString()
                            + " (" + LocalDate.now().toString()
                            + ").jpg")
                    fos = FileOutputStream(image)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    Objects.requireNonNull<OutputStream>(fos)
                    Objects.requireNonNull(fos).close()
                    viewState.setState(OpenImageContract.ViewState.SAVE)
                }
            }
        } catch (e: Exception) {
            viewState.setState(OpenImageContract.ViewState.NOT_SAVE)
        }
    }
}