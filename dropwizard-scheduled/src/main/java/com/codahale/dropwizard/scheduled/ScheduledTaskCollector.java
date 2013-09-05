package com.codahale.dropwizard.scheduled;

import it.sauronsoftware.cron4j.SchedulingPattern;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskCollector;
import it.sauronsoftware.cron4j.TaskTable;

public class ScheduledTaskCollector implements TaskCollector {
  
  private TaskTable taskTable = new TaskTable();
  
  public void addTask(String pattern, Task task){
    if (!SchedulingPattern.validate(pattern)) {
      throw new RuntimeException(String.format("invalid scheduling pattern: (%s)", pattern));
    }
    
    this.taskTable.add(new SchedulingPattern(pattern), task);
  }
  
  @Override
  public TaskTable getTasks() {
    return this.taskTable;
  }
}
