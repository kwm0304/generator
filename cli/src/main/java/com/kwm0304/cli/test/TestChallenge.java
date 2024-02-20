package com.kwm0304.cli.test;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class TestChallenge {
    @Id
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;
    private boolean active;
    private int dayNumber;

    public TestChallenge(Long id, LocalDate startDate, LocalDate endDate, boolean active, int dayNumber) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.dayNumber = dayNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }
}
