package com.bongdaphui.base

import android.util.Log
import com.bongdaphui.approvePlayer.ApprovePlayerResponse
import com.bongdaphui.listener.DeleteDataDataListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.UpdateListener
import com.bongdaphui.model.*
import com.bongdaphui.utils.Constant
import com.bongdaphui.utils.FireBasePath
import com.bongdaphui.utils.Utils
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList


class BaseRequest {

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

        val fieldList: ArrayList<FbFieldModel> = ArrayList()

        db.orderBy("name").get()
            .addOnSuccessListener { result ->

                for (document in result) {

                    val fbFieldModel = FbFieldModel(
                        document.data["id"] as Long?,
                        document.data["idCity"] as String?,
                        document.data["idDistrict"] as String?,
                        document.data["photoUrl"] as String?,
                        document.data["name"] as String?,
                        document.data["phone"] as String?,
                        document.data["phone2"] as String?,
                        document.data["address"] as String?,
                        document.data["amountField"] as String?,
                        document.data["price"] as String?,
                        document.data["priceMax"] as String?,
                        document.data["lat"] as String?,
                        document.data["lng"] as String?,
                        document.data["countRating"] as String?,
                        document.data["rating"] as String?
                    )
                    fieldList.add(fbFieldModel)
                }

                if (fieldList.size > 0) {

                    listener.onSuccess(fieldList)

                } else {

                    listener.onFail("Chưa có dữ liệu")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(Constant().tag, "Error getting documents: ", exception)
                listener.onFail("${exception.message}")
            }
    }

    fun getUserInfo(uid: String, listener: GetDataListener<UserModel>) {

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionUser).document(uid).get()

            .addOnSuccessListener { document ->

                val value = document?.toObject(UserModel::class.java)

                if (value != null) {

                    listener.onSuccess(value)

                } else {

                    listener.onFail("Not Found")
                }
            }
        /*.addOnFailureListener { exception ->
            listener.onFail(exception.localizedMessage)
        }*/
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

        val schedulePlayerList: ArrayList<SchedulePlayerModel> = ArrayList()

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionSchedulePlayer).get()

            .addOnSuccessListener { result ->

                for (document in result) {

                    val schedulePlayerModel = SchedulePlayerModel(
                        document.data["id"] as String?,
                        document.data["idCity"] as String?,
                        document.data["idDistrict"] as String?,
                        document.data["startTime"] as String?,
                        document.data["endTime"] as String?,
                        document.data["idPlayer"] as String?,
                        document.data["namePlayer"] as String?,
                        document.data["phonePlayer"] as String?,
                        document.data["photoUrlPlayer"] as String?,
                        document.data["typeField"] as String?
                    )
                    schedulePlayerList.add(schedulePlayerModel)
                }
                Log.d(Constant().tag, "schedule player size: ${schedulePlayerList.size}")

                if (schedulePlayerList.size > 0) {

                    listener.onSuccess(schedulePlayerList)

                } else {

                    listener.onFail("Chưa có dữ liệu")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(Constant().tag, "Error getting documents: ", exception)
                listener.onFail("${exception.message}")
            }
    }

    fun getScheduleClub(listener: GetDataListener<ScheduleClubModel>) {

        val scheduleClubList: ArrayList<ScheduleClubModel> = ArrayList()

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionScheduleClub).get()

            .addOnSuccessListener { result ->

                for (document in result) {

                    val scheduleClubModel = ScheduleClubModel(
                        document.data["id"] as String?,
                        document.data["idClub"] as String?,
                        document.data["idCaptain"] as String?,
                        document.data["idCity"] as String?,
                        document.data["idDistrict"] as String?,
                        document.data["startTime"] as String?,
                        document.data["endTime"] as String?,
                        document.data["nameClub"] as String?,
                        document.data["phone"] as String?,
                        document.data["photoUrl"] as String?,
                        document.data["typeField"] as String?
                    )

                    scheduleClubList.add(scheduleClubModel)
                }
                Log.d(Constant().tag, "schedule club size: ${scheduleClubList.size}")

                if (scheduleClubList.size > 0) {

                    listener.onSuccess(scheduleClubList)

                } else {

                    listener.onFail("Chưa có dữ liệu")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(Constant().tag, "Error getting documents: ", exception)
                listener.onFail("${exception.message}")
            }
    }

    fun deleteDocument(
        collection: String, document: String, listener: DeleteDataDataListener
    ) {

        val db = FirebaseFirestore.getInstance().collection(collection).document(document)
        db.delete()
            .addOnSuccessListener {
                //                Log.d(Constant().tag, "DocumentSnapshot successfully deleted!")
                listener.onSuccess()
            }
            .addOnFailureListener {
                //                Log.w(Constant().tag, "Error deleting document ${it.message}")
                listener.onFail("${it.message}")
            }
    }

    fun saveOrUpdateClub(clubModel: ClubModel, listener: UpdateListener) {

        FirebaseFirestore.getInstance().collection(FireBasePath().collectionClub).document("${clubModel.id}")

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

                    listener.onFail("Có lỗi khi lấy thông tin đội bóng!")
                }
            }.addOnFailureListener {
                listener.onFail("Có lỗi khi lấy thông tin đội bóng!")
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

    fun writeReviewClub(clubModel: ClubModel, userModel: UserModel, review: String, listener: UpdateListener) {

        val currentTime = Calendar.getInstance().timeInMillis
        val id = "${Utils().getRandomNumberString()}$currentTime"

        val commentModel = CommentModel(id, userModel.id, userModel.name, review)
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