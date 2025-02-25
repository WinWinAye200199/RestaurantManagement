package com.example.resturant_management_system.service;

import java.util.List;

import com.example.resturant_management_system.model.dto.LeaveRequestDto;
import com.example.resturant_management_system.model.entities.LeaveRequest;
import com.example.resturant_management_system.security.UserPrincipal;


public interface LeaveRequestService {
    void save(LeaveRequest leaveRequest);
    LeaveRequest findById(Long id);
    List<LeaveRequest> findAllByUserId(Long userId);
    List<LeaveRequestDto> getLeaveRequestsByUser(UserPrincipal currentUser);
}

