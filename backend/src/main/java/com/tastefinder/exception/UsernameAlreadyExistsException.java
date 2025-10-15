package com.tastefinder.exception;

/**
 * 用户名已存在异常
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    
    public UsernameAlreadyExistsException(String username) {
        super("用户名已存在: " + username);
    }
}

