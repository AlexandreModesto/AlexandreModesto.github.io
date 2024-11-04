package com.task.api.Task.controller;

import com.task.api.Task.model.Task;
import com.task.api.Task.model.TaskDTO;
import com.task.api.Task.service.ServiceTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "task/")
public class ControllerTask {
    @Autowired
    ServiceTask service;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> allTasks = service.getAll();
        return ResponseEntity.ok(allTasks);
    }

    @GetMapping(value = "info/{id}")
    public ResponseEntity<?> getTask(@PathVariable(value = "id")Long id){
        Optional<Task> task =service.getInfo(id);
        if(task.isPresent()){
            return ResponseEntity.ok(task);
        }else {return ResponseEntity.notFound().build();}
    }

    @PostMapping(value = "new")
    public ResponseEntity<?> newTask(@RequestBody @Validated TaskDTO data){
        if(service.checkName(data.name()).isPresent()){
            Task task =new Task(data.name(),data.cost(),data.dateLimit());
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "edit/{id}")
    public ResponseEntity<?> editTask(@RequestBody @Validated Task task, @PathVariable(value = "id")Long id){
        Optional<Task> oldTask = service.getInfo(id);
        if(oldTask.isPresent()){
            var newTask = oldTask.get();
            newTask.setName(task.getName());
            newTask.setCost(task.getCost());
            newTask.setDateLimit(task.getDateLimit());
            newTask.setPosition(task.getPosition());
            service.register(newTask);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable(value = "id")Long id){
        Optional<Task> task = service.getInfo(id);
        if(task.isPresent()){
            service.remove(task.get());
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}