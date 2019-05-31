package com.bongdaphui

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import com.bongdaphui.approvePlayer.ApproveJoinClubScreen
import com.bongdaphui.base.BaseActivity
import com.bongdaphui.base.BaseApplication
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.club.ClubScreen
import com.bongdaphui.dao.AppDatabase
import com.bongdaphui.field.FieldScreen
import com.bongdaphui.findClub.FindClubScreen
import com.bongdaphui.findPlayer.FindPlayerScreen
import com.bongdaphui.manager.ManagerScreen
import com.bongdaphui.splash.SplashScreen
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.IntentExtraName
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), View.OnClickListener {

    override fun getActiveActivity(): FragmentActivity {
        return BaseApplication().getActiveActivity()!!
    }

    private lateinit var mAuth: FirebaseAuth

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
//
//        //setup fire base
//        val builder = FirebaseOptions.Builder()
//            .setApplicationId("1:303996690941:android:37a411d693a33eff")
//            .setApiKey("AIzaSyB5GkogHL0UNqnsY4WVBFb5gq26cO6vJqY")
//            .setDatabaseUrl("https://bongdaphui-27e53.firebaseio.com")
//            .setStorageBucket("bongdaphui-27e53.appspot.com")
//            .setProjectId("bongdaphui-27e53")
//
//        FirebaseApp.initializeApp(this, builder.build())

        mAuth = FirebaseAuth.getInstance()

//        prefs = SharedPreference(this

        database = Room.databaseBuilder(this, AppDatabase::class.java, Constant().roomData)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

        //check intent
        val isOpenFromRequestJoinTeam = intent.extras?.getBoolean(IntentExtraName.REQUEST_JOIN_TEAM, false) ?: false
        if (isOpenFromRequestJoinTeam) {
            addFragment(getFragmentContainerResId(), ApproveJoinClubScreen())
        } else {
            replaceFragment(SplashScreen(), true)
        }
    }

    fun getFireBaseAuth(): FirebaseAuth? {
        return mAuth
    }


    private fun replaceFragment(baseFragment: BaseFragment, clearStack: Boolean) {

        replaceFragment(getFragmentContainerResId(), baseFragment, clearStack)
    }

    override fun onBindView() {

        onClickListener()

    }

    private fun onClickListener() {

        activity_main_iv_back.setOnClickListener(this)

        activity_main_v_menu_club.setOnClickListener(this)

        activity_main_v_menu_find_player.setOnClickListener(this)

        activity_main_v_menu_find_club.setOnClickListener(this)

        activity_main_v_menu_football_field.setOnClickListener(this)

        activity_main_v_menu_manager.setOnClickListener(this)

    }

    fun showHeader(isShow: Boolean) {

        if (isShow) {
            activity_main_v_header.visibility = View.VISIBLE
        } else {
            activity_main_v_header.visibility = View.GONE

        }
    }

    fun showFooter(isShow: Boolean) {

        if (isShow) {
            activity_main_v_footer.visibility = View.VISIBLE
        } else {
            activity_main_v_footer.visibility = View.GONE

        }
    }

    fun showTitle(isShow: Boolean) {

        if (isShow) {
            activity_main_tv_title.visibility = View.VISIBLE
        } else {
            activity_main_tv_title.visibility = View.GONE

        }
    }

    fun setTitleHeader(title: String) {
        activity_main_tv_title.text = title
    }

    fun showButtonBack(isShow: Boolean) {

        if (isShow) {
            activity_main_iv_back.visibility = View.VISIBLE
        } else {
            activity_main_iv_back.visibility = View.GONE

        }
    }

    fun showProgress(isShow: Boolean) {
        if (isShow) {
            activity_main_progress.visibility = View.VISIBLE
        } else {
            activity_main_progress.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.activity_main_iv_back -> onBackPressed()
            R.id.activity_main_v_menu_football_field -> openFindField()
            R.id.activity_main_v_menu_find_player -> openFindPlayer()
            R.id.activity_main_v_menu_find_club -> openFindClub()
            R.id.activity_main_v_menu_club -> openClubs()
            R.id.activity_main_v_menu_manager -> openManager()

        }
    }

    fun openFindField() {

        replaceFragment(FieldScreen(), true)

        activity_main_iv_menu_club.isSelected = false
        activity_main_tv_menu_club.isSelected = false
        activity_main_iv_menu_find_player.isSelected = false
        activity_main_tv_menu_find_player.isSelected = false
        activity_main_iv_menu_find_club.isSelected = false
        activity_main_tv_menu_find_club.isSelected = false
        activity_main_iv_menu_football_field.isSelected = true
        activity_main_tv_menu_football_field.isSelected = true
        activity_main_iv_menu_settings.isSelected = false
        activity_main_tv_menu_settings.isSelected = false
    }

    private fun openFindPlayer() {

        replaceFragment(FindPlayerScreen(), true)

        activity_main_iv_menu_club.isSelected = false
        activity_main_tv_menu_club.isSelected = false
        activity_main_iv_menu_find_player.isSelected = true
        activity_main_tv_menu_find_player.isSelected = true
        activity_main_iv_menu_find_club.isSelected = false
        activity_main_tv_menu_find_club.isSelected = false
        activity_main_iv_menu_football_field.isSelected = false
        activity_main_tv_menu_football_field.isSelected = false
        activity_main_iv_menu_settings.isSelected = false
        activity_main_tv_menu_settings.isSelected = false
    }

    private fun openFindClub() {

        replaceFragment(FindClubScreen(), true)

        activity_main_iv_menu_club.isSelected = false
        activity_main_tv_menu_club.isSelected = false
        activity_main_iv_menu_find_player.isSelected = false
        activity_main_tv_menu_find_player.isSelected = false
        activity_main_iv_menu_find_club.isSelected = true
        activity_main_tv_menu_find_club.isSelected = true
        activity_main_iv_menu_football_field.isSelected = false
        activity_main_tv_menu_football_field.isSelected = false
        activity_main_iv_menu_settings.isSelected = false
        activity_main_tv_menu_settings.isSelected = false
    }

    private fun openClubs() {

        replaceFragment(ClubScreen(), true)

        activity_main_iv_menu_club.isSelected = true
        activity_main_tv_menu_club.isSelected = true
        activity_main_iv_menu_find_player.isSelected = false
        activity_main_tv_menu_find_player.isSelected = false
        activity_main_iv_menu_find_club.isSelected = false
        activity_main_tv_menu_find_club.isSelected = false
        activity_main_iv_menu_football_field.isSelected = false
        activity_main_tv_menu_football_field.isSelected = false
        activity_main_iv_menu_settings.isSelected = false
        activity_main_tv_menu_settings.isSelected = false
    }

    private fun openManager() {

        replaceFragment(ManagerScreen(), true)

        activity_main_iv_menu_club.isSelected = false
        activity_main_tv_menu_club.isSelected = false
        activity_main_iv_menu_find_player.isSelected = false
        activity_main_tv_menu_find_player.isSelected = false
        activity_main_iv_menu_find_club.isSelected = false
        activity_main_tv_menu_find_club.isSelected = false
        activity_main_iv_menu_football_field.isSelected = false
        activity_main_tv_menu_football_field.isSelected = false
        activity_main_iv_menu_settings.isSelected = true
        activity_main_tv_menu_settings.isSelected = true
    }

    override fun onDestroy() {
        super.onDestroy()
//        FirebaseApp.getInstance().delete()

        val pid = android.os.Process.myPid()
        android.os.Process.killProcess(pid)

    }

    fun getDatabase(): AppDatabase {

        return database
    }
}
