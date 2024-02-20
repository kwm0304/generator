package com.kwm0304.cli.controller;

import com.kwm0304.cli.model.TestChallenge;
import com.kwm0304.cli.service.TestChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/testChallenges")
public class TestChallengeController {

    private final TestChallengeService testChallengeService;

    @Autowired
    public TestChallengeController(TestChallengeService testChallengeService) {
        this.testChallengeService = testChallengeService;
    }

    @GetMapping
    public List<TestChallenge> getAll() {
        return testChallengeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestChallenge> getById(@PathVariable Long id) {
        return testChallengeService.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TestChallenge create(@RequestBody TestChallenge testChallenge) {
        return testChallengeService.save(testChallenge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestChallenge> update(@PathVariable Long id, @RequestBody TestChallenge testChallengeDetails) {
        return ResponseEntity.ok(testChallengeService.update(id, testChallengeDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        testChallengeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
