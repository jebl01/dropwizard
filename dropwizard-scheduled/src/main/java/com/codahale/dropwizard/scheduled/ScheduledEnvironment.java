package com.codahale.dropwizard.scheduled;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.dropwizard.lifecycle.Managed;
import com.codahale.dropwizard.lifecycle.setup.LifecycleEnvironment;
import com.google.common.base.Strings;

public class ScheduledEnvironment implements Managed {
  private static final Logger LOGGER = LoggerFactory.getLogger(LifecycleEnvironment.class);
  private final Scheduler scheduler;
  private final ScheduledTaskCollector taskCollector = new ScheduledTaskCollector();

  public ScheduledEnvironment(Scheduler scheduler) {
    this.scheduler = scheduler;
    this.scheduler.addTaskCollector(taskCollector);
  }

  public void schedule(final Object object) {
    for (final Method method : object.getClass().getMethods()) {
      if (method.isAnnotationPresent(Scheduled.class) && method.getReturnType().equals(Void.TYPE) && method.getParameterTypes().length == 1
          && method.getParameterTypes()[0].isAssignableFrom(TaskExecutionContext.class)) {

        final Scheduled annotation = method.getAnnotation(Scheduled.class);

        final String scheduledMethod = object.getClass().getSimpleName() + ":" + method.getName();
        final String pattern = annotation.cronSchedule();
        final String name = Strings.isNullOrEmpty(annotation.name()) ? scheduledMethod : annotation.name();
        
        this.taskCollector.addTask(pattern, new NamedTask(object, method, name));

        LOGGER.info("scheduled method for invocation: " + scheduledMethod + " pattern:" + pattern);
      }
      else {
        throw new RuntimeException("Invalid method signature! Scheduled methods should return void and take a 'TaskExecutionContext' parameter");
      }
    }
  }

  @Override
  public void start() throws Exception {
    LOGGER.debug("starting task scheduler...");
    this.scheduler.start();
  }

  @Override
  public void stop() throws Exception {
    LOGGER.debug("stopping task scheduler...");
    this.scheduler.stop();
  }
}
