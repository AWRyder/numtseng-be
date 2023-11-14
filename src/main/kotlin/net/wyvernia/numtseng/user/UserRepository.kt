package net.wyvernia.numtseng.user

import net.wyvernia.numtseng.common.AbstractRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository : AbstractRepository<User, UUID> {

    fun findByLoginEquals(login:String) : User?
    @Query(" SELECT u.hashedPassword FROM User u WHERE u.login = :username")
    fun findHashedPasswordByUserLogin(username: String) : String?
    @Query(" SELECT u.enabled FROM User u WHERE u.login = :username")
    fun findUserIsEnabledByLogin(username: String): Boolean
}