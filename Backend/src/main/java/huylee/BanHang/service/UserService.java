package huylee.BanHang.service;

import huylee.BanHang.dtos.UserDTO;
import huylee.BanHang.entity.User;

public interface UserService {
    User createUser(UserDTO userDTO);
    String login(String phoneNumber, String password);
}
