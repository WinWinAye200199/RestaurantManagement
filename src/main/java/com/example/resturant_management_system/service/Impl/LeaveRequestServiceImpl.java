package com.example.resturant_management_system.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.resturant_management_system.model.dto.LeaveRequestDto;
import com.example.resturant_management_system.model.entities.LeaveRequest;
import com.example.resturant_management_system.repository.LeaveRequestRepository;
import com.example.resturant_management_system.security.UserPrincipal;
import com.example.resturant_management_system.service.LeaveRequestService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Override
    public void save(LeaveRequest leaveRequest) {
        leaveRequestRepository.save(leaveRequest);
    }

    @Override
    public LeaveRequest findById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Leave request not found"));
    }

    @Override
    public List<LeaveRequest> findAllByUserId(Long userId) {
        return leaveRequestRepository.findAllByUser_Id(userId);
    }
    
    // Method to get leave requests by user
    public List<LeaveRequestDto> getLeaveRequestsByUser(UserPrincipal user) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUser(user);
        return leaveRequests.stream()
                .map(leaveRequest -> {
                    LeaveRequestDto leaveRequestDto = new LeaveRequestDto();
                    leaveRequestDto.setId(leaveRequest.getId());
                    leaveRequestDto.setStartDate(leaveRequest.getStartDate());
                    leaveRequestDto.setEndDate(leaveRequest.getEndDate());
                    leaveRequestDto.setReason(leaveRequest.getReason());
                    leaveRequestDto.setStatus(leaveRequest.getStatus());
                    return leaveRequestDto;
                })
                .collect(Collectors.toList());
    }
}
