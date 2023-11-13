package net.wyvernia.numtseng.learning

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name="Exercise")
data class Exercise(
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    var id: UUID? = null,
    var prompt: String? = null,
    var tags: String? = null,
    @OneToMany( fetch = FetchType.EAGER)
    var questions: List<Question> = listOf()
) {

}