package com.plotn.cleverNest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
//@ComponentScan(basePackages = arrayOf("com.plotn.cleverNest"))
class NestServiceApplication

fun main(args: Array<String>) {
	runApplication<NestServiceApplication>(*args)
}
