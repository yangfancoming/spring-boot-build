

package org.springframework.boot

import org.springframework.context.ConfigurableApplicationContext

import kotlin.reflect.KClass

/**
 * Top level function acting as a Kotlin shortcut allowing to write
 * `runApplication<FooApplication>(arg1, arg2)` instead of
 * `SpringApplication.run(FooApplication::class.java, arg1, arg2)`.
 *
 * @author Sebastien Deleuze
 * @since 2.0.0
 */
inline fun <reified T : Any> runApplication(vararg args: String): ConfigurableApplicationContext =
		SpringApplication.run(T::class.java, *args)

/**
 * Top level function acting as a Kotlin shortcut allowing to write
 * `runApplication<FooApplication>(arg1, arg2) { // SpringApplication customization ... }`
 * instead of instantiating `SpringApplication` class, customize it and then invoking
 * `run(arg1, arg2)`.
 *
 * @author Sebastien Deleuze
 * @since 2.0.0
 */
inline fun <reified T : Any> runApplication(vararg args: String, init: SpringApplication.() -> Unit): ConfigurableApplicationContext =
		SpringApplication(T::class.java).apply(init).run(*args)
