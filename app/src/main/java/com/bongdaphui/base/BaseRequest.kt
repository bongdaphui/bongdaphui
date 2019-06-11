package com.bongdaphui.base

import android.util.Log
import com.bongdaphui.approvePlayer.ApprovePlayerResponse
import com.bongdaphui.listener.DeleteDataDataListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.UpdateListener
import com.bongdaphui.model.*
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.DateTimeUtil
import com.bongdaphui.utils.FireBasePath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson


class BaseRequest {

    private val notYetData = "Chưa có dữ liệu"

    fun removeImage(urlImage: String) {

        val photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(urlImage)

        photoRef.delete().addOnSuccessListener {
            // File deleted successfully
            Log.d(Constant().tag, "onSuccess: deleted file")
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
            Log.d(Constant().tag, "onFailure: did not delete file")
        }
    }

    fun getDataField(path: String, listener: GetDataListener<FbFieldModel>) {

        val db = FirebaseFirestore.getInstance().collection(path)

        db.orderBy("name").get()
            .addOnSuccessListener { document ->

                val value = document?.toObjects(FbFieldModel::class.java)

                if (value != null && value.size > 0) {

                    listener.onSuccess(ArrayList(value))

                } else {

                    listener.onFail(notYetData)
                }
            }
            .addOnFailureListener { exception ->
                listener.onFail(exception.localizedMessage)
            }
    }

    fun getUserInfo(uid: String, listener: GetDataListener<UserModel>) {

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionUser).document(uid).get()

            .addOnSuccessListener { document ->

                val value = document?.toObject(UserModel::class.java)

                if (value != null) {

                    listener.onSuccess(value)

                } else {

                    listener.onFail(notYetData)
                }
            }
            .addOnFailureListener { exception ->
                listener.onFail(exception.localizedMessage)
            }
    }

    fun saveOrUpdateUser(
        userModel: UserModel,
        listener: UpdateListener,
        oldPlayer: UserStickModel? = null
    ) {
        val db = FirebaseFirestore.getInstance().collection(FireBasePath().collectionUser).document(userModel.id)
        db.set(userModel, SetOptions.merge())
            .addOnSuccessListener {
                if (oldPlayer != null) {
                    //update all collection has player
                    //club
                    val newPlayer =
                        UserStickModel(userModel.id, userModel.photoUrl, userModel.name, userModel.position)
                    val dbClub = FirebaseFirestore.getInstance().collection(FireBasePath().collectionClub)
                    dbClub.whereArrayContains("players", Gson().toJson(oldPlayer))
                        .get().addOnSuccessListener { document ->
                            for (item in document) {
                                val clubModel = item.toObject(ClubModel::class.java)
                                var oldPlayer = UserStickModel()
                                for (player in clubModel.players) {
                                    val playerObj = Gson().fromJson(player, UserStickModel::class.java)
                                    if (playerObj.id == userModel.id) {
                                        oldPlayer = playerObj
                                    }
                                }
                                //update if user is captain
                                if (clubModel.idCaptain == userModel.id) {
                                    clubModel.caption = userModel.name
                                    dbClub.document(item.id).set(clubModel, SetOptions.merge())
                                }
                                dbClub.document(item.id)
                                    .update("players", FieldValue.arrayRemove(Gson().toJson(oldPlayer)))
                                dbClub.document(item.id)
                                    .update("players", FieldValue.arrayUnion(Gson().toJson(newPlayer)))
                            }
                        }
                    //update schedule db
                    val dbSchedule = FirebaseFirestore.getInstance().collection(FireBasePath().collectionSchedulePlayer)
                    dbSchedule.whereEqualTo("idPlayer", userModel.id)
                        .get().addOnSuccessListener { documents ->
                            for (item in documents) {
                                var scheduleClubModel = item.toObject(SchedulePlayerModel::class.java)
                                scheduleClubModel.namePlayer = userModel.name
                                scheduleClubModel.phonePlayer = userModel.phone
                                scheduleClubModel.photoUrlPlayer = userModel.photoUrl
                                dbSchedule.document(item.id).set(scheduleClubModel, SetOptions.merge())
                            }
                        }

                    //update request db
                    val dbRequest = FirebaseFirestore.getInstance().collection(FireBasePath().collectionRequestJoinClub)
                    dbRequest.whereEqualTo("idPlayer", userModel.id)
                        .get().addOnSuccessListener { documents ->
                            for (item in documents) {
                                var approvePlayerResponse = item.toObject(ApprovePlayerResponse::class.java)
                                approvePlayerResponse.namePlayer = userModel.name
                                approvePlayerResponse.photoPlayer = userModel.photoUrl
                                dbRequest.document(item.id).set(approvePlayerResponse, SetOptions.merge())
                            }
                        }
                }
                listener.onUpdateSuccess()
            }
            .addOnFailureListener {
                listener.onUpdateFail()
            }
    }

    fun getSchedulePlayer(listener: GetDataListener<SchedulePlayerModel>) {

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionSchedulePlayer).get()

            .addOnSuccessListener { document ->
                val value = document?.toObjects(SchedulePlayerModel::class.java)

                if (value != null && value.size > 0) {

                    listener.onSuccess(ArrayList(value))

                } else {

                    listener.onFail(notYetData)
                }
            }
            .addOnFailureListener { exception ->
                listener.onFail(exception.localizedMessage)
            }
    }

    fun getScheduleClub(listener: GetDataListener<ScheduleClubModel>) {

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionScheduleClub).get()

            .addOnSuccessListener { document ->
                val value = document?.toObjects(ScheduleClubModel::class.java)

                if (value != null && value.size > 0) {

                    listener.onSuccess(ArrayList(value))

                } else {

                    listener.onFail(notYetData)
                }
                Log.d(Constant().tag, document.metadata.toString())

            }
            .addOnFailureListener { exception ->
                listener.onFail(exception.localizedMessage)
            }
    }

    private fun deleteDocument(
        collection: String, document: String, listener: DeleteDataDataListener
    ) {

        val db = FirebaseFirestore.getInstance().collection(collection).document(document)
        db.delete()
            .addOnSuccessListener {
                listener.onSuccess()
            }
            .addOnFailureListener {
                listener.onFail("${it.message}")
            }
    }

    fun saveOrUpdateClub(clubModel: ClubModel, listener: UpdateListener) {

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionClub).document(clubModel.id)

            .set(clubModel, SetOptions.merge())

            .addOnSuccessListener {
                listener.onUpdateSuccess()
            }
            .addOnFailureListener {
                listener.onUpdateFail()
            }
    }

    fun getClubInfo(id: String, listener: GetDataListener<ClubModel>) {

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionClub).document(id).get()

            .addOnSuccessListener { document ->

                val value = document?.toObject(ClubModel::class.java)

                if (value != null) {

                    listener.onSuccess(value)

                } else {

                    listener.onFail(notYetData)
                }
            }.addOnFailureListener {
                listener.onFail(notYetData)
            }
    }

    fun getClubs(listener: GetDataListener<ClubModel>) {
        try {
            FirebaseFirestore.getInstance().collection(FireBasePath().collectionClub).get()

                .addOnSuccessListener { document ->

                    val value = document?.toObjects(ClubModel::class.java)

                    if (value != null) {

                        listener.onSuccess(ArrayList(value))

                    } else {

                        listener.onFail("Not Found")
                    }
                    Log.d("Tien", document.metadata.toString())
                }
                .addOnFailureListener { exception ->
                    listener.onFail(exception.localizedMessage)
                }

        } catch (e: Exception) {
            Log.d(Constant().tag, "crash ${e.message}")

        }
    }

    fun registerJoinClub(clubModel: ClubModel, userModel: UserModel, message: String, listener: UpdateListener) {

        val db = FirebaseFirestore.getInstance().collection(FireBasePath().collectionRequestJoinClub)
        //check exist
        db.document(clubModel.id + userModel.id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                val isExist = document?.exists() ?: false
                if (isExist) {
                    listener.onUpdateFail("Yêu cầu của bạn đang chờ duyệt, vui lòng đợi.")
                } else {
                    //subscribe club
                    FirebaseMessaging.getInstance().subscribeToTopic(clubModel.id)
                    //save request
                    val approvePlayerResponse = ApprovePlayerResponse(
                        clubModel.id,
                        clubModel.name,
                        userModel.id,
                        userModel.photoUrl,
                        userModel.name,
                        clubModel.idCaptain,
                        message
                    )


                    db.document(clubModel.id + userModel.id).set(approvePlayerResponse, SetOptions.merge())
                        .addOnSuccessListener {
                            //send message to captain
                            listener.onUpdateSuccess()
                        }
                        .addOnFailureListener { e ->
                            listener.onUpdateFail(e.localizedMessage)
                        }
                }
            }
        }
    }

    fun getListApprovePlayer(idCaptain: String, listener: GetDataListener<ApprovePlayerResponse>) {

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionRequestJoinClub)
            .whereEqualTo("idCaptain", idCaptain)
            .get()

            .addOnSuccessListener { document ->

                val value = document?.toObjects(ApprovePlayerResponse::class.java)

                if (value != null) {
                    listener.onSuccess(ArrayList(value))
                } else {

                    listener.onFail("Not Found")
                }
                Log.d("Tien", document.metadata.toString())
            }
            .addOnFailureListener { exception ->
                listener.onFail(exception.localizedMessage)
            }
    }

    fun approvePlayer(idClub: String, userModel: UserModel, isAccept: Boolean, listener: UpdateListener) {
        // update request db
        val userStickModel = UserStickModel(userModel.id, userModel.photoUrl, userModel.name, userModel.position)
        val dbClub = FirebaseFirestore.getInstance().collection(FireBasePath().collectionClub).document(idClub)
        if (isAccept) {
            dbClub.update("players", FieldValue.arrayUnion(Gson().toJson(userStickModel)))
                .addOnSuccessListener {
                    //delete request
                    deleteDocument(
                        FireBasePath().collectionRequestJoinClub,
                        idClub + userModel.id,
                        object : DeleteDataDataListener {
                            override fun onSuccess() {
                                listener.onUpdateSuccess()
                            }

                            override fun onFail(message: String) {
                                listener.onUpdateFail(message)
                            }

                        })

                }.addOnFailureListener {
                    listener.onUpdateFail(it.localizedMessage)
                }

        } else {
            deleteDocument(
                FireBasePath().collectionRequestJoinClub,
                idClub + userModel.id,
                object : DeleteDataDataListener {
                    override fun onSuccess() {
                        listener.onUpdateSuccess()
                    }

                    override fun onFail(message: String) {
                        listener.onUpdateFail(message)
                    }

                })
        }

    }

    fun writeReviewClub(
        currentTime: Long,
        clubModel: ClubModel,
        userModel: UserModel,
        review: String,
        listener: UpdateListener
    ) {

        val commentModel = CommentModel(
            "$currentTime", userModel.id, userModel.name, userModel.photoUrl, review,
            DateTimeUtil().getFormat(currentTime, DateTimeUtil.DateFormatDefinition.DD_MM_YYYY_HH_MM.format)
        )
        val dbClub = FirebaseFirestore.getInstance().collection(FireBasePath().collectionClub).document(clubModel.id)

        dbClub.update("comments", FieldValue.arrayUnion(Gson().toJson(commentModel)))
            .addOnSuccessListener {
                listener.onUpdateSuccess()
            }
            .addOnFailureListener {
                listener.onUpdateFail(it.localizedMessage)
            }

    }

    fun getCountRequest(idCaptain: String, listener: GetDataListener<Int>) {
        FirebaseFirestore.getInstance().collection(FireBasePath().collectionRequestJoinClub)
            .whereEqualTo("idCaptain", idCaptain)
            .get()
            .addOnSuccessListener { document ->
                listener.onSuccess(document.size())
            }
            .addOnFailureListener { exception ->
                listener.onFail(exception.localizedMessage)
            }
    }

    fun getTutorial(listener: GetDataListener<TutorialModel>) {

        val list: ArrayList<TutorialModel> = ArrayList()

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionTutorial).get()

            .addOnSuccessListener { result ->

                for (document in result) {

                    val welcomeModel = TutorialModel(
                        document.data["id"] as String?,
                        document.data["photo"] as String?,
                        document.data["title"] as String?,
                        document.data["content"] as String?
                    )

                    list.add(welcomeModel)
                }

                if (list.size > 0) {
                    listener.onSuccess(list)
                }
            }
    }
}