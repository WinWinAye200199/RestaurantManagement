package com.example.resturant_management_system.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.resturant_management_system.model.entities.Attendance;
import com.example.resturant_management_system.model.entities.User;
import com.example.resturant_management_system.repository.AttendanceRepository;
import com.example.resturant_management_system.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public void save(Attendance attendance) {
        attendanceRepository.save(attendance);
    }

    @Override
    public Attendance findLastAttendance(User user) {
    	Attendance attendance = attendanceRepository.findTopByUser_IdOrderByClockInDesc(user.getId())
                .orElseThrow(() -> new IllegalStateException("No clock-in record found for the user."));
        System.out.print("I'm in attendace service"+ attendance.getClockIn());
    	return attendance;
    }
    
}
