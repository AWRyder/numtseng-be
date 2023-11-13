package net.wyvernia.numtseng.common

import java.io.Serializable
import java.util.*

abstract class AbstractCrudService<R : AbstractRepository<T, ID>?, T:Any, ID : Serializable>(
    private var repository: R,
) {
    val all: Set<T>
        get() = HashSet(repository!!.findAll())

    fun getById(id: ID): Optional<T> {
        return repository!!.findById(id)
    }

    fun save(toPersist: T): T {
        return repository!!.save(toPersist)
    }

    fun saveAll(toPersist: Collection<T>): Collection<T> {
        return repository!!.saveAll(toPersist)
    }

    fun delete(toPersist: T): T {
        repository!!.delete(toPersist)
        return toPersist
    }

    fun deleteById(id: ID) {
        repository!!.deleteById(id)
    }
}