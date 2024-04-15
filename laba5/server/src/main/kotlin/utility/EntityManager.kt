package utility

import dataBaseHandler
import entity.Flat
import entity.View
import java.time.LocalDateTime
import java.util.concurrent.locks.ReentrantReadWriteLock

class EntityManager() {

    private var flatCollection = LinkedHashSet<Flat>()
    private var lastInitTime: LocalDateTime? = null
    private var lastSaveTime: LocalDateTime? = null
    private var collectionLocker = ReentrantReadWriteLock()


    init {
        dataBaseHandler.connectToDataBase()
        flatCollection = dataBaseHandler.loadCollectionFromBD()
    }

    fun getFlatCollection(): LinkedHashSet<Flat>{
        collectionLocker.readLock().lock()
        try {
            return flatCollection
        } finally {
            collectionLocker.readLock().unlock()
        }
    }

    fun getLastInitTime(): String {
        return if (lastInitTime == null) {
            "иницилизация еще не произовидилась"
        } else lastInitTime.toString()
    }

    fun getLastSaveTime(): String {
        return if (lastSaveTime == null) {
            "сохранение еще не производилось"
        } else lastSaveTime.toString()
    }

    fun getCollectionType(): String {
        return this.flatCollection.javaClass.typeName
    }

    fun getCollectionSize(): Int {
        return this.flatCollection.size
    }

    fun getById(id: Int): Flat? {
        collectionLocker.readLock().lock()
        try {
            return flatCollection.find { flat -> flat.getId() == id }
        } finally {
            collectionLocker.readLock().unlock()
        }
    }

    fun generateId(): Int {
        if (flatCollection.isEmpty()) return 1
        return flatCollection.last().getId() + 1
    }

    fun setLastSaveTime(data: LocalDateTime) {
        this.lastSaveTime = data
    }

    fun addObjectToCollection(flat: Flat) {
        collectionLocker.writeLock().lock()
        try {
            flatCollection.add(flat)
        } finally {
            collectionLocker.writeLock().unlock()
        }
    }

    fun removeFromCollection(flat: Flat) {
        collectionLocker.writeLock().lock()
        try {
            flatCollection.remove(flat)
        } finally {
            collectionLocker.writeLock().unlock()
        }
    }

    fun areaMin(int: Int): Boolean {
        return int < flatCollection.minOf { flat -> flat.getArea() }
    }

    fun areaMax(int: Int): Boolean {
        return int > flatCollection.maxOf { flat -> flat.getArea() }
    }

    fun clearCollection(username: String) {
        collectionLocker.writeLock().lock()
        try {
            flatCollection.removeIf { flat -> flat.getUserName() == username}
        } finally {
            collectionLocker.writeLock().unlock()
        }
    }

    fun getCountLessThanHouse(): Int {
        return 0
    }

    fun getInDescending(): String {
        val list = ArrayList<String>()
        ((flatCollection.sortedByDescending { flat -> flat.getId() }).forEach { flat -> list.add(flat.getId().toString()) })
        return list.joinToString()
    }

    fun getViewInDescending(): String {
        val list = View.values()
        list.sortedDescending().forEach {  }
        return list.joinToString()
    }

    override fun toString(): String {
        if (flatCollection.isEmpty()) return "Коллекция пустая"

        var collection = ""
        for(flat in flatCollection) {
            collection += flat
            if(flat != flatCollection.last()) collection += "\n"
        }
        return collection
    }
}
