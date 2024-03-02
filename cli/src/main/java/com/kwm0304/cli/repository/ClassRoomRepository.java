package com.kwm0304.cli.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kwm0304.cli.entity.ClassRoom;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Integer> {
}