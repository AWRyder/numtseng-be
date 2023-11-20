package net.wyvernia.numtseng.learning

import jakarta.persistence.*
import java.util.*

@Entity
class Word(
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id")
    var id: UUID? = null,
    @Column(nullable = false)
    var word: String = "",
    @OneToMany(fetch = FetchType.EAGER)
    var tags: List<Tag> = listOf(),
    @Column(nullable = false)
    var type: String = "",
    var translation: String? = "",
    var syllables: String? = "",
) {

}