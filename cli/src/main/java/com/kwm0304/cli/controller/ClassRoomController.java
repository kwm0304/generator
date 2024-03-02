package com.kwm0304.cli.controller;

import com.kwm0304.cli.entity.ClassRoom;
import com.kwm0304.cli.service.ClassRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/classRooms")
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    @Autowired
    public ClassRoomController(ClassRoomService classRoomService) {
        this.classRoomService = classRoomService;
    }

    @GetMapping
    public List<ClassRoom> getAll() {
        return classRoomService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassRoom> getById(@PathVariable Integer id) {
        return classRoomService.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ClassRoom create(@RequestBody ClassRoom classRoom) {
        return classRoomService.save(classRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassRoom> update(@PathVariable Integer id, @RequestBody ClassRoom classRoomDetails) {
        return ResponseEntity.ok(classRoomService.update(id, classRoomDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        classRoomService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
