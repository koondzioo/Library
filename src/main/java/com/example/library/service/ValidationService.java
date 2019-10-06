package com.example.library.service;

import com.example.library.exception.ExceptionCode;
import com.example.library.exception.MyException;
import com.example.library.model.User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ValidationService {
    
    public boolean validateUser(User user) {
        try {
            boolean isValid = true;
            if(user == null)
            {
                isValid = false;
            }
            if(!(user.getUsername() != null && user.getUsername().matches("[a-z]+")))
            {
                isValid = false;
            }
            if(!Objects.equals(user.getPassword(), user.getPasswordConfirmation()))
            {
                isValid = false;
            }
            return isValid;
        }catch (Exception e)
        {
            throw new MyException(ExceptionCode.VALIDATION, "VALIDATION USER EXCEPTION");
        }
    }
}
