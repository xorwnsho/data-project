package com.xorwnsho.data_project.query;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QueryController {

	private final QueryService queryService;

	@PostMapping("/query")
	public QueryResponse query(@Valid @RequestBody QueryRequest request) {
		return queryService.handle(request.message());
	}
}
