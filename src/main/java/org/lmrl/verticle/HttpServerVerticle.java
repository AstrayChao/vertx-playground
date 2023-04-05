package org.lmrl.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpServerVerticle extends AbstractVerticle {
    private static final int port = 1234;
    private long requestCounter = 0;

    @Override
    public void start(Promise<Void> promise) {
        log.info("I: {}", config().getInteger("n", -1));
        vertx.setPeriodic(10000, $ -> log.info("current request count: {}", requestCounter));
        vertx
                .createHttpServer()
                .requestHandler(req -> {
                    log.info("request {} from {}", requestCounter++, req.remoteAddress().host());
                    req.response().putHeader("Content-Type", "application/json");
                    req.response().end("hello " + req.remoteAddress().host());
                })
                .listen(port, ar -> {
                    if (ar.succeeded()) {
                        log.info("vert.x server started on port:{}", port);
                        promise.complete();
                    } else {
                        log.error("server failed", ar.cause());
                        promise.fail(ar.cause());
                    }
                });
    }

}
