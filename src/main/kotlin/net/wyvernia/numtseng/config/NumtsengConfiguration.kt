package net.wyvernia.numtseng.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "numtseng")
class NumtsengConfiguration (
    var jwt: JwtConfiguration? = null
) {

}