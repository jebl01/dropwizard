package com.codahale.dropwizard.scheduled;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamedTask extends Task {
  private static final Logger LOGGER = LoggerFactory.getLogger(NamedTask.class);

  private final Object target;
  private final Method method;
  public final String name;

  NamedTask(Object target, Method method, String name) {
    this.target = target;
    this.method = method;
    this.name = name;
  }

  @Override
  public void execute(TaskExecutionContext context) throws RuntimeException {
    try {
      LOGGER.debug("executing task: " + name);
      method.invoke(target, context);
      LOGGER.debug("finished executing task: " + name);
    }
    catch (Throwable t) {
      LOGGER.warn("failed to invoking scheduled method: " + name, t);
      throw new RuntimeException(t);
    }
  }
}
