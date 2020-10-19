package at.shockbytes.sample

import at.shockbytes.firebase.FirebaseStorable
import at.shockbytes.firebase.rx3.*
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber


object Rx3Samples {

    data class SampleContent(
        val id: String = "",
        val content: String
    ): FirebaseStorable {
        override fun copyWithNewId(newId: String): FirebaseStorable {
            return copy(id = newId)
        }
    }

    private val firebaseDatabase = FirebaseDatabase.getInstance().reference.database


    fun listSubjectFromFirebase() {

        val subject = PublishSubject.create<List<SampleContent>>()

        subject.fromFirebase(
            dbRef = firebaseDatabase.getReference("sample_content"),
            changedChildKeySelector = { it.id },
            cancelHandler = { dbError ->
                Timber.e(dbError.toException())
            }
        )
    }

    fun listFromFirebaseDatabaseListen() {

        val subject = PublishSubject.create<List<SampleContent>>()

        firebaseDatabase.listen(
            reference = "sample_content",
            relay = subject,
            changedChildKeySelector = { it.id },
            cancelHandler = { dbError ->
                Timber.e(dbError.toException())
            }
        )
    }

    fun singleValueSubjectFromFirebase() {

        val subject = PublishSubject.create<SampleContent>()

        subject.fromFirebase(
            dbRef = firebaseDatabase.getReference("sample_content"),
            errorHandler = { dbError ->
                Timber.e(dbError.toException())
            }
        )
    }

    fun fromFirebaseDatabaseListenForValue() {

        val subject = PublishSubject.create<SampleContent>()

        firebaseDatabase.listenForValue(
            reference = "sample_content",
            childReference = "child_1",
            relay = subject,
            errorHandler = { dbError ->
                Timber.e(dbError.toException())
            }
        )
    }

    fun firebaseDatabaseInsertValue() {

        val update = SampleContent(content = "This library is awesome!")

        firebaseDatabase.insertValue(reference = "sample_content", value = update)
    }

    fun firebaseDatabaseUpdateValue() {

        val update = SampleContent(id = "id1", content = "This library is awesome!")

        firebaseDatabase.updateValue(
            reference = "sample_content",
            childId = update.id,
            value = update
        )
    }

    fun firebaseDatabaseRemoveChildValue() {

        val update = SampleContent(id = "id1", content = "This library is awesome!")

        firebaseDatabase.removeChildValue(
            reference = "sample_content",
            childId = update.id
        )
    }

    fun firebaseDatabaseReactiveRemoveValue() {

        val update = SampleContent(id = "id1", content = "This library is awesome!")

        firebaseDatabase.reactiveRemoveValue(reference = update.id)
            .subscribe {
                Timber.d("Successfully removed ${update.id}!")
            }
    }
}