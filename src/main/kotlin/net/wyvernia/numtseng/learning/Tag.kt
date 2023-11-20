package net.wyvernia.numtseng.learning

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.*

@Entity
class Tag(
    @Id
    @Column(name = "id")
    var name: String? = null,
) {

}