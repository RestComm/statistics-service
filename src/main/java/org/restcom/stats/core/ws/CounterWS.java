package org.restcom.stats.core.ws;

import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.restcom.stats.core.persistence.PersistenceUtil;

/**
 * @author Ricardo Limonta
 */
@Path(value = "/counter")
@Named
public class CounterWS {
    
    @Inject
    private PersistenceUtil pm;
    
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response insertCounter(Map<String, Object> counter) {
        pm.insert(counter, "counter");
        return Response.status(Response.Status.OK).build();
    }
}