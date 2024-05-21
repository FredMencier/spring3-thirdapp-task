package ch.heg.task;

import ch.heg.task.entities.*;
import ch.heg.task.exceptions.TaskNotFoundException;
import ch.heg.task.services.TasksServices;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TachesServicesTest {

    @Autowired
    TasksServices tachesServices;

    @Test
    public void addTask() {
        Task newTask = populateTask("My newTask", Statut.FAILED, "t.durand@heg.com");
        Task task = tachesServices.addTask(newTask);

        Assertions.assertNotNull(task);
        Assertions.assertNotNull(task.getId());
    }

    @Test
    public void findTaskById() {
        Task task = tachesServices.findById(100L);

        Assertions.assertNotNull(task);
        Assertions.assertNotNull(task.getId());
        Assertions.assertEquals(Long.valueOf(100), task.getId());
        Assertions.assertEquals("a.einstein@heg.com", task.getEmailResponsable());
    }

    @Test
    public void findTaskByStatus() {
        List<Task> tasks = tachesServices.findByStatus(Statut.TODO);

        Assertions.assertNotNull(tasks);
        Assertions.assertFalse(tasks.isEmpty());
        Assertions.assertEquals(1, tasks.size());
        Task task = tasks.get(0);
        Assertions.assertEquals(Long.valueOf(100), task.getId());
        Assertions.assertEquals("a.einstein@heg.com", task.getEmailResponsable());
    }

    @Test
    public void updateTask() {
        Task task = tachesServices.findById(100L);
        task.setDateExecution(new Date());
        task.setDescription("Task for Sending email");
        Task taskUpdated = tachesServices.updateTask(task);

        Assertions.assertNotNull(taskUpdated);
        Assertions.assertNotNull(taskUpdated.getId());
        Assertions.assertEquals(Long.valueOf(100), taskUpdated.getId());
        Assertions.assertEquals("Task for Sending email", taskUpdated.getDescription());
        Assertions.assertNotNull(taskUpdated.getDateExecution());
    }

    @Test
    public void addTasks() {
        Task task1 = populateTask("first task", Statut.CANCELED, "t.durand@heg.com");
        Task task2 = populateTask("second task", Statut.CANCELED, "t.durand@heg.com");
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tachesServices.addTasks(tasks);

        List<Task> taskList = tachesServices.findByStatus(Statut.CANCELED);
        Assertions.assertNotNull(taskList);
        Assertions.assertEquals(2, taskList.size());
    }

    @Test
    public void addTasksKO() {
        Task task1 = populateTask("first task", Statut.DONE, "t.durand@heg.com");
        Task task2 = populateTask("second task", Statut.DONE, "t.durand@heg.com");
        Task task3 = populateTask("very long description, very long description, very long description, very long description, very long description, very long description, very long description, very long description, very long description, very long description, very long description, very long description, very long description, very long description, very long description", Statut.DONE, "t.durand@heg.com");
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        tachesServices.addTasks(tasks);

        List<Task> taskList = tachesServices.findByStatus(Statut.DONE);
        Assertions.assertNotNull(taskList);
        Assertions.assertEquals(0, taskList.size());
    }

    /**
     * helper method to crete task object
     * @param description
     * @param statut
     * @param email
     * @return
     */
    private Task populateTask(final String description, final Statut statut, final String email) {
        Task newTask = new Task();
        newTask.setDescription(description);
        newTask.setDateCreation(new Date());
        newTask.setStatut(statut);
        newTask.setDateExecution(null);
        newTask.setEmailResponsable(email);
        return newTask;
    }

    @Test
    public void addPersonWithAddress() {
        Person person = getPersonWithAddress();

        Person personInserted = tachesServices.addPerson(person);

        Assertions.assertNotNull(personInserted);
        Assertions.assertNotNull(personInserted.getId());
        Assertions.assertNotNull(personInserted.getAddress());
        Assertions.assertNotNull(personInserted.getAddress().getId());
        Assertions.assertNotNull(personInserted.getAddress().getPerson());
        Assertions.assertNotNull(personInserted.getAddress().getPerson().getId());
    }

    @Test
    public void addPersonWithAddressWithException() {
        assertThrows(PersistenceException.class, () -> {
            Person person = getPersonWithAddress();
            person.setLastname(null);

            tachesServices.addPerson(person);
        });
    }

    @Test
    public void addPersonWithAddressAndTask() {
        Person person = getPersonWithAddress();
        Task task = populateTask("first task", Statut.DONE, "t.durand@heg.com");
        task.setPerson(person);
        person.getTaskList().add(task);

        Person personInserted = tachesServices.addPerson(person);

        Assertions.assertNotNull(personInserted);
        Assertions.assertNotNull(personInserted.getId());
        Assertions.assertNotNull(personInserted.getAddress());
        Assertions.assertNotNull(personInserted.getAddress().getId());
        Assertions.assertNotNull(personInserted.getAddress().getPerson());
        Assertions.assertNotNull(personInserted.getAddress().getPerson().getId());
        Assertions.assertEquals(1, personInserted.getTaskList().size());
        List<Task> list = new ArrayList<>(personInserted.getTaskList());
        Assertions.assertNotNull(list.get(0).getId());
    }

    @Test
    public void addPersonWithAddressAndTaskAndAudit() {
        Person person = getPersonWithAddress();
        Task task = populateTask("first task", Statut.DONE, "t.durand@heg.com");
        Audit audit = new Audit();
        audit.setDateEvent(new Date());
        audit.setEventDescription("Creation Task");
        task.setPerson(person);
        task.getAuditList().add(audit);
        person.getTaskList().add(task);

        Person personInserted = tachesServices.addPerson(person);

        Assertions.assertNotNull(personInserted);
        Assertions.assertNotNull(personInserted.getId());
        Assertions.assertNotNull(personInserted.getAddress());
        Assertions.assertNotNull(personInserted.getAddress().getId());
        Assertions.assertNotNull(personInserted.getAddress().getPerson());
        Assertions.assertNotNull(personInserted.getAddress().getPerson().getId());
        Assertions.assertEquals(1, personInserted.getTaskList().size());
        List<Task> taskList = new ArrayList<>(personInserted.getTaskList());
        Task firstTask = taskList.get(0);
        Assertions.assertNotNull(firstTask.getId());
        Assertions.assertEquals(1, firstTask.getAuditList().size());
        List<Audit> listAudit = new ArrayList<>(firstTask.getAuditList());
        Audit firstAudit = listAudit.get(0);
        Assertions.assertNotNull(firstAudit.getId());
    }

    @Test
    public void findPerson() {
        addPersonWithAddressAndTaskAndAudit();
        Person person = tachesServices.findPersonById(1L);
        Assertions.assertNotNull(person);
    }

    private Person getPersonWithAddress() {
        Person person = new Person();
        person.setName("Marcel");
        person.setLastname("Dupond");
        Address address = new Address();
        address.setCity("Geneva");
        address.setStreet("Chante poulet");
        person.setAddress(address);
        address.setPerson(person);
        return person;
    }
}