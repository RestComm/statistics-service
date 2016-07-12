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
package org.restcom.stats.web.controller;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.restcom.stats.core.dto.MetricEventDTO;
import org.restcom.stats.core.service.MetricEventService;
import org.restcom.stats.core.type.MetricType;

/**
 *
 * @author Ricardo Limonta
 */
@Named
@ViewScoped
public class DashboardController implements Serializable {
    
    @Inject
    private MetricEventService metricService;
    
    private MetricEventDTO counterStatus;
    private MetricEventDTO histogramStatus;
    private MetricEventDTO meterStatus;
    private MetricEventDTO timerStatus;

    private MetricType metricType = MetricType.COUNTER;
    private List<String> metricKeys;
    private String keySelected;
    private List<MetricEventDTO> metrics;
    
    private final LineChartModel metricsChart;
    private final DateFormat fmt;
    
    public DashboardController() {
        super();
        
        //init chart
        this.metricsChart = new LineChartModel();
        this.fmt = new SimpleDateFormat("MM-dd HH:mm");
    }
    
    @PostConstruct
    public void init() {
        //retrieve metric keys
        this.retrieveMetricKeys();
        
        //load statistics
        this.retrieveStatistics();
    }

    public void retrieveStatistics() {
        
        //retrieve the last counter status
        this.counterStatus = metricService.restrieveStatus(MetricType.COUNTER);
        
        //retrieve the last histogram status
        this.histogramStatus = metricService.restrieveStatus(MetricType.HISTOGRAM);
        
        //retrieve the last meter status
        this.meterStatus = metricService.restrieveStatus(MetricType.METER);
        
        //retrieve the last timer status
        this.timerStatus = metricService.restrieveStatus(MetricType.TIMER);

        //retrieve chart statistics
        this.retrieveChartStatistics();
    }
    
    public void retrieveChartStatistics() {
        
        //retrieve keys metrics of selected metric type
        this.metricKeys = metricService.retrieveMetricKeys(metricType);
        
        //retrieve metrics
        this.metrics = metricService.retrieveMetrics(this.metricType, this.keySelected);
        
        //update chart
        this.updateChart();
    }
    
    public void changeMetricType() {
        //reload metric keys
        this.retrieveMetricKeys();
        
        //reload chart
        this.retrieveChartStatistics();
    }
    
    private void retrieveMetricKeys() {
        //retrieve keys metrics of selected metric type
        this.metricKeys = metricService.retrieveMetricKeys(metricType);
        
        //set default metrici key
        if (!this.metricKeys.isEmpty()) {
           this.keySelected = this.metricKeys.get(0);
        }
    }
    
    private void updateChart() {
        
        //reset chart
        this.setupChart();
        
        LineChartSeries keyMetricSerie = new LineChartSeries();
        
        //create default serie
        keyMetricSerie.setLabel(keySelected);
        keyMetricSerie.setFill(true);
        
        //add default value for empty result
        if (metrics.isEmpty()) {
            keyMetricSerie.set("01-01 00:00", 0);
        } else {
            for (MetricEventDTO item : metrics) {
                keyMetricSerie.set(fmt.format(item.getTimestamp()), item.getTotalEvents());
            }
        }
        
        //ajuste a formatacao do label do grafico de acordo com o tipo de periodo
        metricsChart.getAxis(AxisType.X).setTickFormat("%m-%d %H:%M");

        //adiciona as series no grafico
        metricsChart.addSeries(keyMetricSerie);
    }

    private void setupChart() {
        //clear data
        metricsChart.clear();
        
        //define chart title
        metricsChart.setTitle(metricType.toString());
        
        //define legend position
        metricsChart.setLegendPlacement(LegendPlacement.INSIDE);
        metricsChart.setLegendPosition("ne");
        metricsChart.setSeriesColors("F08080");
        
        //Define min value for Axis Y
        metricsChart.getAxis(AxisType.Y).setMin(0);
        
        //create X Axit like Date
        DateAxis axis = new DateAxis();
        axis.setTickAngle(-50);
        axis.setMin("07-07 16:00");
        metricsChart.getAxes().put(AxisType.X, axis);
    }
    
    public MetricEventDTO getCounterStatus() {
        return counterStatus;
    }

    public void setCounterStatus(MetricEventDTO counterStatus) {
        this.counterStatus = counterStatus;
    }

    public MetricEventService getCounterService() {
        return metricService;
    }

    public void setCounterService(MetricEventService counterService) {
        this.metricService = counterService;
    }

    public MetricEventDTO getHistogramStatus() {
        return histogramStatus;
    }

    public void setHistogramStatus(MetricEventDTO histogramStatus) {
        this.histogramStatus = histogramStatus;
    }

    public MetricEventDTO getMeterStatus() {
        return meterStatus;
    }

    public void setMeterStatus(MetricEventDTO meterStatus) {
        this.meterStatus = meterStatus;
    }

    public MetricEventDTO getTimerStatus() {
        return timerStatus;
    }

    public void setTimerStatus(MetricEventDTO timerStatus) {
        this.timerStatus = timerStatus;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public List<String> getMetricKeys() {
        return metricKeys;
    }

    public void setMetricKeys(List<String> metricKeys) {
        this.metricKeys = metricKeys;
    }

    public String getKeySelected() {        
        return keySelected;
    }

    public void setKeySelected(String keySelected) {
        this.keySelected = keySelected;
    }

    public LineChartModel getMetricsChart() {
        return metricsChart;
    }
}