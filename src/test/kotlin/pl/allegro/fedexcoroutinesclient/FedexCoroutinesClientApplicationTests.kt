package pl.allegro.fedexcoroutinesclient

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertTimeout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.allegro.fedexcoroutinesclient.service.HelloErrorsService
import pl.allegro.fedexcoroutinesclient.service.HelloResilienceService
import pl.allegro.fedexcoroutinesclient.service.HelloService
import java.time.Duration

@SpringBootTest
class FedexCoroutinesClientApplicationTests(
	@Autowired val service: HelloService,
	@Autowired val errorsService: HelloErrorsService,
	@Autowired val resilientService: HelloResilienceService,
) {

	@Test
	fun shouldCallEndpointsWithDelay() {
		logger.info("Begin")
		assertTimeout(Duration.ofSeconds(15)) {
			service.callHelloSuspendedNo5()
		}
		logger.info("The end")
	}

	@Test
	fun shouldCallEndpointsWithErrors() {
		logger.info("Begin")
		assertTimeout(Duration.ofSeconds(15)) {
			errorsService.callHelloErrorsNo3()
		}
		logger.info("The end")
	}

	@Test
	fun shouldCallEndpointsWithResilientService() {
		logger.info("Begin")
		assertTimeout(Duration.ofSeconds(15)) {
			resilientService.callResilientHelloNo2()
		}
		logger.info("The end")
	}

	companion object {
		val logger = logger()
	}

}
