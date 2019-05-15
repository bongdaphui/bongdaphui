package com.bongdaphui.base

import android.content.Context
import android.util.Log
import com.bongdaphui.MainActivity
import com.bongdaphui.listener.CheckUserListener
import com.bongdaphui.listener.FireBaseSuccessListener
import com.bongdaphui.listener.GetDataListener
import com.bongdaphui.listener.UpdateListener
import com.bongdaphui.model.ClubModel
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.model.UserModel
import com.bongdaphui.utils.Constant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage


class BaseRequest {

    fun loadData(context: Context, reference: String, listener: FireBaseSuccessListener) {

        if (context is MainActivity) {

            context.showProgress(true)
        }

        val ref = FirebaseDatabase.getInstance().getReference(reference)

        ref.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                if (context is MainActivity) {

                    context.showProgress(false)
                }

                listener.onFail("Load Data Cancel")

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    listener.onSuccess(p0)

                } else {

                    listener.onFail("Load Data Fail")

                }

                if (context is MainActivity) {

                    context.showProgress(false)
                }
            }
        })
    }


    fun checkUserExistsOnFireBase(id: String, checkUserListener: CheckUserListener) {
        val rootRef = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_USER)
            .child(id)

        rootRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                checkUserListener.onCheck(dataSnapshot.exists())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                checkUserListener.onCancel()
            }
        })
    }


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


    fun getDataField(
        listener: GetDataListener<FbFieldModel>
    ) {

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
//                        document.data["comment"] as ArrayList<CommentModel>?
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

    fun getUserInfo(
        uid: String,
        listener: GetDataListener<UserModel>
    ) {
        val db = FirebaseFirestore.getInstance().collection(Constant().userPathField).document(uid)
        db.get()
            .addOnSuccessListener { document ->
                val value = document?.toObject(UserModel::class.java)
                if (value != null) {
                    listener.onSuccess(value)
                } else {
                    listener.onFail("Not Found")
                }


            }
            .addOnFailureListener { exception ->
                listener.onFail(exception.localizedMessage)
            }
    }

    fun saveOrUpdateUser(
        userModel: UserModel,
        listener: UpdateListener
    ) {
        val db = FirebaseFirestore.getInstance().collection(Constant().userPathField).document(userModel.id.toString())
        db.set(userModel, SetOptions.merge())
            .addOnSuccessListener {
                listener.onUpdateSuccess()
            }
            .addOnFailureListener { _ ->
                listener.onUpdateFail()
            }
    }

    fun saveOrUpdateClub(
        clubModel: ClubModel,
        listener: UpdateListener
    ) {
        val db = FirebaseFirestore.getInstance().collection(Constant().clubPathField).document(clubModel.id.toString())
        db.set(clubModel, SetOptions.merge())
            .addOnSuccessListener {
                listener.onUpdateSuccess()
            }
            .addOnFailureListener { _ ->
                listener.onUpdateFail()
            }
    }

    fun getClubs(
        listener: GetDataListener<ClubModel>
    ) {
        val db = FirebaseFirestore.getInstance().collection(Constant().clubPathField)
        db.get()
            .addOnSuccessListener { document ->
                val value = document?.toObjects(ClubModel::class.java)
                if (value != null) {
                    listener.onSuccess(ArrayList(value))
                } else {
                    listener.onFail("Not Found")
                }


            }
            .addOnFailureListener { exception ->
                listener.onFail(exception.localizedMessage)
            }
    }
}