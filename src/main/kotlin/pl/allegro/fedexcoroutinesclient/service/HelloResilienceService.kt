package pl.allegro.fedexcoroutinesclient.service

import io.github.resilience4j.kotlin.retry.executeFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.allegro.fedexcoroutinesclient.api.HelloFeignClient
import pl.allegro.fedexcoroutinesclient.logger
import java.time.Duration
import java.util.concurrent.Executors

@Component
class HelloResilienceService(
    @Autowired
    val client: HelloFeignClient
) {

    fun callResilientHelloNo1(): List<String> {
        val dispatcher = Executors.newFixedThreadPool(5).asCoroutineDispatcher()
        return runBlocking(dispatcher) {
            val deferred1 = async { retry().executeFunction { callHelloWithErrors() } }
            val deferred2 = async { callHello5Seconds() }
            listOf(deferred1, deferred2).awaitAll()
        }
    }

    private fun retry(): Retry {
        val config: RetryConfig = RetryConfig.custom<Any>()
            .maxAttempts(10)
            .waitDuration(Duration.ofMillis(1000))
            .build()
        return Retry.of("", config)
    }

    private fun callHello5Seconds(): String {
        logThreadInfo("callHello5Seconds")
        return client.getData5Seconds()!!.text
    }

    private fun callHelloWithErrors(): String {
        logThreadInfo("callHelloWithErrors")
        return client.getDataWithErrors()!!.text
    }

    private fun logThreadInfo(prefix: String) {
        val thread = Thread.currentThread()
        val threadName = thread.name
        val threadId = thread.id
        logger.info("$prefix: name=$threadName, id=$threadId")
    }

    companion object {
        val logger = logger()
    }

}
