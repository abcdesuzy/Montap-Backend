package com.project.montap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


// MVC 사이클
// 1. 브라우저에서 요청을 날린다 (or postman)
// 2. 스프링 웹서버에 요청이 들어온다.
// 3. 컨트롤러 (입구) 에서 요청에 맞는 메소드를 찾는다.
// 4. 컨트롤러의 해당 메소드에서 서비스를 호출한다.
// 5. 서비스에서 정의된 로직이 수행된다. (repository 를 통한 디비 접속) - 핵심로직
// 6. repository 에서 DB 로 부터 데이터를 가져온다.
// 7. service -> controller -> 응답을 브라우저로 전달한다.

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "hello MonTap [!]";
    }

    // REST API 가장 많이 쓰는 것들

    // @GetMapping -> 리소스 얻기
    // @PostMapping -> 리소스 생성
    // @PutMapping -> 리소스 수정
    // (@PatchMapping -> 리소스 일부 수정)
    // @DeleteMapping -> 리소스 삭제

}
