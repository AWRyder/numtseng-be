package net.wyvernia.numtseng.security

import org.springframework.security.access.prepost.PreAuthorize
import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy


@java.lang.annotation.Target(ElementType.METHOD)
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("isAuthenticated()")
annotation class IsAuthenticated