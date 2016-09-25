package me.jin.account;

import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;


/**
 * Created by nayoung on 2016. 9. 20..
 */
public class AccountTest {

    @Test
    public void getterSetter(){
        Account account = new Account();
        account.setUsername("soolbe");
        account.setPassword("password");

        assertThat(account.getUsername(), is("soolbe"));

    }


}
