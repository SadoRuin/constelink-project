package com.srp.constelinkbeneficiary.db.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srp.constelinkbeneficiary.common.exception.CustomException;
import com.srp.constelinkbeneficiary.common.exception.CustomExceptionType;
import com.srp.constelinkbeneficiary.db.dto.enums.BeneficiaryMemberDonate;
import com.srp.constelinkbeneficiary.db.dto.enums.BeneficiarySortType;
import com.srp.constelinkbeneficiary.db.dto.request.BeneficiaryReqeust;
import com.srp.constelinkbeneficiary.db.dto.response.BeneficiaryByDiaryDateResponse;
import com.srp.constelinkbeneficiary.db.dto.response.BeneficiaryInfoResponse;
import com.srp.constelinkbeneficiary.db.service.BeneficiaryService;
import com.srp.constelinkbeneficiary.jwt.JWTParser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "수혜자 API", description = "수혜자 API")
@RestController
@RequestMapping("/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

	private final BeneficiaryService beneficiaryService;
	private final JWTParser jwtParser;

	@Operation(summary = "한명의 수혜자 정보 조회", description = "beneficiaryId = 수혜자 Id")
	@GetMapping("/{beneficiaryId}")
	// 해당 수혜자 정보 가져오기
	public ResponseEntity<BeneficiaryInfoResponse> findBeneficiaryById(
		@PathVariable(value = "beneficiaryId") Long beneficiaryId) {
		BeneficiaryInfoResponse beneficiary = beneficiaryService.findBeneficiaryById(beneficiaryId);
		return ResponseEntity.ok(beneficiary);
	}

	@Operation(summary = "해당 병원의 수혜자 목록 조회", description = "hospitalId = 병원ID, "
		+ "page = 페이지, "
		+ "size = 한 페이지 담는 자료 수")
	@GetMapping("/hospital/{hospitalId}")
	// 하나의 병원에 있는 모든 수혜자 목록 가져오기
	public ResponseEntity<Page<BeneficiaryInfoResponse>> findBeneficiaryByHospitalId(
		@PathVariable(value = "hospitalId") Long hospitalId,
		@RequestParam(value = "page", required = false, defaultValue = "1") int page,
		@RequestParam(value = "size", required = false, defaultValue = "5") int size
	) {
		Page<BeneficiaryInfoResponse> beneficiaryInfoList = beneficiaryService.findBeneficiariesByHospitalId(hospitalId,
			page - 1, size);
		return ResponseEntity.ok(beneficiaryInfoList);
	}

	@Operation(summary = "수혜자 등록", description = "hospitalId = 병원ID, beneficiaryName = 수혜자 이름, beneficiaryBirthday = 수혜자 생일, beneficiaryDisease = 수혜자 병명, beneficiaryPhoto = 수혜자 사진, beneficiaryAmountGoal = 목표금액 필요")
	@PostMapping("")
	public ResponseEntity<BeneficiaryInfoResponse> addBeneficiary(
		@RequestBody BeneficiaryReqeust beneficiaryReqeust,
		HttpServletRequest request
	) {
		Long hospitalId = beneficiaryReqeust.getHospitalId();
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(accessToken == null) {
			if(beneficiaryReqeust.getHospitalId() <1) {
				throw new CustomException(CustomExceptionType.HOSPITAL_NOT_FOUND);
			}
			hospitalId=beneficiaryReqeust.getHospitalId();
		} else {
			hospitalId = jwtParser.resolveToken(accessToken);
			beneficiaryReqeust.setHospitalId(hospitalId);
		}
		BeneficiaryInfoResponse beneficiaryInfoResponse = beneficiaryService.addBeneficiary(beneficiaryReqeust);

		return ResponseEntity.ok(beneficiaryInfoResponse);
	}

	@Operation(summary = "모든 수혜자 목록 조회", description = "page = 페이지, "
		+ "size = 한 페이지 개수, "
		+ "sortBy = 정렬 타입"
		+ ", ☆DIARY_DATE_DESC, DIARY_DATE_ASC는 DIARY 있는 것들만 출력☆")
	@GetMapping("")
	public ResponseEntity<Page<BeneficiaryInfoResponse>> findAllBeneficiaries(
		@RequestParam(value = "page", required = false, defaultValue = "1") int page,
		@RequestParam(value = "size", required = false, defaultValue = "5") int size,
		@RequestParam(value = "sortBy", required = false, defaultValue = "NONE") BeneficiarySortType sortType
	) {
		Page<BeneficiaryInfoResponse> beneficiaryInfoList = beneficiaryService.findAllBeneficiary(
			page - 1, size, sortType);

		return ResponseEntity.ok(beneficiaryInfoList);
	}


	@Operation(summary = "해당 회원이 기부했던 수혜자 목록 조회(일기 있는 사람만)", description = "memberId = 회원ID, "
		+ "page = 페이지, "
		+ "size = 한 페이지 담는 자료 수"
		+ "sortBy = 정렬타입")
	@GetMapping("/donate")
	// 회원이 기부한 모든 수혜자 목록 가져오기
	public ResponseEntity<Page<BeneficiaryByDiaryDateResponse>> findBeneficiaryByMemberDonate(
		@RequestParam(value = "page", required = false, defaultValue = "1") int page,
		@RequestParam(value = "size", required = false, defaultValue = "5") int size,
		@RequestParam(value = "sortBy", required = false, defaultValue = "NONE") BeneficiaryMemberDonate sortType,
		@RequestParam(value = "memberId", required = false, defaultValue = "0") Long memberId,
		HttpServletRequest request
	) {
		String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		if(accessToken == null) {
			if(memberId<1){
				throw new CustomException(CustomExceptionType.MEMBER_NOT_FOUND);
			}
		} else {
			memberId = jwtParser.resolveToken(accessToken);
		}
		Page<BeneficiaryByDiaryDateResponse> beneficiaryInfoList = beneficiaryService.getBeneficiaryDonatedByMember(
			page - 1, size, memberId, sortType);
		return ResponseEntity.ok(beneficiaryInfoList);
	}

}
