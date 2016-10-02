package me.jin.account;

import me.jin.commons.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nayoung on 2016. 9. 20..
 */
@RestController
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create,
                                        BindingResult result){
        if (result.hasErrors()){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("잘못된 요청입니다.");
            errorResponse.setCode("bad.request");
            // TODO BindingResult 안에 들어있는 에러 정보 사용하기
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Account newAccount = service.createAccount(create);

        /*
        서비스 정상에러 확인 방법
        1. 리턴 타입으로 판단
        2. 파라미터 이용
        3. 서비스에서 예외를 던지는 방법
         */
        return new ResponseEntity(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    public ResponseEntity handleUserDuplicatedException(UserDuplicatedException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("[" + e.getUsername() + "] 중복된 username 입니다");
        errorResponse.setCode("duplicated.username.exception");
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // TODO: 2016. 9. 28. 예외 처리 네번째 방법 (콜백 비스무리한거..)
    // TODO: 2016. 10. 2. HETEOAS
    // /accounts?page=0&size=10&sort=username,asc&sort=fullName,desc
    // matrix path 로도 사용가능
    // TODO 뷰
    // NSPA 1. Thymeleaf
    // SPA 2. 앵귤러 3. 리액트
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity getAccounts(Pageable pageable) {
//        Page<Account> page = repository.findAll(pageable);
//        page.getContent().parallelStream().map(account -> modelMapper.map(account, AccountDto.Response.class))
//                .collect(Collectors.toList());
        Page<Account> page = repository.findAll(pageable);
        // TODO: 2016. 10. 2. stream() vs parallelStream() 
        List<AccountDto.Response> content = page.getContent().parallelStream()
                .map(account -> modelMapper.map(account, AccountDto.Response.class))
                .collect(Collectors.toList());
        PageImpl<AccountDto.Response> result = new PageImpl(content, pageable, page.getTotalElements());
        return new ResponseEntity(result, HttpStatus.OK);
    }

}
