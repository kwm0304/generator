package com.kwm0304.cli.controller;

import com.kwm0304.cli.model.Class;
import com.kwm0304.cli.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/classs")
public class ClassController {

    private final ClassService classService;

    @Autowired
    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping
    public List<Class> getAll() {
        return classService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Class> getById(@PathVariable Integer id) {
        return classService.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Class create(@RequestBody Class class) {
        return classService.save(class);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Class> update(@PathVariable Integer id, @RequestBody Class classDetails) {
        return ResponseEntity.ok(classService.update(id, classDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        classService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
