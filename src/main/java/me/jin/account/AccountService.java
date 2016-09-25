package me.jin.account;

import org.modelmapper.ModelMapper;
//import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by nayoung on 2016. 9. 23..
 */
@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public Account createAccount(AccountDto.Create dto) {
//        Account account = new Account();
//        account.setUsername(dto.getUsername());
//        account.setPassword(dto.getPassword());
        //model mapper 사용할수있음

        Account account = modelMapper.map(dto, Account.class);
//        Account account = new Account();
//        BeanUtils.copyProperties(dto, account);

        Date now = new Date();
        account.setJoined(now);
        account.setUpdated(now);

        return repository.save(account);

    }
}
