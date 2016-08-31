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
import java.util.Calendar;
import java.util.Date;
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
import org.restcom.stats.core.dto.CounterDTO;
import org.restcom.stats.core.dto.HistogramDTO;
import org.restcom.stats.core.dto.MeterDTO;
import org.restcom.stats.core.dto.MetricStatusDTO;
import org.restcom.stats.core.dto.TimerDTO;
import org.restcom.stats.core.service.CounterService;
import org.restcom.stats.core.service.HistogramService;
import org.restcom.stats.core.service.MeterService;
import org.restcom.stats.core.service.MetricEventService;
import org.restcom.stats.core.service.TimerService;
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
    
    @Inject
    private CounterService counterService;
    
    @Inject
    private HistogramService histogramService;
     
    @Inject
    private MeterService meterService;
    
    @Inject
    private TimerService timerService;
    
    private MetricStatusDTO counterStatus, histogramStatus, meterStatus, timerStatus;
    private List<CounterDTO> counterMetrics;
    private List<HistogramDTO> histogramMetrics;
    private List<MeterDTO> meterMetrics;
    private List<TimerDTO> timerMetrics;
    private List<String> counterKeys, histogramKeys, meterKeys, timerKeys;
    private String counterKey, histogramKey, meterKey, timerKey;
    private final LineChartModel counterChart, histogramChart, meterChart, timerChart;
    private final DateFormat fmt;
    private final Calendar cal;
    private Date fromDate, toDate;
    
    public DashboardController() {
        super();
        
        //retrieve calendar instance
        this.cal = Calendar.getInstance();
        
        //init counter chart
        this.counterChart = new LineChartModel();
        this.counterChart.setLegendPlacement(LegendPlacement.INSIDE);
        this.counterChart.setLegendPosition("ne");
        this.counterChart.setSeriesColors("F08080");
        
        //init histogram chart
        this.histogramChart = new LineChartModel();
        this.histogramChart.setLegendPlacement(LegendPlacement.INSIDE);
        this.histogramChart.setLegendPosition("ne");
        this.histogramChart.setSeriesColors("2288AA");
        
        //init meter chart
        this.meterChart = new LineChartModel();
        this.meterChart.setLegendPlacement(LegendPlacement.INSIDE);
        this.meterChart.setLegendPosition("ne");
        this.meterChart.setSeriesColors("58BA27");
        
        //init timer chart
        this.timerChart = new LineChartModel();
        this.timerChart.setLegendPlacement(LegendPlacement.INSIDE);
        this.timerChart.setLegendPosition("ne");
        this.timerChart.setSeriesColors("A30303");
        
        //define date mask
        this.fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    public void load() {
        //set fromDate to 0 hour of day
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.fromDate = cal.getTime();

        //set toDate to end hour of day
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        this.toDate = cal.getTime();

        //retrieve metric keys
        this.retrieveMetricKeys();
        
        //retrieve all statistics
        this.retrieveAllStatistics();
    }

    public void retrieveAllStatistics() {
        
        //retrieve the last counter status
        this.counterStatus = metricService.restrieveStatus(fromDate.getTime(), toDate.getTime(), MetricType.COUNTER);
        
        //retrieve the last histogram status
        this.histogramStatus = metricService.restrieveStatus(fromDate.getTime(), toDate.getTime(), MetricType.HISTOGRAM);
        
        //retrieve the last meter status
        this.meterStatus = metricService.restrieveStatus(fromDate.getTime(), toDate.getTime(), MetricType.METER);
        
        //retrieve the last timer status
        this.timerStatus = metricService.restrieveStatus(fromDate.getTime(), toDate.getTime(), MetricType.TIMER);

        //retrieve counter statistics
        this.retrieveCounterStatistics();
        
        //retrieve histogram statistics
        this.retrieveHistogramStatistics();
        
        //retrieve meter statistics
        this.retrieveMeterStatistics();
        
        //retrieve timer statistics
        this.retrieveTimerStatistics();
    }
    
    /**
     * Retrieve Counter Statistics.
     */
    public void retrieveCounterStatistics() {        
        //retrieve metrics
        this.counterMetrics = counterService.retrieveMetrics(fromDate.getTime(), toDate.getTime(), this.counterKey);
        
        //update chart
        this.updateCouterChart();
    }
    
    /**
     * Retrieve Histogram Statistics.
     */
    public void retrieveHistogramStatistics() {        
        //retrieve metrics
        this.histogramMetrics = histogramService.retrieveMetrics(fromDate.getTime(), toDate.getTime(), this.histogramKey);
        
        //update chart
        this.updateHistogramChart();
    }
    
    /**
     * Retrieve Meter Statistics.
     */
    public void retrieveMeterStatistics() {        
        //retrieve metrics
        this.meterMetrics = meterService.retrieveMetrics(fromDate.getTime(), toDate.getTime(), this.meterKey);
        
        //update chart
        this.updateMeterChart();
    }
    
    /**
     * Retrieve Timer Statistics.
     */
    public void retrieveTimerStatistics() {        
        //retrieve metrics
        this.timerMetrics = timerService.retrieveMetrics(fromDate.getTime(), toDate.getTime(), this.timerKey);
        
        //update chart
        this.updateTimerChart();
    }
    
    /**
     * Clear and Update Counter Chart.
     */
    private void updateCouterChart() {
        
        //clear data
        counterChart.clear();
        
        //set Y Axis min
        counterChart.getAxis(AxisType.Y).setMin(0);
        
        //create X Axit like Date
        DateAxis axis = new DateAxis();
        axis.setTickAngle(-50);
        axis.setMin(fmt.format(this.fromDate));
        counterChart.getAxes().put(AxisType.X, axis);
        
        LineChartSeries counterSerie = new LineChartSeries();
        
        //create default serie
        counterSerie.setLabel(this.counterKey);
        counterSerie.setFill(true);
        
        //add default value for empty result
        if (counterMetrics.isEmpty()) {
            counterSerie.set(fmt.format(fromDate), 0);
        } else {
            for (CounterDTO counter : counterMetrics) {
                counterSerie.set(fmt.format(new Date(counter.getTimestamp())), counter.getCount());
            }
        }
        
        //format X Axis label
        counterChart.getAxis(AxisType.X).setTickFormat("%m-%d %H:%M");

        //add default serie
        counterChart.addSeries(counterSerie);
    }
    
    /**
     * Clear and Update Histogram Chart.
     */
    private void updateHistogramChart() {
        
        //clear data
        histogramChart.clear();
        
        //set Y Axis min
        histogramChart.getAxis(AxisType.Y).setMin(0);
        
        //create X Axit like Date
        DateAxis histogramDateAxis = new DateAxis();
        histogramDateAxis.setTickAngle(-50);
        histogramDateAxis.setMin(fmt.format(this.fromDate));
        histogramChart.getAxes().put(AxisType.X, histogramDateAxis);
        
        LineChartSeries histogramSerie = new LineChartSeries();
        
        //create default serie
        histogramSerie.setLabel(this.histogramKey);
        
        histogramSerie.setFill(true);
        
        //add default value for empty result
        if (histogramMetrics.isEmpty()) {
            histogramSerie.set(fmt.format(fromDate), 0);
        } else {
            for (HistogramDTO hist : histogramMetrics) {
                histogramSerie.set(fmt.format(new Date(hist.getTimestamp())), hist.getCount());
            }
        }
        
        //format X Axis label
        histogramChart.getAxis(AxisType.X).setTickFormat("%m-%d %H:%M");

        //add default serie
        histogramChart.addSeries(histogramSerie);
    }
    
    /**
     * Clear and Update Meter Chart.
     */
    private void updateMeterChart() {
        
        //clear data
        meterChart.clear();
        
        //set Y Axis min
        meterChart.getAxis(AxisType.Y).setMin(0);
        
        //create X Axit like Date
        DateAxis meterDateAxis = new DateAxis();
        meterDateAxis.setTickAngle(-50);
        meterDateAxis.setMin(fmt.format(this.fromDate));
        meterChart.getAxes().put(AxisType.X, meterDateAxis);
        
        LineChartSeries meterSerie = new LineChartSeries();
        
        //create default serie
        meterSerie.setLabel(this.meterKey);
        
        meterSerie.setFill(true);
        
        //add default value for empty result
        if (meterMetrics.isEmpty()) {
            meterSerie.set(fmt.format(fromDate), 0);
        } else {
            for (MeterDTO meter : meterMetrics) {
                meterSerie.set(fmt.format(new Date(meter.getTimestamp())), meter.getCount());
            }
        }
        
        //format X Axis label
        meterChart.getAxis(AxisType.X).setTickFormat("%m-%d %H:%M");

        //add default serie
        meterChart.addSeries(meterSerie);
    }

    /**
     * Clear and Update Timer Chart.
     */
    private void updateTimerChart() {
        
        //clear data
        timerChart.clear();
        
        //set Y Axis min
        timerChart.getAxis(AxisType.Y).setMin(0);
        
        //create X Axit like Date
        DateAxis timerDateAxis = new DateAxis();
        timerDateAxis.setTickAngle(-50);
        timerDateAxis.setMin(fmt.format(this.fromDate));
        timerChart.getAxes().put(AxisType.X, timerDateAxis);
        
        LineChartSeries timerSerie = new LineChartSeries();
        
        //create default serie
        timerSerie.setLabel(this.timerKey);
        timerSerie.setFill(true);
        
        //add default value for empty result
        if (timerMetrics.isEmpty()) {
            timerSerie.set(fmt.format(fromDate), 0);
        } else {
            for (TimerDTO timer : timerMetrics) {
                timerSerie.set(fmt.format(new Date(timer.getTimestamp())), timer.getCount());
            }
        }
        
        //format X Axis label
        timerChart.getAxis(AxisType.X).setTickFormat("%m-%d %H:%M");

        //add default serie
        timerChart.addSeries(timerSerie);
    }
    
    /**
     * Load Metric Keys.
     */
    private void retrieveMetricKeys() {
        //retrieve counter keys
        this.counterKeys = metricService.retrieveMetricKeys(MetricType.COUNTER);
        //set default counter key
        if (!this.counterKeys.isEmpty()) this.counterKey = this.counterKeys.get(0);

        //retrieve histogram keys
        this.histogramKeys = metricService.retrieveMetricKeys(MetricType.HISTOGRAM);
        //set default histogram key
        if (!this.histogramKeys.isEmpty()) this.histogramKey = this.histogramKeys.get(0);

        //retrieve meter keys
        this.meterKeys = metricService.retrieveMetricKeys(MetricType.METER);
        //set default meter key
        if (!this.meterKeys.isEmpty()) this.meterKey = this.meterKeys.get(0);

        //retrieve timer keys
        this.timerKeys = metricService.retrieveMetricKeys(MetricType.TIMER);
        //set default timer key
        if (!this.timerKeys.isEmpty()) this.timerKey = this.timerKeys.get(0);
    }
    
    public MetricStatusDTO getCounterStatus() {
        return counterStatus;
    }

    public MetricStatusDTO getHistogramStatus() {
        return histogramStatus;
    }

    public MetricStatusDTO getMeterStatus() {
        return meterStatus;
    }

    public MetricStatusDTO getTimerStatus() {
        return timerStatus;
    }

    public String getCounterKey() {
        return counterKey;
    }

    public void setCounterKey(String counterKey) {
        this.counterKey = counterKey;
    }

    public String getHistogramKey() {
        return histogramKey;
    }

    public void setHistogramKey(String histogramKey) {
        this.histogramKey = histogramKey;
    }

    public String getMeterKey() {
        return meterKey;
    }

    public void setMeterKey(String meterKey) {
        this.meterKey = meterKey;
    }

    public String getTimerKey() {
        return timerKey;
    }

    public void setTimerKey(String timerKey) {
        this.timerKey = timerKey;
    }

    public List<String> getCounterKeys() {
        return counterKeys;
    }

    public List<String> getHistogramKeys() {
        return histogramKeys;
    }

    public List<String> getMeterKeys() {
        return meterKeys;
    }

    public List<String> getTimerKeys() {
        return timerKeys;
    }

    public LineChartModel getCounterChart() {
        return counterChart;
    }

    public LineChartModel getHistogramChart() {
        return histogramChart;
    }

    public LineChartModel getMeterChart() {
        return meterChart;
    }

    public LineChartModel getTimerChart() {
        return timerChart;
    }
    
    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}