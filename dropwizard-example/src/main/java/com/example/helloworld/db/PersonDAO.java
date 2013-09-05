package com.example.helloworld.db;

import com.codahale.dropwizard.hibernate.AbstractDAO;
import com.codahale.dropwizard.scheduled.Scheduled;
import com.example.helloworld.core.Person;
import com.google.common.base.Optional;

import org.hibernate.SessionFactory;

import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.util.List;

public class PersonDAO extends AbstractDAO<Person> {
    public PersonDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Person> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public Person create(Person person) {
        return persist(person);
    }

    public List<Person> findAll() {
        return list(namedQuery("com.example.helloworld.core.Person.findAll"));
    }
    
    @Scheduled(cronSchedule= "* * * * *", name = "DoStuffTask")
    public void doStuffScheduled(TaskExecutionContext context){
      System.out.println("executing task!!");
    }
}
