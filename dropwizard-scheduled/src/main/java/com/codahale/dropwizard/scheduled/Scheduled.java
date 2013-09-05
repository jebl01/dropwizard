package com.codahale.dropwizard.scheduled;

import it.sauronsoftware.cron4j.Task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduled
{
    /**
     * The cron scedule at which the annotated method should be invoked
     *<pre> 
     *  .-------------------- minute (0 - 59)
     *  |   .---------------- hour (0 - 23)
     *  |   |   .------------ day of month (1 - 31)
     *  |   |   |   .-------- month (1 - 12) OR jan,feb,mar,apr ...
     *  |   |   |   |  .----- day of week (0 - 7) (Sunday=0 or 7)  OR sun,mon,tue,wed,thu,fri,sat
     *  |   |   |   |  |
     *  *   *   *   *  *
     *  *   *   *   *  *     -> Every minute
     *  0   0   1   *  *     -> Run once a month, midnight, first of month
     *  0   1   *   *  sun   -> Every Sunday at 1:00AM
     * </pre>
     * @return the cron scedule as a string e.g. "* * * * *"
     */
    String cronSchedule();
    
    /**
     * The name of the scheduled task. If not set, the target class and method name will be used 
     */
    String name();
    /**
     * @see Task#canBePaused()
     */
    boolean canBePaused() default false;
    
    /**
     * @see Task#canBeStopped()
     */
    boolean canBeStopped() default false;
    
    /**
     * @see Task#supportsStatusTracking()
     */
    boolean supportsStatusTracking() default false;
    /**
     * @see Task#supportsCompletenessTracking()
     */
    boolean supportsCompletenessTracking() default false;
}
