package com.bloomreach.garage.reservation.api.component;

import com.bloomreach.garage.reservation.api.entity.EmployeeWorkingHours;
import com.bloomreach.garage.reservation.api.entity.GarageOperation;
import com.bloomreach.garage.reservation.api.model.AvailableSlot;
import com.bloomreach.garage.reservation.config.ReservationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Calculates available time slots for mechanics based on their working hours
 * and the durations of the garage operations to be performed.
 */
@RequiredArgsConstructor
@Component
public class SlotCalculator {

    private final ReservationProperties reservationProperties;

    /**
     * Calculates available time slots for a given mechanic's working hours and a list of operations.
     *
     * @param workingHours The working hours of the mechanic.
     * @param operations   The list of operations to accommodate within the time slots.
     * @return A list of available time slots for the mechanic.
     */
    public List<AvailableSlot> calculateSlots(final EmployeeWorkingHours workingHours, final List<GarageOperation> operations) {
        final List<AvailableSlot> availableSlots = new ArrayList<>();
        LocalTime start = workingHours.getStartTime();
        final LocalTime end = workingHours.getEndTime();

        // Minimum slot duration
        final int minDuration = reservationProperties.getDefaultSlotDuration();

        // Calculate minimum advance time
        final LocalTime nowPlusMinAdvance = LocalTime.now().plusMinutes(reservationProperties.getMinAdvanceMinutes());

        // Convert start time and nowPlusMinAdvance to LocalDateTime for proper comparison
        LocalDateTime startDateTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), start);
        final LocalDateTime nowPlusMinAdvanceDateTime = LocalDateTime.of(LocalDateTime.now().toLocalDate(), nowPlusMinAdvance);

        // If start time is before nowPlusMinAdvance, check if it's on the next day
        if (startDateTime.isBefore(nowPlusMinAdvanceDateTime)) {
            startDateTime = startDateTime.plusDays(1);  // Adjust for the next day if needed
        }

        // Calculate possible time slots
        while (start.plusMinutes(minDuration).isBefore(end)) {
            boolean canAccommodateAll = true;
            LocalTime slotEnd = start;

            for (final GarageOperation operation : operations) {
                slotEnd = slotEnd.plusMinutes(operation.getDurationInMinutes());
                if (slotEnd.isAfter(end)) {
                    canAccommodateAll = false;
                    break;
                }
            }

            if (canAccommodateAll && !startDateTime.isBefore(nowPlusMinAdvanceDateTime)) {
                availableSlots.add(new AvailableSlot(start, slotEnd));
            }

            start = start.plusMinutes(minDuration);
        }

        return availableSlots;
    }
}
