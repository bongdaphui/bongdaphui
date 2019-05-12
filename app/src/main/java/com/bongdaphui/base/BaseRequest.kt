package com.bongdaphui.base

import android.content.Context
import android.util.Log
import com.bongdaphui.MainActivity
import com.bongdaphui.listener.CheckUserListener
import com.bongdaphui.listener.FireBaseSuccessListener
import com.bongdaphui.listener.UpdateUserListener
import com.bongdaphui.model.FbFieldModel
import com.bongdaphui.model.UserModel
import com.bongdaphui.utils.Constant
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
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

    fun loadUserData(context: Context, idUser: String, listener: FireBaseSuccessListener) {

        if (context is MainActivity) {

            context.showProgress(true)
        }

        val ref = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_USER)

        ref.child(idUser).addValueEventListener(object : ValueEventListener {

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

    //if user first time open app, create data on firrebasse
    fun createUserDataOnFireBase(userId: String?, listener: UpdateUserListener) {

        // Assign FirebaseDatabase instance with root database name.
        val databaseReference = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_USER)

        val userModel = UserModel(
            userId, "", "", "", "", ""
        )

        // Adding image upload id s child element into databaseReference.
        databaseReference.child(userId!!).setValue(userModel)

            .addOnCompleteListener {

                listener.onUpdateSuccess()
            }
            .addOnFailureListener {

                listener.onUpdateFail()
            }
    }

    fun updateUserDataOnFireBase(userModel: UserModel, listener: UpdateUserListener) {

        // Assign FirebaseDatabase instance with root database name.
        val databaseReference = FirebaseDatabase.getInstance().getReference(Constant().DATABASE_USER)

        // Adding image upload id s child element into databaseReference.
        databaseReference.child("${userModel.id}").setValue(userModel)

            .addOnCompleteListener {

                listener.onUpdateSuccess()
            }
            .addOnFailureListener {

                listener.onUpdateFail()
            }
    }

    fun removeImage(urlImage: String) {
        val photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(urlImage)

        photoRef.delete().addOnSuccessListener(OnSuccessListener<Void> {
            // File deleted successfully
            Log.d(Constant().TAG, "onSuccess: deleted file")
        }).addOnFailureListener(OnFailureListener {
            // Uh-oh, an error occurred!
            Log.d(Constant().TAG, "onFailure: did not delete file")
        })
    }

    fun saveDataField(context: Context, path: String, fbFieldModel: FbFieldModel) {

        val db = FirebaseFirestore.getInstance().document(path)

        db.set(fbFieldModel)
            .addOnSuccessListener {
                Log.d(Constant().TAG, "upload field success")

            }
            .addOnFailureListener {
                Log.d(Constant().TAG, "upload field fail : $it")

            }
    }
}