package com.example;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class FluxExample {
    public static void main(String[] args) throws InterruptedException {
        // Flux 생성 및 비동기 논블로킹 처리
        Flux<Integer> flux = Flux.range(1, 5)
                .map(i -> {
                    System.out.println("[map] Transforming " + i + " to " + (i * 2));
                    return i * 2;
                })
                .subscribeOn(Schedulers.parallel()) // 비동기적으로 실행 (별도 스레드)
                .delayElements(Duration.ofMillis(500)) // 비동기 지연 추가
                .doOnNext(i -> System.out.println("[doOnNext] Received: " + i))
                .doOnComplete(() -> System.out.println("Processing Complete"));

        // 비동기 실행을 위해 잠시 대기 (데모용)
        flux.subscribe();

        do {
            System.out.println("########  Main thread is running...");
            Thread.sleep(3000);
        } while (true);
    }
}
