package net.wyvernia.numtseng.common

import net.wyvernia.numtseng.exceptions.NotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable
import java.util.function.Supplier

@NoRepositoryBean
interface AbstractRepository<T, ID : Serializable> : CrudRepository<T, ID>, JpaRepository<T, ID>,
    JpaSpecificationExecutor<T> {
    override fun findAll(): List<T>
    fun findRequiredById(id: ID): T {
        return findById(id).orElseThrow<RuntimeException>(Supplier<RuntimeException> { NotFoundException() })
    }
}