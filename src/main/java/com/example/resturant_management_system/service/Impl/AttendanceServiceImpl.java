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
    
//    @Override
//    public List<Attendance> findByUser(User user) {
//        return attendanceRepository.findByUser(user);
//    }
    
 // Mapping function for converting entity to DTO
//    public List<AttendanceDto> getAttendanceRecordsByUser(UserPrincipal user) {
//        List<Attendance> attendances = attendanceRepository.findByUser(user);
//        List<AttendanceDto> attendanceDtos = new ArrayList<>();
//
//        for (Attendance attendance : attendances) {
//            AttendanceDto attendanceDto = new AttendanceDto();
//            attendanceDto.setId(attendance.getId());
//            attendanceDto.setClockIn(attendance.getClockIn());
//            attendanceDto.setClockOut(attendance.getClockOut());
//            attendanceDto.setTotalHours(attendance.getTotalHours());
//            attendanceDtos.add(attendanceDto);
//        }
//
//        return attendanceDtos;
//    }

//	@Override
//	public List<Attendance> findByUser(UserPrincipal currentUser) {
//		 return attendanceRepository.findByUser(currentUser);
//	}
}
