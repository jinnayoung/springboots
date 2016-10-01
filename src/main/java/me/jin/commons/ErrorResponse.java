package me.jin.commons;

import lombok.Data;

import java.util.List;

/**
 * Created by nayoung on 2016. 9. 30..
 */
@Data
public class ErrorResponse {

    private String message;

    private String code;

    private List<FieldError> errors;

    public static class FieldError{
        private  String field;
        private String value;
        private String reason;
    }

}
