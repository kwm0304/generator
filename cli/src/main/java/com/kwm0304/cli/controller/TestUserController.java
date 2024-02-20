package com.kwm0304.cli.controller;

import com.kwm0304.cli.model.TestUser;
import com.kwm0304.cli.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/testUsers")
public class TestUserController {

    private final TestUserService testUserService;

    @Autowired
    public TestUserController(TestUserService testUserService) {
        this.testUserService = testUserService;
    }

    @GetMapping
    public List<TestUser> getAll() {
        return testUserService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestUser> getById(@PathVariable Long id) {
        return testUserService.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TestUser create(@RequestBody TestUser testUser) {
        return testUserService.save(testUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestUser> update(@PathVariable Long id, @RequestBody TestUser testUserDetails) {
        return ResponseEntity.ok(testUserService.update(id, testUserDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        testUserService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
