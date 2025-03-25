package com.bloomreach.garage.reservation.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "employee_working_hours")
public class EmployeeWorkingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Employee employee;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek; // Day of the week

    @NotNull
    private LocalTime startTime; // Starting time of the employee's work

    @NotNull
    private LocalTime endTime; // Ending time of the employee's work
}
