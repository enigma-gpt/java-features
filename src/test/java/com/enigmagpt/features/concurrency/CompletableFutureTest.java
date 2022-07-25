package com.enigmagpt.features.concurrency;

import lombok.extern.log4j.Log4j2;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Log4j2
class CompletableFutureTest {

    @Test
    void testOne() {
        long start = System.currentTimeMillis();

        CompletableFuture<Function<List<Integer>, List<Integer>>> cf = CompletableFuture.supplyAsync(fillBigList())
                .thenApply(t -> applyToBigList())
                .whenComplete((r, e) -> onComplete(start));

        Awaitility.await().atMost(Duration.ofSeconds(5)).until(cf::isDone);
    }

    private void onComplete(long start) {
        log.error("The computations are complete! Time millis: {}", System.currentTimeMillis() - start);
    }

    private Supplier<List<Integer>> fillBigList() {
        return () -> {
            List<Integer> bigList = new LinkedList<>();
            for (int i = 0; i < 10_000_000; i++) {
                bigList.add(i);
            }
            return bigList;
        };
    }

    private Function<List<Integer>, List<Integer>> applyToBigList() {
        return bigList -> bigList.stream().map(element -> element + 1).collect(Collectors.toList());
    }
}