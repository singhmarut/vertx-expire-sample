package io.vertx.examples.spring.verticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.examples.spring.service.ProductService;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;


/**
 * Simple verticle to wrap a Spring service bean - note we wrap the service call
 * in executeBlocking, because we know it's going to be a JDBC call which blocks.
 * As a general principle with Spring beans, the default assumption should be that it will block unless you
 * know for sure otherwise (in other words use executeBlocking unless you know for sure your service call will be
 * extremely quick to respond)
 */
@Service
public class SpringDemoVerticle extends AbstractVerticle {

    public static final String ALL_PRODUCTS_ADDRESS = "example.all.products";

    private final ObjectMapper mapper = new ObjectMapper();

    private List<OrderVerticle> orderVerticleList = new ArrayList<>();

    public SpringDemoVerticle() {

    }

    @Override
    public void start() throws Exception {
        super.start();
        int count = 0;

        while (count < 100){
            OrderVerticle orderVerticle = new OrderVerticle(5, ChronoUnit.SECONDS);
            orderVerticleList.add(orderVerticle);
            vertx.deployVerticle(orderVerticle);
            count++;
        }
    }
}
