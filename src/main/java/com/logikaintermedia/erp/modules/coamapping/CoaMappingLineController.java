package com.logikaintermedia.erp.modules.coamapping;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccountsResponse;
import com.logikaintermedia.erp.utility.ApiResponse;
import com.logikaintermedia.erp.utility.DataTableResponse;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/coamappingline")
public class CoaMappingLineController {
	private CoaMappingLineService service;

	public CoaMappingLineController(CoaMappingLineService service) {
		this.service = service;
	}

	@PreAuthorize("hasAnyRole('Owner', 'Admin')")
	@GetMapping("/find-coa-mapping-by-company-id")
	public ResponseEntity<?> findCoaMappingByCompanyId(@AuthenticationPrincipal AuthUserPrincipal user) {
		List<CoaMappingResponse> response = service.findCoaMappingByCompanyId(user.getCompanyId());
		return ResponseEntity.ok(ApiResponse.success("success", response));
	}
	
	@PreAuthorize("hasAnyRole('Owner', 'Admin')")
	@GetMapping("/find-coa-by-company-id")
	public ResponseEntity<?> findCoaByCompanyId(@AuthenticationPrincipal AuthUserPrincipal user) {
		List<ChartOfAccountsResponse> response = service.findCoaByCompanyId(user.getCompanyId());
		return ResponseEntity.ok(ApiResponse.success("success", response));
	}
	
	@PreAuthorize("hasAnyRole('Owner', 'Admin')")
	@GetMapping
	public ResponseEntity<DataTableResponse<CoaMappingLineResponse>> findCoaMappingById(
			@AuthenticationPrincipal AuthUserPrincipal user, @RequestParam(defaultValue = "0") int draw,
			@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "10") int length,
			@RequestParam(required = false) String keyword) {
		log.warn("KEYWORD : {}", keyword);

		DataTableResponse<CoaMappingLineResponse> response = service.findCoaMappingLineByCompanyId(user.getCompanyId(),
				draw, start, length, keyword);
		return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasAnyRole('Owner', 'Admin')")
	@PostMapping("/{mappingId}/lines")
	public ResponseEntity<?> save(
			@AuthenticationPrincipal AuthUserPrincipal user, @PathVariable UUID mappingId, 
			@Valid @RequestBody List<CoaMappingLineRequest> lines) {
		int[] response = service.save(lines, user.getCompanyId(), mappingId);
		return ResponseEntity.ok(ApiResponse.success("success", response));
	}
}
