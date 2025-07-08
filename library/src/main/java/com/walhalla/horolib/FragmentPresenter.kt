package com.walhalla.horolib

import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.core.net.toUri
import androidx.core.graphics.createBitmap

class FragmentPresenter(activity: android.app.Activity, view: PresenterView) :
    android.view.View.OnClickListener {


    fun onSave(
        activity: android.app.Activity,
        watermark: android.widget.TextView,
        success: com.walhalla.horolib.databinding.ItemSuccessBinding
    ) {
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.R) { //>30
            if (openAllFilesAccessPermission30()) {
                val uri0 =
                    String.format("package:%s", activity.applicationContext.packageName).toUri()
                val uri1 = android.net.Uri.fromParts("package", activity.packageName, null)
                //DLog.d("@@@" + uri0 + "|" + uri1 + "|");
//                try {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                    intent.setData(uri0);
//                    storageActivityResultLauncher.launch(intent);
//                    Toast.makeText(getActivity(), "@@@@", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                    storageActivityResultLauncher.launch(intent);
//                }
                try {
                    val intent = android.content.Intent()
                    intent.action =
                        android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                    intent.data = uri1
                    //--    storageActivityResultLauncher.launch(intent);
                    //Toast.makeText(getActivity(), "@@@@", Toast.LENGTH_SHORT).show();
                } catch (e: java.lang.Exception) {
                    val intent = android.content.Intent()
                    intent.action =
                        android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    //--    storageActivityResultLauncher.launch(intent);
                }
            } else {
                //Toast.makeText(getContext(), "@@@@@@@@@", Toast.LENGTH_SHORT).show();
                onSave29(activity, watermark, success)
            }
        } else {
            val perm = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            val var0 = ContextCompat.checkSelfPermission(
                activity,
                perm
            ) == PackageManager.PERMISSION_GRANTED
            if (var0) {
                onSave29(activity, watermark, success)
            } else {
                //show permission popup
                requestStoragePermission(activity)
            }
        }
    }

    private fun onSave29(
        activity: android.app.Activity,
        watermark: android.widget.TextView,
        success: com.walhalla.horolib.databinding.ItemSuccessBinding
    ) {
        watermark.visibility = android.view.View.VISIBLE
        val bitmap = createBitmap(success.llBackground.getWidth(), success.llBackground.getHeight())
        val canvas = android.graphics.Canvas(bitmap)
        success.llBackground.draw(canvas)
        val fos: java.io.OutputStream?

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val resolver: ContentResolver = activity.getContentResolver()
            val contentValues: ContentValues = ContentValues()
            contentValues.put(
                MediaStore.MediaColumns.DISPLAY_NAME,
                java.lang.System.currentTimeMillis().toString() + ".jpg"
            )
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                android.os.Environment.DIRECTORY_PICTURES
            )
            val imageUri: android.net.Uri? =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                DLog.d("@" + imageUri.path)
            }

            Toast.makeText(activity, R.string.file_saved, Toast.LENGTH_SHORT).show()
            success.tvSaveQuote.setText(R.string.progress_saved)
            success.ivSaveQuote.setImageResource(R.drawable.ic_menu_check)

            try {
                fos = resolver.openOutputStream(java.util.Objects.requireNonNull<Uri>(imageUri))
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, fos!!)
                fos.flush()
                fos.close()
                try {
                    MediaScannerConnection.scanFile(
                        activity, kotlin.arrayOf<kotlin.String?>(
                            imageUri!!.path
                        ), null,
                        MediaScannerConnection.OnScanCompletedListener { path: String?, uri: Uri? ->
                            DLog.d(
                                "Image is saved in gallery and gallery is refreshed."
                            )
                        }
                    )
                } catch (e: java.lang.Exception) {
                    DLog.handleException(e)
                }
            } catch (e: java.io.IOException) {
                DLog.handleException(e)
            }
            watermark.visibility = android.view.View.INVISIBLE
        } else {
            val outputStream: java.io.FileOutputStream?
            val sdCard = android.os.Environment.getExternalStorageDirectory()

            val directory = java.io.File(sdCard.getAbsolutePath() + "/Latest_quotes")
            val tmp = directory.mkdir()

            val filename = kotlin.String.format(
                java.util.Locale.getDefault(),
                "%d.jpg",
                java.lang.System.currentTimeMillis()
            )

            val outFile = java.io.File(directory, filename)
            Toast.makeText(activity, R.string.saved, Toast.LENGTH_SHORT).show()
            success.tvSaveQuote.setText(R.string.progress_saved)
            success.ivSaveQuote.setImageResource(R.drawable.ic_menu_check)
            try {
                outputStream = java.io.FileOutputStream(outFile)
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                val intent =
                    android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.setData(android.net.Uri.fromFile(outFile))
                activity.sendBroadcast(intent)

                try {
                    MediaScannerConnection.scanFile(
                        activity, kotlin.arrayOf<kotlin.String>(outFile.toString()), null,
                        OnScanCompletedListener { path: kotlin.String?, uri: android.net.Uri? ->
                            DLog.d(
                                "Image is saved in gallery and gallery is refreshed."
                            )
                        }
                    )
                } catch (e: java.lang.Exception) {
                    DLog.handleException(e)
                }
            } catch (e: java.io.IOException) {
                DLog.handleException(e)
            }
            watermark.setVisibility(android.view.View.INVISIBLE)
        }
    }

    fun rr(activity: android.app.Activity): kotlin.String {
        val app_name = activity.getString(R.string.app_name)
        val packageName = activity.getPackageName()

        val sb = java.lang.StringBuilder()
        //
        //
        for (o in data) {
            java.util.Collections.shuffle(o)

            for (i in o.indices) {
                sb.append(o.get(i))
                if (i < o.size - 1) {
                    sb.append(", ")
                } else {
                    sb.append(".\n")
                }
            }
        }

        sb.append(app_name).append("\n")

        //sb.append(UConst.GOOGLE_PLAY_CONSTANT).append(getActivity().getPackageName()).append("\n");

        //sb.append(GOOGLE_PLAY_CONSTANT);
        sb.append(packageName).append("\n")
        sb.append(app_name).append("\n")
        return sb.toString().trim { it <= ' ' }
    }

    fun copyStatus(activity: android.app.Activity, text: kotlin.String?) {
        try {
            val clipboard = activity.getApplicationContext()
                .getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager?
            val clip: ClipData = ClipData.newPlainText("label", text)
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity, R.string.copy_to_buffer, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "NONE", Toast.LENGTH_SHORT).show()
            }
        } catch (e: java.lang.Exception) {
            DLog.handleException(e)
        }
    }

    fun requestStoragePermission(activity: android.app.Activity) {
        java.sql.DriverManager.println("@@@show permission popup " + activity.javaClass.getSimpleName())

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle(R.string.permission_title_needed)
                .setMessage(R.string.this_permission_is_needed)
                .setPositiveButton(
                    android.R.string.ok,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: kotlin.Int ->
                        ActivityCompat.requestPermissions(
                            activity,
                            kotlin.arrayOf<kotlin.String>(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            com.walhalla.horolib.Constants.STORAGE_PERMISSION_CODE
                        )
                    })
                .setNegativeButton(
                    android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, which: kotlin.Int -> dialog.dismiss() })
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                activity, kotlin.arrayOf<kotlin.String>(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ), com.walhalla.horolib.Constants.STORAGE_PERMISSION_CODE
            )
        }
    }


    interface PresenterView {
        fun setBackgroundResource(image: kotlin.Int)
    }

    private val mView: PresenterView
    private val data: kotlin.collections.MutableList<kotlin.collections.MutableList<kotlin.String?>> =
        java.util.ArrayList<kotlin.collections.MutableList<kotlin.String?>>()


    private val images: kotlin.IntArray
    private var imagesIndex = 0

    init {
        data.add(CoreUtil.readFile(activity, "en.txt"))
        this.mView = view


        val typedArray: TypedArray = activity.getResources().obtainTypedArray(R.array.images)
        this.images = kotlin.IntArray(typedArray.length())
        for (index in images.indices) {
            this.images[index] = typedArray.getResourceId(index, -1)
        }
        typedArray.recycle()
    }


    override fun onClick(v: android.view.View) {
        if (v.getId() == R.id.llBackground || v.getId() == R.id.right) {
            DLog.d("@@@ r " + imagesIndex)
            //length=1
            //index=0
            if (imagesIndex < images.size) {
                mView.setBackgroundResource(images[imagesIndex])
            }
            ++imagesIndex // update index, so that next time it points to next resource  // update index, so that next time it points to next resource
            if (imagesIndex > images.size - 1) {
                imagesIndex =
                    0 // if we have reached at last index of array, simply restart from beginning
            }
        } else if (v.getId() == R.id.left) {
            DLog.d("@@@ l " + imagesIndex)
            if (imagesIndex < images.size) {
                mView.setBackgroundResource(images[imagesIndex])
            }
            --imagesIndex // update index, so that next time it points to next resource
            if (imagesIndex < 0) {
                imagesIndex =
                    images.size - 1 // if we have reached at last index of array, simply restart from beginning
            }
        }
    }

    private fun getRandomNumberUsingNextInt(min: kotlin.Int, max: kotlin.Int): kotlin.Int {
        val random = java.util.Random()
        return random.nextInt(max - min) + min
    }

    companion object {
        fun openAllFilesAccessPermission30(): kotlin.Boolean {
            var aa = false
            var bb = false
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) { //30
                aa = !android.os.Environment.isExternalStorageManager()
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) { //29
                bb = !android.os.Environment.isExternalStorageLegacy()
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) { //30
                //DLog.d("@@@@@" + Environment.isExternalStorageManager() + "@@@" + Environment.isExternalStorageLegacy());
            }
            return false
            //return aa;
        }
    }
}