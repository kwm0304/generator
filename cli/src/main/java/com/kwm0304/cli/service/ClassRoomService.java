package com.kwm0304.cli.service;

import com.kwm0304.cli.entity.ClassRoom;
import com.kwm0304.cli.repository.ClassRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClassRoomService {

    @Autowired
    private final ClassRoomRepository classRoomRepository;

    public ClassRoomService(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;
    }

    public List<ClassRoom> findAll() {
        return classRoomRepository.findAll();
    }

    public Optional<ClassRoom> findById(Integer id) {
        return classRoomRepository.findById(id);
    }

    public ClassRoom save(ClassRoom classRoom) {
        return classRoomRepository.save(classRoom);
    }

    public ClassRoom update(Integer id, ClassRoom classRoomDetails) {
        if (!classRoomRepository.existsById(id)) {
            throw new RuntimeException("ClassRoom not found with id " + id);
        }
        classRoomDetails.setId(id);
        return classRoomRepository.save(classRoomDetails);
    }

    public void deleteById(Integer id) {
        if (!classRoomRepository.existsById(id)) {
            throw new RuntimeException("ClassRoom not found with id " + id);
        }
        classRoomRepository.deleteById(id);
    }
}
