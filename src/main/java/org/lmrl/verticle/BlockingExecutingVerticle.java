package org.lmrl.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlockingExecutingVerticle extends AbstractVerticle {
    private void IOMethod(Promise<String> promise) {
        log.info("process I/O method...");
        try {
            String res = fetchDB();
            log.info("I/O method finished...");
            promise.complete(res);
        } catch (InterruptedException e) {
            log.info("I/O method error...");
            promise.fail(e);
        }
    }

    private String fetchDB() throws InterruptedException {
        Thread.sleep(2000);
        return """
                   {
                       "name": "test",
                       "age": 12
                   }
                """;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.setPeriodic(3000, $ -> vertx.executeBlocking(this::IOMethod, this::resultHandler));
    }

    private void resultHandler(AsyncResult<String> asyncResult) {
        if (asyncResult.succeeded()) {
            log.info("IO method return:{}", asyncResult.result());
        } else {
            log.error("IO method occurs error", asyncResult.cause());
        }
    }
}
