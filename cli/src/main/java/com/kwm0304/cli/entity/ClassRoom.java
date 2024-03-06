package com.kwm0304.cli.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_number")
    private Integer roomNumber;

    @Column(name = "teacher")
    private String teacher;

    public ClassRoom(Integer id, Integer roomNumber, String teacher) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.teacher = teacher;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassRoom classRoom = (ClassRoom) o;
        return Objects.equals(getId(), classRoom.getId()) && Objects.equals(getRoomNumber(), classRoom.getRoomNumber()) && Objects.equals(getTeacher(), classRoom.getTeacher());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRoomNumber(), getTeacher());
    }

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", roomNumber=" + roomNumber +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}