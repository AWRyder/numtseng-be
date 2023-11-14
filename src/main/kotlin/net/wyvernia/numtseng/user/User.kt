package net.wyvernia.numtseng.user

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name="SEC_User")
class User (
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    var id: UUID? = null,

    var login: String? = null,
    var hashedPassword: String = "",
    var enabled: Boolean? = null,
    var lastLogin: LocalDateTime? = null,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf()
    }

    override fun getPassword(): String = hashedPassword

    override fun getUsername(): String  = login ?: ""

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled ?: false
}