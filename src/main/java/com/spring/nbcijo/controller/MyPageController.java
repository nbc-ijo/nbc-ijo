package com.spring.nbcijo.controller;

import com.spring.nbcijo.dto.response.MyInformResponseDto;
import com.spring.nbcijo.dto.response.ResponseDto;
import com.spring.nbcijo.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<ResponseDto<MyInformResponseDto>> getMyInform() {

        MyInformResponseDto myInformResponseDto = myPageService.getMyInform(1L);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ResponseDto.<MyInformResponseDto>builder()
                .statusCode(HttpStatus.OK.value())
//                .message("내 정보 조회가 완료되었습니다.")
                .data(myInformResponseDto).build());
    }
}