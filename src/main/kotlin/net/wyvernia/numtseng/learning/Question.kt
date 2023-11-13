package net.wyvernia.numtseng.learning

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import java.util.*

@Entity
@Table(name = "Question")
data class Question (
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    var id: UUID? = null,

    var question:String? = null,
    var answers:String? = null,

    ) {

}