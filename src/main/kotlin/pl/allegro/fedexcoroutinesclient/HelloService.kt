package pl.allegro.fedexcoroutinesclient

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class HelloService(
    @Autowired
    val client: HelloFeignClient
) {

    fun callHelloSuspendedNo1(): List<String> {
        logThreadInfo("Top level")
        return runBlocking {
            val deferred1 = async { callHello5Seconds() }.await()
            val deferred2 = async { callHello10Seconds() }.await()
            listOf(deferred1, deferred2)
        }
    }

    fun callHelloSuspendedNo2(): List<String> {
        logThreadInfo("Top level")
        return runBlocking {
            val deferred1 = async { callHello5Seconds() }
            val deferred2 = async { callHello10Seconds() }
            awaitAll(deferred1, deferred2)
        }
    }

    fun callHelloSuspendedNo3(): List<String> {
        logThreadInfo("Top level")
        return runBlocking {
            val deferred1 = async(newSingleThreadContext("MyOwnThread1")) { callHello5Seconds() }
            val deferred2 = async(newSingleThreadContext("MyOwnThread2")) { callHello10Seconds() }
            awaitAll(deferred1, deferred2)
        }
    }

    private fun callHello5Seconds(): String {
        logThreadInfo("callHello5Seconds")
        return client.getData5Seconds()!!.text
    }

    private fun callHello10Seconds(): String {
        logThreadInfo("callHello10Seconds")
        return client.getData10Seconds()!!.text
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
