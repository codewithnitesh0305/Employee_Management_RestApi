package com.springboot.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.Entity.User;
import com.springboot.Helper.MyExcelHalper;
import com.springboot.Repository.UserRepository;

@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository repository;
    @Override
    public User saveUser(User user) {
        // TODO Auto-generated method stub
        return repository.save(user);
    }
    @Override
    public List<User> getAllUser() {
        // TODO Auto-generated method stub
       List<User> list = repository.findAllUserByNativQuery();
       return list;
    }
    @Override
    public User getUserById(int id) {
        // TODO Auto-generated method stub
       User user = repository.findUserByNativQuery(id);
       return user;
    }
    @Override
    public User updateUserById(User user) {
        // TODO Auto-generated method stub
        return repository.save(user);
        
    }
    @Override
    public void deleteUserById(int id) {
        // TODO Auto-generated method stub
       repository.deleteById(id);
    }
	@Override
	public void saveExcelData(MultipartFile file) {
		// TODO Auto-generated method stub
		try {
			List<User> users = MyExcelHalper.convertExcelToListOfProduct(file.getInputStream());
			repository.saveAll(users);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public ByteArrayInputStream downloadExcelData() throws IOException {
		// TODO Auto-generated method stub
		List<User> user = repository.findAll();
		ByteArrayInputStream dataToExcel = MyExcelHalper.dataToExcel(user);
		return dataToExcel;
	}
	
	

}
