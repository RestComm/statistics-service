package org.restcom.stats.core.config;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.restcom.stats.core.ws.CounterWS;
import org.restcom.stats.core.ws.GaugeWS;
import org.restcom.stats.core.ws.HistogramWS;
import org.restcom.stats.core.ws.MeterWS;
import org.restcom.stats.core.ws.TimerWS;

/**
 *
 * @author Ricardo Limonta
 */
@ApplicationPath("/rest")
public class ApplicationConfig extends Application {
     @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> returnValue = new HashSet<Class<?>>();
        returnValue.add(CounterWS.class);
        returnValue.add(GaugeWS.class);
        returnValue.add(HistogramWS.class);
        returnValue.add(MeterWS.class);
        returnValue.add(TimerWS.class);
        return returnValue;
    }
}
