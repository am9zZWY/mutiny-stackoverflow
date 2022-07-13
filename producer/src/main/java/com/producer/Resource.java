package com.producer;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/")
@ApplicationScoped
public class Resource {

    private static final Logger LOGGER = Logger.getLogger("Producer-Resource");

    WebClient client;

    public Resource() {
        Vertx vertx = Vertx.vertx();
        this.client = WebClient.create(vertx);
    }

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Starting the Application ...");
    }

    @GET
    @Produces(TEXT_PLAIN)
    @Path("/send/{n}")
    public String getTimesStream(int n) {
        for (int i = 0; i < n; i++) {
            LOGGER.info("Sending time " + i  + " to consumer ...");
            this.client.postAbs("http://consumer:8081/add").sendJson(randHashMap()).subscribe().with(response -> LOGGER.info("Received " + response.statusCode() + " from consumer ..."));
        }
        return "Sent " + n + " times to consumer ...";
    }

    private HashMap<String, Long[]> randHashMap() {
        HashMap<String, Long[]> hashMap = new HashMap<>();
        hashMap.put("foo", new Long[]{System.nanoTime(), System.nanoTime() + 200});
        hashMap.put("bar", new Long[]{System.nanoTime(), System.nanoTime() + 250});
        hashMap.put("foobar", new Long[]{System.nanoTime(), System.nanoTime() + 100});
        return hashMap;
    }
}
