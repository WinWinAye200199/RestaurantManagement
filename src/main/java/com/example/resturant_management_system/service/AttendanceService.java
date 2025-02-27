package com.example.resturant_management_system.service;

import com.example.resturant_management_system.model.entities.Attendance;
import com.example.resturant_management_system.model.entities.User;

public interface AttendanceService {
    void save(Attendance attendance);
    Attendance findLastAttendance(User user);

}

