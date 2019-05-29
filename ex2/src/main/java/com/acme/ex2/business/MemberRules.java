package com.acme.ex2.business;

import com.acme.ex2.model.entity.Member;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class MemberRules {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @HandleBeforeCreate
    @HandleBeforeSave
    public void encryptPassword(Member member) {

        System.out.println("################# ENCRYPTION #################");
        if (member.getAccount().getPassword() != null) {
            member.getAccount().setPassword(encoder.encode(member.getAccount().getPassword()));
        }

    }

}
