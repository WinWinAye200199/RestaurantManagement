package com.example.resturant_management_system.service.Impl;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.resturant_management_system.exception.BadRequestException;
import com.example.resturant_management_system.exception.NotFoundException;
import com.example.resturant_management_system.exception.UnauthorizedException;
import com.example.resturant_management_system.model.entities.ResetToken;
import com.example.resturant_management_system.model.entities.User;
import com.example.resturant_management_system.model.request.LoginRequest;
import com.example.resturant_management_system.model.request.ResetPasswordRequest;
import com.example.resturant_management_system.model.response.ApiResponse;
import com.example.resturant_management_system.model.response.JwtResponse;
import com.example.resturant_management_system.repository.ResetTokenRepository;
import com.example.resturant_management_system.repository.UserRepository;
import com.example.resturant_management_system.security.UserPrincipal;
import com.example.resturant_management_system.service.AuthService;
import com.example.resturant_management_system.service.EmailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	private final JwtServiceImpl jwtService;
	
	private final ResetTokenRepository resetTokenRepository;
//	
//    private final JavaMailSender mailSender;
    
    private final EmailService emailService;

//	@Override
//	public JwtResponse authenticate(LoginRequest loginRequest) {
//
//		Date expiredAt = new Date((new Date()).getTime() + 86400 * 1000);
//		try {
//			Authentication authentication = authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//			if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")|| grantedAuthority.getAuthority().equals("ROLE_USER"))) {
//				String jwtToken = jwtService.createToken(authentication);
//				return new JwtResponse("Bearer", jwtToken, expiredAt.toInstant().toString());
//			} else {
//				throw new NotFoundException("Username or Password is wrong!");
//			}
//		} catch (Exception e) {
//			throw new UnauthorizedException("Username or Password is wrong!");
//		}
//
//	}
    
//    @Override
//    public JwtResponse authenticate(LoginRequest loginRequest) {
//        Date expiredAt = new Date((new Date()).getTime() + 86400 * 1000);
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//            // Check if the authenticated user has either ROLE_ADMIN or ROLE_USER
//            if (authentication.getAuthorities().stream()
//                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN") 
//                            || grantedAuthority.getAuthority().equals("ROLE_USER"))) {
//                
//                String jwtToken = jwtService.createToken(authentication);
//                return new JwtResponse("Bearer", jwtToken, expiredAt.toInstant().toString());
//            } else {
//                throw new NotFoundException("Username or Password is wrong!");
//            }
//        } catch (Exception e) {
//            throw new UnauthorizedException("Username or Password is wrong!");
//        }
//    }
    @Override
    public JwtResponse authenticate(LoginRequest loginRequest) {
        Date expiredAt = new Date((new Date()).getTime() + 86400 * 1000);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            // Extract role
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.equals("ROLE_ADMIN") || auth.equals("ROLE_USER"))
                    .findFirst()
                    .orElseThrow(() -> new UnauthorizedException("User role not found!"));

            String jwtToken = jwtService.createToken(authentication);

            return new JwtResponse("Bearer", jwtToken, expiredAt.toInstant().toString(), role);
        } catch (Exception e) {
            throw new UnauthorizedException("Username or Password is wrong!");
        }
    }



	@Override
	public ApiResponse changePassword(UserPrincipal currentUser, String oldPassword, String newPassword) {

		System.out.println(currentUser.getName());
		User foundUser = userRepository.findByName(currentUser.getUsername()).orElseThrow(
				() -> new BadRequestException("Username Not Found : username -> " + currentUser.getUsername()));
		
		String foundPassword = foundUser.getPassword();

		if (passwordEncoder.matches(oldPassword, foundPassword)) {

			foundUser.setPassword(passwordEncoder.encode(newPassword));

			userRepository.save(foundUser);
			return new ApiResponse(true, "Changed Password Successfully!");
		} else {

//				return new ApiResponse(false,"Wrong Old Password!");
			throw new BadRequestException("Wrong Password!");
		}

	}

//	@Override
//	public UserResponse createUser(UserRequest userRequest) {
//
//		User user = new User();
//		user.setUsername(userRequest.getUsername());
//		
//		List<Role>roles = new ArrayList<>();
//		List<Long> roleIds = userRequest.getRoleIds();
//		for (Long roleId : roleIds) {
//			Role role = roleRepository.findById(roleId).orElse(null);
//			roles.add(role);
//		}
//		user.setRoles(roles);
//		String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
//		user.setPassword(encodedPassword);
//
//		User savedUser = userRepository.save(user);
//		UserDto savedUserDto = userMapper.mapToDto(savedUser);
//		UserResponse savedUserResponse = userMapper.mapToResponse(savedUserDto);
//
//		return savedUserResponse;
//	}

//	@Override
//	public ApiResponse resetPassword(UserDto userDto) {
//
//		User foundUser = userRepository.findByUsername(userDto.getUsername())
//				.orElseThrow(() -> new BadRequestException("Username Not Found!"));
//		if (foundUser != null) {
//			String resetPassword = "Admin@123";
//			foundUser.setPassword(passwordEncoder.encode(resetPassword));
//			userRepository.save(foundUser);
//
//			return new ApiResponse(true, "Reset Password Successfully!");
//
//		}
////			return new ApiResponse(false,"Wrong Username!");
//		throw new NotFoundException("Username Not Found!");
//	}
	@Override
    public ApiResponse requestResetPassword(String email) {
//        // Step 1: Find user by username
//		System.out.println("Username: " + email);
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new NotFoundException("User not found!"));
//        
//        System.out.println("User found: " + user.getName());
//		
//        // Step 2: Generate reset token
//        String token = UUID.randomUUID().toString();
//        ResetToken resetToken = new ResetToken(token, user);
//        resetTokenRepository.save(resetToken);
//
//        // Step 3: Send token via email
//        String emailContent = "Click the link below to reset your password:\n" +
//                "http://yourapp.com/reset-password?token=" + token;
//        try {
//			sendEmail(user.getEmail(), "Reset Password", emailContent);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        return new ApiResponse(true, "Password reset link sent to your email.");
		 // Step 1: Find user by username
	    System.out.println("Username: " + email);
	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new NotFoundException("User not found!"));
	    
	    System.out.println("User found: " + user.getName());
	    
	    // Step 2: Generate a reset code (e.g., 6-digit random code)
	    String resetCode = generateResetCode();
	    
	    // Optionally, save the code in the database for future validation (if needed)
	    ResetToken resetToken = new ResetToken(resetCode, user); 
	    resetTokenRepository.save(resetToken);

//	    // Step 3: Send reset code via email
//	    String emailContent = "Your password reset code is: " + resetCode;
//	    try {
//	        sendEmail(user.getEmail(), "Reset Password", emailContent);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//
//	    return new ApiResponse(true, "Password reset code sent to your email.");
	 // Step 3: Send reset code via email
	    String emailSubject = "Reset Password";
	    String emailBody = "Dear " + user.getName() + ",\n\n"
	            + "Your password reset code is: " + resetCode + ".\n"
	            + "Please use this code to reset your password.\n\n"
	            + "Best Regards,\nSupport Team";

	    try {
	        emailService.sendEmail(user.getEmail(), emailSubject, emailBody);
	    } catch (Exception e) {
	        e.printStackTrace(); // Handle exception and log error
	    }

	    return new ApiResponse(true, "Password reset code sent to your email.");
    }
	
	private String generateResetCode() {
	    // Generate a random 6-digit code (you can modify this as needed)
	    int code = (int)(Math.random() * 900000) + 100000;
	    return String.valueOf(code);
	}


    @Override
    public ApiResponse resetPassword(ResetPasswordRequest request) {
//        // Step 1: Validate token
//        ResetToken resetToken = resetTokenRepository.findByToken(request.getToken())
//                .orElseThrow(() -> new BadRequestException("Invalid or expired token!"));
//
//        // Step 2: Validate new passwords
//        if (!request.getNewPassword().equals(request.getConfirmedPassword())) {
//            throw new BadRequestException("Passwords do not match!");
//        }
//
//        // Step 3: Update user's password
//        User user = resetToken.getUser();
//        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
//        userRepository.save(user);
//
//        // Step 4: Invalidate the token
//        resetTokenRepository.delete(resetToken);
//
//        return new ApiResponse(true, "Password reset successfully!");
    	// Step 1: Validate reset code
        ResetToken resetToken = resetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid or expired reset code!"));

        // Step 2: Validate new passwords
        if (!request.getNewPassword().equals(request.getConfirmedPassword())) {
            throw new BadRequestException("Passwords do not match!");
        }

        // Step 3: Update user's password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Step 4: Invalidate the code (delete or mark it as used)
        resetTokenRepository.delete(resetToken);

        return new ApiResponse(true, "Password reset successfully!");
    }

//    private void sendEmail(String recipientEmail, String subject, String content) throws Exception {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(recipientEmail);
//            helper.setSubject(subject);
//            helper.setText(content, true);
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            throw new Exception("Failed to send email to " + recipientEmail, e);
//        }
//    }

}