package com.bongdaphui.base

import android.util.Log
import com.bongdaphui.approvePlayer.ApprovePlayerResponse
import com.bongdaphui.listener.DeleteDataDataListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.UpdateListener
import com.bongdaphui.model.*
import com.bongdaphui.utils.Constant
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson


class BaseRequest {

    fun removeImage(urlImage: String) {

        val photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(urlImage)

        photoRef.delete().addOnSuccessListener {
            // File deleted successfully
            Log.d(Constant().TAG, "onSuccess: deleted file")
        }.addOnFailureListener {
            // Uh-oh, an error occurred!
            Log.d(Constant().TAG, "onFailure: did not delete file")
        }
    }

    fun getDataField(listener: GetDataListener<FbFieldModel>) {

        val db = FirebaseFirestore.getInstance().collection(Constant().collectionPathField)

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
                        document.data["address"] as String?,
                        document.data["amountField"] as String?,
                        document.data["price"] as String?,
                        document.data["lat"] as String?,
                        document.data["lng"] as String?,
                        document.data["countRating"] as String?,
                        document.data["rating"] as String?
                    )
                    fieldList.add(fbFieldModel)
                }

                Log.d(Constant().TAG, "field size: ${fieldList.size}")

                if (fieldList.size > 0) {

                    listener.onSuccess(fieldList)

                } else {

                    listener.onFail("Chưa có dữ liệu")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(Constant().TAG, "Error getting documents: ", exception)
                listener.onFail("${exception.message}")
            }
    }

    fun getUserInfo(uid: String, listener: GetDataListener<UserModel>) {

        FirebaseFirestore.getInstance().collection(Constant().userPathField).document(uid).get()

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
        listener: UpdateListener
    ) {
        val db = FirebaseFirestore.getInstance().collection(Constant().userPathField).document(userModel.id)
        db.set(userModel, SetOptions.merge())
            .addOnSuccessListener {
                listener.onUpdateSuccess()
            }
            .addOnFailureListener {
                listener.onUpdateFail()
            }
    }

    fun getSchedulePlayer(listener: GetDataListener<SchedulePlayerModel>) {

        val schedulePlayerList: ArrayList<SchedulePlayerModel> = ArrayList()

        FirebaseFirestore.getInstance().collection(Constant().schedulePlayerPathField).get()

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
                Log.d(Constant().TAG, "schedule player size: ${schedulePlayerList.size}")

                if (schedulePlayerList.size > 0) {

                    listener.onSuccess(schedulePlayerList)

                } else {

                    listener.onFail("Chưa có dữ liệu")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(Constant().TAG, "Error getting documents: ", exception)
                listener.onFail("${exception.message}")
            }
    }

    fun getScheduleClub(listener: GetDataListener<ScheduleClubModel>) {

        val scheduleClubList: ArrayList<ScheduleClubModel> = ArrayList()

        FirebaseFirestore.getInstance().collection(Constant().scheduleClubPathField).get()

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
                Log.d(Constant().TAG, "schedule club size: ${scheduleClubList.size}")

                if (scheduleClubList.size > 0) {

                    listener.onSuccess(scheduleClubList)

                } else {

                    listener.onFail("Chưa có dữ liệu")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(Constant().TAG, "Error getting documents: ", exception)
                listener.onFail("${exception.message}")
            }
    }

    fun deleteDocument(
        collection: String, document: String, listener: DeleteDataDataListener
    ) {

        val db = FirebaseFirestore.getInstance().collection(collection).document(document)
        db.delete()
            .addOnSuccessListener {
                Log.d(Constant().TAG, "DocumentSnapshot successfully deleted!")
                listener.onSuccess()
            }
            .addOnFailureListener {
                Log.w(Constant().TAG, "Error deleting document ${it.message}")
                listener.onFail("${it.message}")
            }
    }

    fun saveOrUpdateClub(clubModel: ClubModel, listener: UpdateListener) {

        FirebaseFirestore.getInstance().collection(Constant().clubPathField).document("${clubModel.id}")

            .set(clubModel, SetOptions.merge())

            .addOnSuccessListener {
                listener.onUpdateSuccess()
            }
            .addOnFailureListener {
                listener.onUpdateFail()
            }
    }

    fun getClubInfo(id: String, listener: GetDataListener<ClubModel>) {

        FirebaseFirestore.getInstance().collection(Constant().clubPathField).document(id).get()

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


            FirebaseFirestore.getInstance().collection(Constant().clubPathField).get()

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
            Log.d(Constant().TAG, "crash ${e.message}")

        }
    }

    fun registerJoinClub(clubModel: ClubModel, userModel: UserModel, message: String, listener: UpdateListener) {

        val db = FirebaseFirestore.getInstance().collection(Constant().requestJoinPathField)
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

        FirebaseFirestore.getInstance().collection(Constant().requestJoinPathField).whereEqualTo("idCaptain", idCaptain)
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
        val dbClub = FirebaseFirestore.getInstance().collection(Constant().clubPathField).document(idClub)

        dbClub.update("players", FieldValue.arrayUnion(Gson().toJson(userStickModel)))
            .addOnSuccessListener {
                //update clubs db
                val db = FirebaseFirestore.getInstance().collection(Constant().requestJoinPathField)
                //check exist
                deleteDocument(Constant().requestJoinPathField, idClub + userModel.id, object : DeleteDataDataListener {
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

    }


}