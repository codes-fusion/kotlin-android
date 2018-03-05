package com.example.rssgrabber.utils

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.MenuItemCompat
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.example.rssgrabber.R

class AndroidUtils {
    fun attachMainController(
            activity: Activity, container: ViewGroup?,
            savedInstanceState: Bundle?, controller: Controller): Router {

        val router = Conductor.attachRouter(activity, container!!, savedInstanceState)
        val hasRoot = router.hasRootController()
        if (!hasRoot) {
            router.setRoot(RouterTransaction.with(controller))
        }
        return router
    }

    fun showEditKeyboard(context: Context, et: EditText?) {
        et?.isFocusableInTouchMode = true
        et?.isFocusable = true
        et?.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    fun closeEditKeyboard(context: Context, et: EditText?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et?.windowToken, 0)
    }

    fun showOptionMenu(menu: Menu?, id: Int) {
        val actionViewItem = menu?.findItem(R.id.action_settings)
        val view = MenuItemCompat.getActionView(actionViewItem)
        val customView = view.findViewById<View?>(id)
        customView?.visibility = View.VISIBLE
    }

    fun hideOptionMenu(menu: Menu?, id: Int) {
        val actionViewItem = menu?.findItem(R.id.action_settings)
        val view = MenuItemCompat.getActionView(actionViewItem)
        val customView = view.findViewById<View?>(id)
        customView?.visibility = View.GONE
    }

    fun optionMenu(menu: Menu?, id: Int, listener: (View) -> Unit) {
        val actionViewItem = menu?.findItem(R.id.action_settings)
        val view = MenuItemCompat.getActionView(actionViewItem)
        val textView = view.findViewById<TextView?>(id)
        textView?.visibility = View.VISIBLE
        textView?.setOnClickListener(listener)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            // Network is present and connected
            isAvailable = true
        }
        return isAvailable
    }

    fun <T : ViewModel> getViewModelProvider(activity: FragmentActivity, modelClass: Class<T>) =
            ViewModelProviders.of(activity).get(modelClass)
}