package org.lmrl.verticle;


import io.vertx.core.Vertx;

public class VerticleDemo {

    public static void main(String[] args) {
        Vertx v = Vertx.vertx();
        v.deployVerticle(new BlockingExecutingVerticle());
        v.deployVerticle(new HttpServerVerticle());
    }
}
