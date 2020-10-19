package at.shockbytes.firebase

interface FirebaseStorable {

    fun copyWithNewId(newId: String): FirebaseStorable
}