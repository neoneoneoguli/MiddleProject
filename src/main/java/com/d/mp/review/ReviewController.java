package com.d.mp.review;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/review/**")
public class ReviewController {
	
	@GetMapping("reviewMain")
	public String reviewMain() {
		return "review/reviewMain";		
	}
	
	@GetMapping("reviewOpenDetail")
	public String reviewOpenDetail() {
		return "review/reviewOpenDetail";		
	}

}
