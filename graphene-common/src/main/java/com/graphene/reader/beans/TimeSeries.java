package com.graphene.reader.beans;

import com.graphene.common.beans.SeriesRange;

import java.util.*;

/**
 * @author Andrei Ivanov
 */
public class TimeSeries {

  private String name;
  private String pathExpression;
  private Long from;
  private Long to;
  private int step;
  private Double[] values;
  private Map<TimeSeriesOption, Object> options = new HashMap<>();
  private Map<String, String> tags = new HashMap<>();

  public TimeSeries(String name, Long from, Long to, int step) {
    this(name, from, to, step, new Double[0]);
  }

  public TimeSeries(String name, Long from, Long to, int step, Double[] values) {
    this.name = name;
    this.from = from;
    this.to = to;
    this.step = step;
    this.values = values;
  }

  public TimeSeries(String name, String pathExpression, Map<String, String> tags, SeriesRange seriesRange) {
    this(name, pathExpression, tags, seriesRange, new Double[seriesRange.getExpectedCount()]);
  }

  public TimeSeries(String name, String pathExpression, Map<String, String> tags, SeriesRange seriesRange, Double[] values) {
    this.name = name;
    this.pathExpression = pathExpression;
    this.tags = tags;
    this.from = seriesRange.getFrom();
    this.to = seriesRange.getTo();
    this.step = seriesRange.getRollup();
    this.values = values;
  }

  public void addValues(Double[] valuesToAdd, Long startTime) {
    int index = 0;
    if (startTime <= from) {
      while (index < valuesToAdd.length) {
        this.values[index] = valuesToAdd[index];
        index++;
      }
    } else {
      int valueIndex = (int) ((startTime - this.from) / this.step);
      while (index < valuesToAdd.length) {
        this.values[valueIndex] = valuesToAdd[index];
        index++;
        valueIndex++;
      }
    }
  }

  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  public String getPathExpression() {
    return pathExpression;
  }

  public void setPathExpression(String pathExpression) {
    this.pathExpression = pathExpression;
  }

  public Long getFrom() {
      return from;
  }

  public void setFrom(Long from) {
      this.from = from;
  }

  public Long getTo() {
      return to;
  }

  public void setTo(Long to) {
      this.to = to;
  }

  public Double[] getValues() {
      return values;
  }

  public void setValues(Double[] values) {
      this.values = values;
  }

  public int getStep() {
      return step;
  }

  public void setStep(int step) {
      this.step = step;
  }

  public void addOption(TimeSeriesOption option) {
      options.put(option, true);
  }

  public void setOption(TimeSeriesOption option, Object value) {
      options.put(option, value);
  }

  public Object getOption(TimeSeriesOption option) {
      return options.get(option);
  }

  public boolean hasOption(TimeSeriesOption option) {
      return options.containsKey(option);
  }

  public Map<String, String> getTags() {
    return tags;
  }

  public void setTags(Map<String, String> tags) {
    this.tags = tags;
  }

  @Override
  public String toString() {
      return "TimeSeries{" +
              "name='" + name + '\'' +
              ", from=" + from +
              ", to=" + to +
              ", step=" + step +
              '}';
  }
}
