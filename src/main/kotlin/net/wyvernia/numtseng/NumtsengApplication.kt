package net.wyvernia.numtseng

import net.wyvernia.numtseng.config.NumtsengConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(NumtsengConfiguration::class)
class NumtsengApplication

fun main(args: Array<String>) {
    runApplication<NumtsengApplication>(*args)
}
