package com.example.byheart.shared

/**
 * Each fragment that has a back button should implement this method.
 * Based on whether or not the fragment has this method the BaseActivity decides which fragment's
 * OnBackPressed's method should be called.
 * @author Bryan de Ridder
 */
interface IOnBackPressed {
    fun onBackPressed(): Boolean
}