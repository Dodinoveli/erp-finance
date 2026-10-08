package com.logikaintermedia.erp.modules.coamapping;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.logikaintermedia.erp.jwt.AuthUserPrincipal;
import com.logikaintermedia.erp.utility.DataTableResponse;
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

}
