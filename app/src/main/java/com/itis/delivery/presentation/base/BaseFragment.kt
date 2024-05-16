package com.itis.delivery.presentation.base

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.itis.delivery.R
import com.itis.delivery.utils.AuthErrors
import com.itis.delivery.utils.observe
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout) {

    protected fun showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(
            requireView(),
            message,
            duration
        ).show()
    }

    inline fun <T> Flow<T>.observe(crossinline block: (T) -> Unit): Job {
        return observe(fragmentLifecycleOwner = this@BaseFragment.viewLifecycleOwner, block)
    }

    protected fun showAuthError(error: AuthErrors) {
        when (error) {
            AuthErrors.WAIT -> showSnackBar(getString(R.string.wait_error))
            AuthErrors.INVALID_DATA -> showSnackBar(getString(R.string.invalid_data))
            AuthErrors.UNEXPECTED -> showSnackBar(getString(R.string.unexpected_error))
            AuthErrors.INVALID_CREDENTIALS -> showSnackBar(getString(R.string.invalid_credentials))
            AuthErrors.USER_ALREADY_EXISTS -> showSnackBar(getString(R.string.user_already_exists))
        }
    }

    protected fun changeErrorVisibility(
        viewBinding: ViewBinding,
        isVisible: Boolean,
        btnOnClickListener: View.OnClickListener
    ) {
        with(viewBinding) {
            val errorLayout = activity?.findViewById<View>(R.id.layout_error)
            errorLayout?.visibility = if (isVisible) View.VISIBLE else View.GONE

            val tryAgainBtn = activity?.findViewById<View>(R.id.btn_try_again)
            tryAgainBtn?.setOnClickListener(btnOnClickListener)
            root.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
    }
}