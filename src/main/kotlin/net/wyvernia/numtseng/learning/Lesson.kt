package net.wyvernia.numtseng.learning

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.util.*

@Entity
data class Lesson (
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    var id: UUID? = null,
    var description: String? = null,
    @OneToMany(fetch = FetchType.EAGER)
    var exercises: List<Exercise> = listOf()
) {

}