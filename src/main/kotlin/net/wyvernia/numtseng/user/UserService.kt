package net.wyvernia.numtseng.user

import net.wyvernia.numtseng.common.AbstractCrudService
import net.wyvernia.numtseng.exceptions.NotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    userRepository: UserRepository
) : AbstractCrudService<UserRepository, User, UUID>(userRepository) {

    fun findUserByLogin(login:String): User{
        return repository.findByLoginEquals(login) ?: throw NotFoundException()
    }

    fun findHashedPasswordByUserLogin(login: String): String? {
        return repository.findHashedPasswordByUserLogin(login)
    }

    fun findUserIsEnabledByLogin(login: String): Boolean {
        return repository.findUserIsEnabledByLogin(login)
    }
}