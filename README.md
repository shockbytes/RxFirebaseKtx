# RxFirebaseKtx

Handy Kotlin extension functions for using Firebase Database in combination with RxJava.

# Sample usage

Consider this `SampleContent` class used for all further operations.
```Kotlin
    data class SampleContent(
        val id: String = "",
        val content: String
    ): FirebaseStorable {
        override fun copyWithNewId(newId: String): FirebaseStorable {
            return copy(id = newId)
        }
    }

    private val firebaseDatabase = FirebaseDatabase.getInstance().reference.database

```

### Forward changes of a collection
```Kotlin
    fun listSubjectFromFirebase() {

        val subject = PublishSubject.create<List<SampleContent>>()

        subject.fromFirebase(
            dbRef = firebaseDatabase.getReference("sample_content"),
            changedChildKeySelector = { it.id }, // Consider the field id as unique for SampleContent
            cancelHandler = { dbError ->
                Timber.e(dbError.toException())
            }
        )
    }
```
### Listen for changes in a collection, directly on the reference
```Kotlin
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
```

### Forward changes of a single value
```Kotlin
    fun singleValueSubjectFromFirebase() {

        val subject = PublishSubject.create<SampleContent>()

        subject.fromFirebase(
            dbRef = firebaseDatabase.getReference("sample_content"),
            errorHandler = { dbError ->
                Timber.e(dbError.toException())
            }
        )
    }
```

### Listen for changes of a single value, directly on the reference
```Kotlin
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
```

### Insert a new value in the database
```Kotlin
    fun firebaseDatabaseInsertValue() {

        val update = SampleContent(content = "This library is awesome!")

        firebaseDatabase.insertValue(reference = "sample_content", value = update)
    }
```

### Update a value in the database
```Kotlin
    fun firebaseDatabaseUpdateValue() {

        val update = SampleContent(id = "id1", content = "This library is awesome!")

        firebaseDatabase.updateValue(
            reference = "sample_content",
            childId = update.id,
            value = update
        )
    }
```

### Delete a child value in the database
```Kotlin
    fun firebaseDatabaseRemoveChildValue() {

        val update = SampleContent(id = "id1", content = "This library is awesome!")

        firebaseDatabase.removeChildValue(
            reference = "sample_content",
            childId = update.id
        )
    }
```

### Wrap the deletion of a value into a `Completable`

```Kotlin
    fun firebaseDatabaseReactiveRemoveValue() {

        val update = SampleContent(id = "id1", content = "This library is awesome!")

        firebaseDatabase.reactiveRemoveValue(reference = update.id)
            .subscribe {
                Timber.d("Successfully removed ${update.id}!")
            }
    }
```
