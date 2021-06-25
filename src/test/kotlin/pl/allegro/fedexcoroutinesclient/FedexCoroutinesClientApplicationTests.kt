package pl.allegro.fedexcoroutinesclient

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FedexCoroutinesClientApplicationTests(
	@Autowired val service: HelloService
) {

	@Test
	fun contextLoads() {
		logger.info("Begin")
		service.callHelloSuspendedNo3()
		logger.info("The end")
	}

	companion object {
		val logger = logger()
	}

}
