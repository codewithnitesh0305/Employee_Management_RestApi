package com.springboot.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.springboot.Entity.User;

public interface UserService {

    public User saveUser(User user);
    public void saveExcelData(MultipartFile file);
    public ByteArrayInputStream downloadExcelData()throws IOException;
    public List<User> getAllUser();
    public User getUserById(int id);
    public User updateUserById(User user);
    public void deleteUserById(int id);
}
