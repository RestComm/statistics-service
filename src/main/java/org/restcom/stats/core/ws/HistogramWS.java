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
@Path(value = "/histogram")
@Named
public class HistogramWS {

    @Inject
    private PersistenceUtil pm;
    
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public Response insertHistogram(Map<String, Object> histogram) {
        pm.insert(histogram, "histogram");
        return Response.status(Response.Status.OK).build();
    }  
}
