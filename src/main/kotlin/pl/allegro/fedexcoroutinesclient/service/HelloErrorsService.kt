package pl.allegro.fedexcoroutinesclient.service

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.allegro.fedexcoroutinesclient.api.HelloFeignClient
import pl.allegro.fedexcoroutinesclient.logger
import java.util.concurrent.Executors

@Component
class HelloErrorsService(
    @Autowired
    val client: HelloFeignClient
) {

    fun callHelloErrorsNo1(): List<String> {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        return runBlocking {
            val deferred1 = async(dispatcher) { callHelloWithErrors() }
            val deferred2 = async(dispatcher) { callHello5Seconds() }
            listOf(deferred1, deferred2).awaitAll()
        }
    }

    fun callHelloErrorsNo2(): List<String> {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        return runBlocking {
            val deferred1 = async(dispatcher) { callErrorWithRetry(1, 10) }
            val deferred2 = async(dispatcher) { callHello5Seconds() }
            listOf(deferred1, deferred2).awaitAll()
        }
    }

    private fun callErrorWithRetry(tryNo: Int, limit: Int): String {
        if (tryNo > limit) {
            throw RuntimeException("Too many tries")
        }
        return try {
            callHelloWithErrors()
        } catch (ex: Exception) {
            callErrorWithRetry(tryNo + 1, limit)
        }
    }

    fun callHelloErrorsNo3(): List<String> {
        var retriesNo: Int
        val retryLimit = 10

        val handler = CoroutineExceptionHandler { _, exception ->
            retriesNo =+ 1
            if (retriesNo <= retryLimit)
                callHelloWithErrors()
            else
                throw RuntimeException("Too many tries")
        }

        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        return runBlocking {
            val deferred1 = async(dispatcher + handler) { callErrorWithRetry(1, 10) } // wow!
            val deferred2 = async(dispatcher) { callHello5Seconds() }
            listOf(deferred1, deferred2).awaitAll()
        }
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
