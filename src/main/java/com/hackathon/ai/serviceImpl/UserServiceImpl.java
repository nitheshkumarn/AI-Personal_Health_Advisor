package com.hackathon.ai.serviceImpl;

import java.util.Date;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hackathon.ai.cache.CacheStore;
import com.hackathon.ai.entity.User;
import com.hackathon.ai.exception.UserAlreadyRegisteredException;
import com.hackathon.ai.repository.UserRepository;
import com.hackathon.ai.requestdto.AuthRequest;
import com.hackathon.ai.requestdto.OTPModel;
import com.hackathon.ai.requestdto.UserRequest;
import com.hackathon.ai.responsedto.AuthResponse;
import com.hackathon.ai.responsedto.UserResponse;
import com.hackathon.ai.security.JwtService;
import com.hackathon.ai.service.UserService;
import com.hackathon.ai.util.CookieManager;
import com.hackathon.ai.util.MessageStructure;
import com.hackathon.ai.util.ResponseStructure;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	UserRepository userRepo;

	private PasswordEncoder passwordEncoder;

	ResponseStructure<UserResponse> structure;

	ResponseStructure<HttpServletResponse> rs;

	private ResponseStructure<AuthResponse> authStructure;

	CacheStore<String> otpCacheStore;

	CacheStore<User> userCacheStore;

	private JavaMailSender javaMailSender;

	private AuthenticationManager authenticationManager;

	private CookieManager cookieManager;

	private JwtService jwtService;

	private User mapToUser(UserRequest userRequest) {
		return User.builder().userName(userRequest.getUserName()).userEmail(userRequest.getUserEmail())
				.userPass(passwordEncoder.encode(userRequest.getUserPass())).build();
	}

	private UserResponse mapToUserResponse(User user) {

		return UserResponse.builder().userId(user.getUserId()).userName(user.getUserName())
				.userEmail(user.getUserEmail()).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> registerUser(UserRequest userRequest) {

		User user = null;
		String OTP = null;

		if (userRepo.existsByUserEmail(userRequest.getUserEmail()) == false) {

			OTP = generateOTP();

			user = mapToUser(userRequest);
			user.setUserName(userRequest.getUserEmail().split("@")[0]);

			userCacheStore.add(userRequest.getUserEmail(), user);
			otpCacheStore.add(userRequest.getUserEmail(), OTP);

			try {
				sendOTPToMail(user, OTP);
			} catch (MessagingException e) {
				log.error("The Email Address doesnt exist");
				e.printStackTrace();
			}

		} else {
			throw new UserAlreadyRegisteredException("User already registered with the given Email");
		}

		structure.setStatus(HttpStatus.ACCEPTED.value()).setMessage("Please Verify mailId using OTP sent " + OTP)
				.setData(mapToUserResponse(user));

		return new ResponseEntity<ResponseStructure<UserResponse>>(structure, HttpStatus.ACCEPTED);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(OTPModel otpModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> login(String accessToken, String refreshToken,
			AuthRequest authRequest, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<ResponseStructure<HttpServletResponse>> logout(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		return null;
	}

	private String generateOTP() {
		// String str = "" + (int) (Math.random() * 900000) + 100000;
		// return str;
		return String.valueOf(new Random().nextInt(100000, 999999));
	}

	private void sendOTPToMail(User user, String otp) throws MessagingException {
		sendMail(MessageStructure.builder().to(user.getUserEmail())
				.subject("Complete Your Registeration to PersonalBuddy ").sentDate(new Date())
				.text("hey, " + user.getUserName() + "Good to see you interested in PersonalBuddy, "
						+ "Complete your Registeration using the OTP <br>" + "<h1>" + otp + "</h1><br>"
						+ "Note: the OTP expires in 1 minute" + "<br><br>" + "with best regards<br>" + "PersonalBuddy")
				.build());
	}

	@Async
	private void sendMail(MessageStructure message) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setTo(message.getTo());
		helper.setSubject(message.getSubject());
		helper.setSentDate(message.getSentDate());
		helper.setText(message.getText(), true);
		javaMailSender.send(mimeMessage);
	}

}
