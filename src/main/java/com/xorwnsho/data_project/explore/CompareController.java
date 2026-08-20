package com.xorwnsho.data_project.explore;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompareController {

	private final CompareService compareService;

	@PostMapping("/compare")
	public CompareResponse compare(@Valid @RequestBody CompareRequest request) {
		return compareService.compare(request);
	}
}
