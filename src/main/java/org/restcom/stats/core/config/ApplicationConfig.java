/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
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
        final Set<Class<?>> returnValue = new HashSet<>();
        returnValue.add(CounterWS.class);
        returnValue.add(GaugeWS.class);
        returnValue.add(HistogramWS.class);
        returnValue.add(MeterWS.class);
        returnValue.add(TimerWS.class);
        return returnValue;
    }
}
