package io.phdata.pulse.log;/* Copyright 2018 phData Inc. */

import org.apache.log4j.spi.LoggingEvent;
import org.junit.Assert;
import org.junit.Test;

public class BufferingEventHandlerTest {

  @Test
  public void BatchMultipleEventsDontFlushWithLowNumber() {
    Integer flushInterval = 1000;
    Integer bufferSize = 10;
    BufferingEventHandler handler = new BufferingEventHandler();
    handler.setFlushIntervalMillis(flushInterval);
    handler.setBufferSize(bufferSize);

    // create fewer events than the buffersize
    int eventsCreated = bufferSize - 1;

    LoggingEvent[] events = TestUtils.generateEvents(eventsCreated);
    for (LoggingEvent event : events) {
      handler.addEvent(event);
    }

    // Should be within time and size constraints for the queue, there are no events produced
    Assert.assertEquals(false, handler.shouldFlush());

  }

  @Test
  public void batchMultipleEventsFlushWhenOverSizeThreshold() {
    Integer flushInterval = 1000;
    Integer batchSize = 10;
    BufferingEventHandler handler = new BufferingEventHandler();
    handler.setFlushIntervalMillis(flushInterval);
    handler.setBufferSize(batchSize);

    // create more events than the buffersize
    int eventsCreated = batchSize + 1;

    LoggingEvent[] events = TestUtils.generateEvents(eventsCreated);
    for (LoggingEvent event : events) {
      handler.addEvent(event);
    }

    // Size threshold is exceeded, produce all events
    Assert.assertEquals(true, handler.shouldFlush());
  }

  @Test
  public void flushAfterFlushInterval() throws Exception {
    Integer flushInterval = 1000;
    Integer bufferSize = 10;
    BufferingEventHandler handler = new BufferingEventHandler();
    handler.setFlushIntervalMillis(flushInterval);
    handler.setBufferSize(bufferSize);

    // create fewer events than the buffersize
    int eventsCreated = bufferSize - 1;

    LoggingEvent[] events = TestUtils.generateEvents(eventsCreated);
    for (LoggingEvent event : events) {
      handler.addEvent(event);
    }

    // sleep past the flush interval
    Thread.sleep((long) (flushInterval + 200));

    // time threshold is exceeded, produce all events
    Assert.assertEquals(true, handler.shouldFlush());
  }

  @Test
  public void getMessages() throws Exception {
    Integer flushInterval = 1000;
    Integer bufferSize = 10;
    BufferingEventHandler handler = new BufferingEventHandler();
    handler.setFlushIntervalMillis(flushInterval);
    handler.setBufferSize(bufferSize);

    // create fewer events than the buffersize
    int eventsCreated = 100;

    LoggingEvent[] events = TestUtils.generateEvents(eventsCreated);
    for (LoggingEvent event : events) {
      handler.addEvent(event);
    }

    // sleep past the flush interval
    // time threshold is exceeded, produce all events
    Assert.assertEquals(100, handler.getMessages().length);
  }
}
