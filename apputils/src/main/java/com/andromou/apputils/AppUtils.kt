package com.andromou.apputils

import android.app.Activity
import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    fun shareApp(context: Context) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
            putExtra(Intent.EXTRA_TEXT, "Check out this cool app!")
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    fun rateApp(context: Context) {
        val uri = Uri.parse("market://details?id=${context.packageName}")
        val rateIntent = Intent(Intent.ACTION_VIEW, uri)
        if (context.packageManager.queryIntentActivities(rateIntent, 0).isEmpty()) {
            rateIntent.data = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
        }
        context.startActivity(rateIntent)
    }

    fun checkMoreApps(context: Context, developerName: String ) {
        val uri = Uri.parse("market://search?q=pub:$developerName")
        val moreAppsIntent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(moreAppsIntent)
    }

    fun dateToLong(date: Date): Long = date.time

    fun longToDate(milliseconds: Long): Date = Date(milliseconds)

    fun dateStringToLong(dateString: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            dateFormat.parse(dateString)?.time ?: -1
        } catch (e: ParseException) {
            e.printStackTrace()
            -1
        }
    }

    fun longToDateString(milliseconds: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(milliseconds)
        return dateFormat.format(date)
    }

    fun dpToPx(context: Context, dp: Float): Float {
        val density = context.resources.displayMetrics.density
        return dp * density
    }

    fun pxToDp(context: Context, px: Float): Float {
        val density = context.resources.displayMetrics.density
        return px / density
    }

    fun writeToSharedPreferences(context: Context, key: String, value: String) {
        val sharedPref = context.getSharedPreferences("your_prefs_file", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun readFromSharedPreferences(context: Context, key: String, defaultValue: String): String {
        val sharedPref = context.getSharedPreferences("your_prefs_file", Context.MODE_PRIVATE)
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.currentFocus
        view?.let { imm.hideSoftInputFromWindow(it.windowToken, 0) }
    }

    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }

    fun formatTime(date: Date): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        if (!isConnected) {
            showToast("No internet connection!" , context)
        }
        return isConnected
    }

    fun openUrlInBrowser(context: Context, url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    fun sendEmail(context: Context, to: Array<String>, subject: String, body: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "", null)).apply {
            putExtra(Intent.EXTRA_EMAIL, to)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    fun callPhoneNumber(context: Context, phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
        context.startActivity(callIntent)
    }

    fun isLandscape(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    fun showToast(message: String, context: Context) {
        val toast = Toast(context)
        val view = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)
        val textView: TextView = view.findViewById(R.id.custom_toast_text)
        textView.text = message
        toast.view = view
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("text", text)
        clipboard.setPrimaryClip(clip)
    }

    fun pasteTextFromClipboard(context: Context, editText: EditText) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip?.getItemAt(0)?.text?.let {
            editText.setText(it.toString())
        }
    }

    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = Configuration(resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun setDefaultLocale(context: Context) {
        val locale = Locale.getDefault()
        val resources = context.resources
        val config = Configuration(resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
