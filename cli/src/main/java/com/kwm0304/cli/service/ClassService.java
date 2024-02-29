package com.kwm0304.cli.service;

import com.kwm0304.cli.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClassService {

    private final ClassRepository classRepository;

    @Autowired
    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public List<Class> findAll() {
        return classRepository.findAll();
    }

    public Optional<Class> findById(Long id) {
        return classRepository.findById(id);
    }

    public Class save(Class class) {
        return classRepository.save(class);
    }

    public Class update(Long id, Class classDetails) {
        if (!classRepository.existsById(id)) {
            throw new RuntimeException("Class not found with id " + id);
        }
        classDetails.setId(id);
        return classRepository.save(classDetails);
    }

    public void deleteById(Long id) {
        if (!classRepository.existsById(id)) {
            throw new RuntimeException("Class not found with id " + id);
        }
        classRepository.deleteById(id);
    }
}
