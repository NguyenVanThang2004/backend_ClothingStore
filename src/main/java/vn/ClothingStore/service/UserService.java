package vn.ClothingStore.service;

import org.springframework.stereotype.Service;
import vn.ClothingStore.domain.User;
import vn.ClothingStore.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository ;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUser(){
        return this.userRepository.findAll();
    }

    public User insertUser(User user){

        User user1 = new User();

        user1.setUsername(user.getUsername());
        user1.setActive(user.isActive());
        user1.setPassword(user.getPassword());

        return this.userRepository.save(user1);

    }

    public User GetUserByUsername(String username){
        return this.userRepository.findByUsername(username);
    }






}
