package ch.heg.task.controller;

import ch.heg.task.dto.TaskSummary;
import ch.heg.task.entities.Statut;
import ch.heg.task.entities.Task;
import ch.heg.task.exceptions.TaskNotFoundException;
import ch.heg.task.services.TasksServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class TaskController {

    private final TasksServices tasksServices;

    public TaskController(TasksServices tasksServices) {
        this.tasksServices = tasksServices;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tasks")
    public List<Task> getTasks() {
        return tasksServices.findAllTasks();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/v1/tasks")
    public List<TaskSummary> getAllTasks() {
        return tasksServices.findAllTasks()
                .stream()
                .map(task -> new TaskSummary(task.getId(), task.getDescription()))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tasks/{id}")
    public Task getTasks(@RequestHeader(value="User-Agent") String userAgent, @PathVariable Long id) {
        log.info("Receive query with user-agent : %s".formatted(userAgent));
        return tasksServices.findById(id);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/v1/tasks/{id}")
    public Task getTasksV1(@PathVariable Long id) {
        try {
            return tasksServices.findByIdWithException(id);
        } catch (TaskNotFoundException tskExcep) {
            ResponseStatusException respEx = new ResponseStatusException(HttpStatus.NOT_FOUND, tskExcep.getMessage(), tskExcep);
            respEx.setTitle("Task not Found");
            throw respEx;
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/v2/tasks/{id}", produces = {"application/json", "application/problem+json"})
    public Task getTasksV2(@PathVariable Long id) {
        return tasksServices.findByIdWithRuntimeException(id);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/tasks")
    public Task createTask(@RequestBody Task task) {
        task.setDateCreation(new Date());
        return tasksServices.addTask(task);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/tasks")
    public Task updateTask(@RequestBody Task task) {
        return tasksServices.updateTask(task);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {
        tasksServices.deleteTask(id);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tasks/search")
    public List<Task> getTasks(@RequestHeader("x-version") String version, @RequestParam String status) {
        log.info("Receive query with x-version : %s".formatted(version));
        return tasksServices.findByStatus(Statut.valueOf(status));
    }
}
