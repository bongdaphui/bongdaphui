package com.bongdaphui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.utils.Utils
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseActivity : AppCompatActivity(), BaseInterface {

    override fun getActiveActivity(): FragmentActivity {
        return BaseApplication().getActiveActivity()!!
    }

    @SuppressLint("UseSparseArrays")
    internal var containers = HashMap<Int, ArrayList<String>>()


    override fun getEnterAnimation(): Int {
        return 0
    }

    /*override fun getActiveActivity(): FragmentActivity {
        return BaseApplication().getActiveActivity()!!

    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication().setActiveActivity(this)

    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        BaseApplication().setActiveActivity(this)

    }

    override fun onResume() {
        super.onResume()
        BaseApplication().setActiveActivity(this)

    }


    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        onBindView()
    }

    override fun setContentView(view: View) {
        super.setContentView(view)

        onBindView()
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams) {
        super.setContentView(view, params)

        onBindView()
    }

    fun getFragmentContainerResId(): Int {
        return R.id.activity_main_fl_main
    }


    fun addFragment(containerId: Int, fragment: BaseFragment) {

        val manager = supportFragmentManager

        val ft = manager.beginTransaction()

        val tag = fragment.javaClass.canonicalName

        var tags = containers[containerId]

        if (tags == null) {

            tags = ArrayList()

//            containers[containerId] = tags

            containers.put(containerId, tags)

            tags.add(tag)

            ft.setCustomAnimations(fragment.getEnterAnimation(), 0, 0, 0)
            ft.add(containerId, fragment, tag)
            ft.commit()

        } else {

            val top = getTopFragment(containerId)

            top?.onPause()

            tags.add(tag)

            val transaction = supportFragmentManager
                .beginTransaction()

            transaction
                .setCustomAnimations(
                    fragment.getEnterAnimation(), 0, 0, 0
                )
                .add(containerId, fragment, tag).commit()
        }
    }

    fun replaceFragment(containerId: Int, fragment: BaseFragment, clearStack: Boolean) {

        if (supportFragmentManager != null) {

            val tag = fragment.javaClass.canonicalName

            val tags = containers[containerId]

            if (tags != null) {

                if (clearStack) {

                    popAllBackStack(containerId)

                    addFragment(containerId, fragment)

                } else {

                    var isExist = false

                    for (i in tags.indices) {

                        val entry = supportFragmentManager.findFragmentByTag(tags[i]) as BaseFragment?

                        if (null != entry && null != entry.javaClass.canonicalName && entry.run {
                                javaClass.canonicalName == tag
                            }
                        ) {

                            isExist = true

                            break
                        }
                    }
                    if (isExist) {

                        if (tags.size > 1) {

                            var top = getTopFragment(containerId)

                            while (!(null != top && null != top.javaClass.canonicalName && top.javaClass.canonicalName == tag)
                            ) {

                                backStack(containerId)

                                top = getTopFragment(containerId)
                            }
                        }
                    } else {

                        if (tags.size > 1) {

                            val top = getTopFragment(containerId)

                            addFragment(containerId, fragment)

                            if (top != null && !Utils().isEmpty(top.javaClass.canonicalName))

                                removeFragment(containerId, top.javaClass.canonicalName)
                        } else {

                            popAllBackStack(containerId)

                            addFragment(containerId, fragment)
                        }
                    }
                }
            } else {

                addFragment(containerId, fragment)
            }
        }
    }

    fun getTopFragment(): BaseFragment {
        return this.getTopFragment(getFragmentContainerResId())!!
    }

    private fun getTopFragment(@IdRes containerId: Int): BaseFragment? {

        try {

            val tags = containers[containerId]

            if (tags != null && tags.size > 0)

                return supportFragmentManager.findFragmentByTag(tags[tags.size - 1]) as BaseFragment?

        } catch (e: Exception) {

            e.printStackTrace()
        }

        return null
    }

    private fun removeFragment(@IdRes containerId: Int, tag: String) {

        val tags = containers[containerId]

        if (tags != null) {

            var removed = getTopFragment(containerId)

            if (removed != null && null != removed.javaClass.canonicalName && removed.javaClass.canonicalName.equals(
                    tag
                )
            ) {

                backStack(containerId)

            } else {

                for (i in tags.indices) {

                    removed = supportFragmentManager.findFragmentByTag(tags[i]) as BaseFragment?

                    if (null != removed && null != removed.javaClass.canonicalName && removed.javaClass.getCanonicalName().equals(
                            tag
                        )
                    ) {

                        val transaction = supportFragmentManager.beginTransaction().remove(removed)

                        transaction.commit()

                        tags.removeAt(i)

                        break
                    }
                }
            }
        }
    }

    private fun popAllBackStack(@IdRes containerId: Int) {
        if (supportFragmentManager != null) {
            try {
                val tags = containers[containerId]
                if (tags != null) {

                    val transaction = supportFragmentManager
                        .beginTransaction()

                    val fragments = ArrayList<BaseFragment>()

                    for (tag in tags)
                        fragments.add(supportFragmentManager.findFragmentByTag(tag) as BaseFragment)

                    for (fragment in fragments) {
                        transaction.remove(fragment)
                    }

                    transaction.commit()

//                    val removingTagsArray = arrayOfNulls<String>(tags.size)

                    tags.toTypedArray()

                    clearStack(containerId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun clearStack(@IdRes containerId: Int) {

        val tags = containers[containerId]

        tags?.clear()
    }

    private fun backStack(@IdRes containerId: Int) {

        if (supportFragmentManager != null) {

            val tags = containers[containerId]

            if (tags != null) {

                if (tags.size <= 1) {

                    finish()
                } else {

                    val transaction = supportFragmentManager.beginTransaction()

                    val removeFragment = getTopFragment(containerId)

                    if (removeFragment != null) {

                        removeFragment.onPause()

                        tags.removeAt(tags.size - 1)

                        transaction.remove(removeFragment)
                        transaction.commit()
                    }

                    val fragment = getTopFragment(containerId)

                    fragment?.onResume()
                }
            }
        }
    }

    private fun getBackStackCount(): Int {
        val tags = containers[getFragmentContainerResId()]
        return tags?.size ?: 0
    }

    private fun backStack() {
        backStack(getFragmentContainerResId())
    }

    override fun onBackPressed() {
        handleBackPressed()
    }

    private fun handleBackPressed() {
//        val fragment = getTopFragment()

        if (getBackStackCount() > 0) {
            backStack()
        } else
            super.onBackPressed()
    }

}