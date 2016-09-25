package me.jin.account;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by nayoung on 2016. 9. 20..
 */
@RestController
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private ModelMapper modelMapper;
//    private AccountRepository repository;

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto.Create create, BindingResult result){
        if(result.hasErrors()){
            // 에러 응답 본문 추가
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Account newAccount = service.createAccount(create);
        //return new ResponseEntity<Account>(newAccount, HttpStatus.CREATED);
        return new ResponseEntity(modelMapper.map(newAccount, AccountDto.Response.class), HttpStatus.CREATED);
    }

}
