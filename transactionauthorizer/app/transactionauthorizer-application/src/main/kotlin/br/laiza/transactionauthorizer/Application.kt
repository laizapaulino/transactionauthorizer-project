package br.laiza.transactionauthorizer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["br.laiza.transactionauthorizer"])
open class Application

	fun main(args: Array<String>) {
		runApplication<Application>(*args)
	}
