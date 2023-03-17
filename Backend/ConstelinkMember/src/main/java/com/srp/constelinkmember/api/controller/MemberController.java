package com.srp.constelinkmember.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srp.constelinkmember.api.service.MemberService;
import com.srp.constelinkmember.util.CookieUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Tag(name = "멤버", description = "멤버 관련 api 입니다.")
@Slf4j
public class MemberController {

	private final MemberService memberService;
	private final CookieUtils cookieUtils;

	@Operation(summary = "회원정보", description = "회원정보 조회 메서드.")
	@GetMapping("/memberInfo")
	public ResponseEntity memberInfo(HttpServletRequest request) {
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		log.info("access == " + accessToken);
		memberService.getMemberInfo(accessToken);
		return null;
	}

	@Operation(summary = "회원탈퇴", description = "회원탈퇴 메서드입니다.")
	@PostMapping("/withdrawal")
	public ResponseEntity withdrawal(HttpServletRequest request) {
		String AccessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		Cookie[] cookies = request.getCookies();
		String refreshToken = cookieUtils.getCookieInfo(cookies, "refresh");
		memberService.withdrawal(AccessToken, refreshToken);
		return ResponseEntity.ok("회원 탈최가 정상적으로 완료되었습니다");
	}

}
