package io.vertx.examples.spring.verticle;


import io.vertx.core.AbstractVerticle;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;

public class OrderVerticle extends AbstractVerticle {

    Instant timestamp = Instant.now();
    int duration;
    TemporalUnit temporalUnit;

    public OrderVerticle(){

    }

    public OrderVerticle(int duration,TemporalUnit temporalUnit){
        this.duration = duration;
        this.temporalUnit = temporalUnit;
    }


    @Override
    public void start() throws Exception {
        //Start consuming events
        vertx.eventBus().<String>consumer("news-feed").handler(msg -> {
            Instant now = Instant.now();
            Instant diff = now.minus(this.duration, temporalUnit);
            if (diff.isAfter(timestamp) ){
                System.out.println(String.format("stopping verticle %s", diff.toEpochMilli()));
                vertx.undeploy(this.deploymentID());
            }
        });
        super.start();
    }
}
