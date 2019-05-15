package com.bongdaphui.player

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bongdaphui.R
import com.bongdaphui.addPlayer.JoinClubScreen
import com.bongdaphui.base.BaseFragment
import com.bongdaphui.model.ClubModel
import com.bongdaphui.model.PlayerModel
import com.bongdaphui.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_player.*


class PlayerScreen : BaseFragment() {

    private var clubModel: ClubModel? = null

    companion object {

        private const val CLUB_MODEL = "CLUB_MODEL"

        fun getInstance(clubModel: ClubModel): PlayerScreen {

            val screen = PlayerScreen()

            val bundle = Bundle()

            bundle.putSerializable(CLUB_MODEL, clubModel)

            screen.arguments = bundle

            return screen
        }
    }


    private val listPlayer: ArrayList<PlayerModel> = ArrayList()

    var adapterPlayer: PlayerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_player, container, false)

    }

    override fun onResume() {
        super.onResume()

        showHeader(true)

        showButtonBack(true)

        showFooter(true)

    }

    @SuppressLint("RestrictedApi")
    override fun onBindView() {

        showProgress(true)

        val bundle = arguments

        if (bundle != null) {

            clubModel = bundle.getSerializable(CLUB_MODEL) as ClubModel

            setTitle(String.format("Cầu thủ %s ", clubModel!!.name))
        }

        Log.d(Constant().TAG, clubModel!!.idCaptain + " : " + getUIDUser())

        if (clubModel!!.idCaptain == getUIDUser()) {

            fragment_member_club_fab.visibility = View.VISIBLE

        } else {

            fragment_member_club_fab.visibility = View.GONE

        }

        onClick()

        adapterPlayer = PlayerAdapter(context, listPlayer)

//        fragment_member_club_rcv.layoutManager = LinearLayoutManager(context)

        fragment_member_club_rcv.adapter = adapterPlayer

        val ref = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_CLUB)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

                showProgress(false)

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    listPlayer.clear()

                    for (i in p0.children) {

                        val memberModel = i.getValue<PlayerModel>(PlayerModel::class.java)

                        if (memberModel!!.idUser == clubModel!!.idCaptain && memberModel.idClub == clubModel!!.id
                        ) {

                            listPlayer.add(memberModel)
                        }
                    }
                    adapterPlayer!!.notifyDataSetChanged()

                    showProgress(false)

                }
            }
        })
    }

    private fun onClick() {

        fragment_member_club_fab.setOnClickListener {

            addFragment(JoinClubScreen.getInstance(clubModel!!))

        }
    }
}