package com.springboot.HomeController;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.Entity.EncryptedRequest;
import com.springboot.Service.CyptroUtil;

import org.apache.poi.ss.usermodel.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.Entity.User;
import com.springboot.Helper.MyExcelHalper;
import com.springboot.Service.UserServiceImp;


@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserServiceImp service;

    @PostMapping("/User")
    public ResponseEntity<?> saveUser(@RequestBody EncryptedRequest encryptedRequest) throws Exception {
        String encryptedData = encryptedRequest.getEncryptedData();
        String decryptedData = CyptroUtil.decrypt(encryptedData);
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(decryptedData, User.class);
        User saveUser = service.saveUser(user);
        return new ResponseEntity<>(saveUser,HttpStatus.CREATED);
    }
    
    @PostMapping("/Users")
    public ResponseEntity<?> saveUsers(@RequestBody User user){
    	User saveUser = service.saveUser(user);
    	return new ResponseEntity<User>(saveUser,HttpStatus.CREATED);
    }
    

    @GetMapping("/User/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int id) throws Exception {
        User user = service.getUserById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = objectMapper.writeValueAsString(user);
        String encryptObject = CyptroUtil.encrypt(jsonObject);
        //System.out.println("Update: "+ encryptObject);
        return new ResponseEntity<String>(encryptObject,HttpStatus.OK);
    }
    @GetMapping("/Users/{id}")
    public ResponseEntity<?> getUsersById(@PathVariable("id") int id){
    	User user = service.getUserById(id);
    	return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    @GetMapping("/User")
    public ResponseEntity<?> getAllUser() throws Exception {
        List<User> user  = service.getAllUser();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonObject = objectMapper.writeValueAsString(user);
        //System.out.println(jsonObject);
        String encryptData = CyptroUtil.encrypt(jsonObject);
        //System.out.println("EncryptData: "+ encryptData);
        return new ResponseEntity<String>(encryptData,HttpStatus.OK);
    }
    
    @GetMapping("/Users")
    public ResponseEntity<?> getAllUsers(){
     	List<User> users = service.getAllUser();
     	return new ResponseEntity<List<User>>(users,HttpStatus.OK);
    }

    @PutMapping("/User/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id, @RequestBody EncryptedRequest encryptedRequest) throws Exception {
        String encryptedData = encryptedRequest.getEncryptedData();
        String decryptedData = CyptroUtil.decrypt(encryptedData);
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(decryptedData,User.class);
        user.setId(id);
        User updateUser = service.updateUserById(user);
        return new ResponseEntity<User>(updateUser,HttpStatus.OK);
    }
    
    @PutMapping("/Users/{id}")
    public ResponseEntity<?> updateUsers(@PathVariable("id") int id, @RequestBody User user){
    	user.setId(id);
    	User updateUser = service.updateUserById(user);
    	return new ResponseEntity<User>(updateUser, HttpStatus.CREATED);
    }

    @DeleteMapping("User/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id){
         service.deleteUserById(id);
        return new ResponseEntity<>("Delete User Successfully",HttpStatus.OK);
    }
    
    @PostMapping("/Users/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
    	if(MyExcelHalper.checkExcelFromat(file)) {
    		service.saveExcelData(file);
    		return ResponseEntity.ok(Map.of("message","File is uploaded and data in save succcessfully"));
    	}
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file upload");
    }
    
    @GetMapping("/Download")
    public ResponseEntity<Resource> downloadExcelData() throws IOException{
    	String filename = "Users.xlsx";
    	ByteArrayInputStream actualData = service.downloadExcelData();
    	InputStreamResource file = new InputStreamResource(actualData);
    	ResponseEntity<Resource> body = ResponseEntity.ok()
    			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filename)
    			.contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
    			.body(file);
    	return body;
    }






}
