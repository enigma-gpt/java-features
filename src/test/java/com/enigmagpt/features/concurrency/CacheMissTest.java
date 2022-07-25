package com.enigmagpt.features.concurrency;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// https://vijay-vk.github.io/java-concurrency/java-cache-miss.html

@Log4j2
public class CacheMissTest {



    @Test
    void simulateCacheMiss() throws InterruptedException {

        Configurator.setAllLevels("", Level.INFO);

        List<Integer> bigIntList = new LinkedList<>();
        for (int element = 0; element < 3_000_000; element++) {
            bigIntList.add(element);
        }

        executeRandomOperationSync(bigIntList);
        executeRandomOperationSync(bigIntList);
        executeRandomOperationSync(bigIntList);
        executeRandomOperationSync(bigIntList);

        executeRandomOperationAsync(bigIntList);
        executeRandomOperationAsync(bigIntList);
        executeRandomOperationAsync(bigIntList);
        executeRandomOperationAsync(bigIntList);

        TimeUnit.SECONDS.sleep(60);
    }

    private void executeRandomOperationSync(List<Integer> bigList) {
        long currentTimeMillis = System.currentTimeMillis();
        List<Integer> collect = bigList.stream().map(elem -> elem + 1).collect(Collectors.toList());
        log.info("Sync operation finished in millis {}", System.currentTimeMillis() - currentTimeMillis);
    }

    private void executeRandomOperationAsync(List<Integer> bigList) {
        //CompletableFuture.runAsync(() -> {
            long currentTimeMillis = System.currentTimeMillis();
            List<Integer> collect = bigList.parallelStream().map(elem -> elem + 1).collect(Collectors.toList());
            log.info("Async operation finished in millis {}", System.currentTimeMillis() - currentTimeMillis);
        //});
    }
}
