package net.wyvernia.numtseng.learning

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/lesson")
class LessonController (
    private val lessonService: LessonService
){
    @RequestMapping("")
    fun getLessons() : ResponseEntity<Set<Lesson>>{

        return ResponseEntity.ok(lessonService.all)
    }

}