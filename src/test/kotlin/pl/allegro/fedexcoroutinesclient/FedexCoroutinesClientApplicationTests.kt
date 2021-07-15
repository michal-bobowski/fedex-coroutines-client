package pl.allegro.fedexcoroutinesclient

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertTimeout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration

@SpringBootTest
class FedexCoroutinesClientApplicationTests(
	@Autowired val service: HelloService
) {

	@Test
	fun shouldCallEndpointsWithDelay() {
		logger.info("Begin")
		assertTimeout(Duration.ofSeconds(15)) {
			service.callHelloSuspendedNo5()
		}
		logger.info("The end")
	}

	companion object {
		val logger = logger()
	}

}
