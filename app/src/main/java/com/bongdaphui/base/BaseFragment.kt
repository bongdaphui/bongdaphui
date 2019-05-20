package com.bongdaphui.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import com.bongdaphui.MainActivity
import com.bongdaphui.dao.AppDatabase
import com.bongdaphui.model.CityModel
import com.bongdaphui.model.ClubModel
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.utils.KeyboardManager
import com.google.firebase.auth.FirebaseAuth

abstract class BaseFragment : Fragment(), BaseInterface {

    private var activeActivity: BaseActivity? = null

    override fun getActiveActivity(): FragmentActivity {
        return activeActivity!!
    }

    override fun onDetach() {
        super.onDetach()
        activeActivity = null
    }

    override fun onAttach(activity: Context?) {
        super.onAttach(activity)
        if (activity is BaseActivity) {
            activeActivity = activity
        }
    }

    /*override fun getActiveActivity(): FragmentActivity {
        return BaseApplication().getActiveActivity()!!

    }*/


    override fun getEnterAnimation(): Int {
        return 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBindView()
    }

    @IdRes
    fun getMainContainerId(): Int {
        return if (activity != null && activity is BaseActivity)
            (activity as BaseActivity)
                .getFragmentContainerResId()
        /* else if (getActiveActivity() is BaseActivity)
             (getActiveActivity() as BaseActivity)
                 .getFragmentContainerResId()*/
        else
            activeActivity?.getFragmentContainerResId() ?: 0
    }

    protected fun addFragment(fragment: BaseFragment) {
        if (activity != null && activity is BaseActivity)
            (activity as BaseActivity).addFragment(getMainContainerId(), fragment)
        /*else if (getActiveActivity() is BaseActivity)
            (getActiveActivity() as BaseActivity).addFragment(getMainContainerId(), fragment)*/
        else
            activeActivity?.addFragment(getMainContainerId(), fragment)
    }

    protected fun replaceFragment(fragment: BaseFragment, clearStack: Boolean) {
        if (activity != null && activity is MainActivity)
            (activity as BaseActivity).replaceFragment(getMainContainerId(), fragment, clearStack)
        else
            activeActivity?.replaceFragment(getMainContainerId(), fragment, clearStack)
    }

    fun onBackPressed() {

        if (activity != null && activity is BaseActivity) {
            if (KeyboardManager.isShowSoftKeyboard(activity as BaseActivity)) {
                KeyboardManager.hideSoftKeyboard(activity as BaseActivity)
            } else {
                (activity as BaseActivity).onBackPressed()
            }
        } else {
            if (KeyboardManager.isShowSoftKeyboard(activeActivity!!)) {
                KeyboardManager.hideSoftKeyboard(activeActivity!!)
            } else {
                activeActivity?.onBackPressed()
            }

        }
    }

    fun showHeader(isShow: Boolean) {
        if (activity is MainActivity) {
            (activity as MainActivity).showHeader(isShow)
        }
    }

    fun showFooter(isShow: Boolean) {
        if (activity is MainActivity) {
            (activity as MainActivity).showFooter(isShow)
        }
    }

    fun showTitle(isShow: Boolean) {
        if (activity is MainActivity) {
            (activity as MainActivity).showTitle(isShow)
        }
    }

    fun setTitle(title: String) {
        if (activity is MainActivity) {
            (activity as MainActivity).setTitleHeader(title)
        }
    }

    fun showButtonBack(isShow: Boolean) {

        if (activity is MainActivity) {

            (activity as MainActivity).showButtonBack(isShow)
        }
    }

    fun showProgress(isShow: Boolean) {

        if (null != activity && activity is MainActivity) {

            (activity as MainActivity).showProgress(isShow)
        }
    }

    fun openClubs() {

        if (activity != null && activity is MainActivity)

            (activity as MainActivity).openClubs()

    }

    /*fun saveUIDUser(KEY_NAME: String, text: String) {

        (activity as MainActivity).saveUIDUser(KEY_NAME, text)
    }

    fun getUIDUser(KEY_NAME: String): String? {

        return (activity as MainActivity).getUIDUser(KEY_NAME)
    }

    fun clearUIDUser() {

        return (activity as MainActivity).clearUIDUser()
    }*/

    fun getFireBaseAuth(): FirebaseAuth? {

        return if (activity != null && activity is MainActivity)

            (activity as MainActivity).getFireBaseAuth()
        else {

            return FirebaseAuth.getInstance()
        }
    }

    fun getUIDUser(): String {
        val user = getFireBaseAuth()?.currentUser
        if (user != null) {
            return user.uid
        }
        return ""
    }

    fun setListCity(list: ArrayList<CityModel>) {

        if (null != activity && activity is MainActivity)

            (activity as MainActivity).setListCityModel(list)

    }

    fun getListCity(): ArrayList<CityModel> {

        return (activity as MainActivity).getListCityModel()
    }

    fun setListField(list: ArrayList<FbFieldModel>) {

        if (null != activity && activity is MainActivity)

            (activity as MainActivity).setListFieldModel(list)

    }

    fun getListField(): ArrayList<FbFieldModel> {

        return (activity as MainActivity).getListFieldModel()
    }

    fun setListClub(list: ArrayList<ClubModel>) {

        if (null != activity && activity is MainActivity)

            (activity as MainActivity).setListClubModel(list)

    }

    fun getListClub(): ArrayList<ClubModel> {

        return (activity as MainActivity).getListClubModel()
    }


    fun hideKeyBoard() {
        KeyboardManager.hideSoftKeyboard(activeActivity!!)
    }

    fun getDatabase(): AppDatabase {

        return (activity as MainActivity).getDatabase()
    }
}