package ch.heg.task.services;

import ch.heg.task.entities.Person;
import ch.heg.task.entities.Statut;
import ch.heg.task.entities.Task;
import ch.heg.task.exceptions.TaskNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class TasksServices {

    private static final Logger LOG = LoggerFactory.getLogger(TasksServices.class);

    @PersistenceContext
    EntityManager entityManager;

    /**
     * add new Task
     * @param task
     * @return
     */
    @Transactional
    public Task addTask(final Task task) {
        entityManager.persist(task);
        return task;
    }

    /**
     * find task by id
     * @param id
     * @return
     */
    public Task findById(final Long id) {
        return entityManager.find(Task.class, id);
    }

    /**
     * find task by id
     * @param id
     * @return
     */
    public Task findByIdWithException(final Long id) throws TaskNotFoundException {
        Task task = entityManager.find(Task.class, id);
        if (task == null) {
            throw new TaskNotFoundException("Task with id " + id + " not found");
        } else {
            return task;
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public Task findByIdWithRuntimeException(final Long id) {
        Task task = entityManager.find(Task.class, id);
        if (task == null) {
            throw new NoSuchElementException("Task with id " + id + " not found");
        } else {
            return task;
        }
    }

    /**
     *
     * @return
     */
    public List<Task> findAllTasks() {
        TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t order by t.dateCreation desc", Task.class);
        return query.getResultList();
    }

    /**
     * find task by statut
     * @param statut
     * @return
     */
    public List<Task> findByStatus(final Statut statut) {
        TypedQuery<Task> namedQuery = entityManager.createQuery("SELECT t FROM Task t where t.statut = :statut", Task.class);
        namedQuery.setParameter("statut", statut);
        return namedQuery.getResultList();
    }

    /**
     * Update task
     * @param task
     * @return
     */
    @Transactional
    public Task updateTask(final Task task) {
        entityManager.merge(task);
        return task;
    }

    /**
     * add multiple task
     * @param tasks
     */
    @Transactional
    public void addTasks(final List<Task> tasks) {
        try {
            for (Task task: tasks) {
                entityManager.persist(task);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void deleteTask(final Long id) {
        Task task = entityManager.find(Task.class, id);
        entityManager.remove(task);
    }

    /**
     * add multiple task
     * @param lists
     */
    public List<String> addTasksBestEffort(final List<List<String>> lists) {
        List<String> errorList = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();

        for (List<String> line : lists) {
            if (line.size() == 5) {
                try {
                    tasks.add(buildTaskFromLine(line));
                } catch (Exception e) {
                    LOG.error("Unable to create task from line " + line);
                    errorList.add(line.toString());
                }
            } else {
                LOG.error("Unable to create task from line " + line);
                errorList.add(line.toString());
            }
        }

        EntityTransaction transaction = entityManager.getTransaction();
        for (Task task: tasks) {
            try {
                transaction.begin();
                entityManager.persist(task);
                transaction.commit();
            } catch (Exception e) {
                LOG.error(e.getMessage());
                transaction.rollback();
                errorList.add(task.toString());
            }
        }
        return errorList;
    }


    /**
     * helper method to crete task object
     * @param line
     * @return
     * @throws Exception
     */
    private Task buildTaskFromLine(final List<String> line) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        Task newTask = new Task();
        newTask.setDescription(line.get(0));
        String creationDate = line.get(1);
        if (creationDate != null && !creationDate.isEmpty()) {
            newTask.setDateCreation(sdf.parse(creationDate));
        }
        String executionDate = line.get(2);
        if (executionDate != null && !executionDate.isEmpty()) {
            newTask.setDateExecution(sdf.parse(executionDate));
        }
        newTask.setStatut(Statut.valueOf(line.get(3)));
        newTask.setEmailResponsable(line.get(4));
        return newTask;
    }

    /**
     * add new person
     * @param person
     * @return
     */
    @Transactional
    public Person addPerson(final Person person) {
        entityManager.persist(person);
        return person;
    }

    /**
     *
     * @param personId
     * @return
     */
    public Person findPersonById(final Long personId) {
        return entityManager.find(Person.class, personId);
    }
}
