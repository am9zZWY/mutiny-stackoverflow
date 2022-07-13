package com.consumer;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
@ApplicationScoped
public class Resource {

    private static final Logger LOGGER = Logger.getLogger("Consumer-Resource");

    @Inject
    Consumer consumer;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("Starting the Application ...");
    }

    @GET
    @Path("/stream")
    @Produces(APPLICATION_JSON)
    public Multi<HashMap<String, Long[]>> getStream() {
        LOGGER.info("Getting stream ...");
        return this.consumer.getStream();
    }

    @GET
    @Path("/avg")
    @Produces(APPLICATION_JSON)
    public Uni<List<HashMap<String, Long[]>>> getAvg() {
        LOGGER.info("Getting compact stream ...");
        return this.consumer.getAvg();
    }

    @POST
    @Path("/add")
    public void add(HashMap<String, Long[]> time) {
        LOGGER.debug("Receiving time");
        consumer.addEvent(time);
    }
}
