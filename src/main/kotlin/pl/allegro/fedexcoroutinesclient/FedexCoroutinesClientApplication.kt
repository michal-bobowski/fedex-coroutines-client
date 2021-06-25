package pl.allegro.fedexcoroutinesclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
class FedexCoroutinesClientApplication

fun main(args: Array<String>) {
	runApplication<FedexCoroutinesClientApplication>(*args)
}
