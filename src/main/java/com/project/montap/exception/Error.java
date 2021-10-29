package com.project.montap.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Error {

    private String errorMsg;

    public static Error unknownError() {

        return new Error("서버에 문제가 발생했습니다.");
    }

}
