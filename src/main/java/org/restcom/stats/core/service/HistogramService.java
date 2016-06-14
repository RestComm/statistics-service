package org.restcom.stats.core.service;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.restcom.stats.core.persistence.PersistenceUtil;

/**
 * @author Ricardo Limonta
 */
@Path(value = "/histogram")
@Named
public class HistogramService {

    @Inject
    private PersistenceUtil pm;
    
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response insertHistogram(Map<String, Object> histogram) {
        pm.insertAsync(histogram, "histogram");
        return Response.status(Response.Status.OK).build();
    }  
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> testHistogram() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("service", "histogram");
        return result;
    }
}
