package pl.allegro.fedexcoroutinesclient

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping


@FeignClient(name = "simple-client", url = "http://localhost:8801")
interface HelloFeignClient {
    @GetMapping("/hello?delay=5")
    fun getData5Seconds(): HelloResponse?

    @GetMapping("/hello?delay=10")
    fun getData10Seconds(): HelloResponse?
}
