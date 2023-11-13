package net.wyvernia.numtseng.learning

import net.wyvernia.numtseng.common.AbstractCrudService
import org.springframework.stereotype.Service
import java.util.*

@Service
class LessonService(
    lessonRepository: LessonRepository,
) : AbstractCrudService<LessonRepository, Lesson, UUID>(lessonRepository) {

}