package com.example.CourseSellingWeb.services.user;

import com.example.CourseSellingWeb.dtos.UserDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.models.Account;
import com.example.CourseSellingWeb.models.Course;
import com.example.CourseSellingWeb.models.User;
import com.example.CourseSellingWeb.responses.course.CourseResponse;
import com.example.CourseSellingWeb.responses.user.UserResponse;
import com.example.CourseSellingWeb.respositories.AccountRepository;
import com.example.CourseSellingWeb.respositories.UserRepository;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceImpl {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    @Override
    public User create(UserDTO userDTO) throws DataNotFoundException {
        Account existsAccount = accountRepository.findById(userDTO.getAccountId())
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + userDTO.getAccountId()));
        User newUser = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .address(userDTO.getAddress())
                .account(existsAccount)
                .build();

        return userRepository.save(newUser);
    }

    @Override
    public User update(int id, UserDTO userDTO) throws DataNotFoundException {
        User existsUser = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));
//        Account existsAccount = accountRepository.findById(userDTO.getAccountId())
//                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + userDTO.getAccountId()));
        existsUser.setName(userDTO.getName());
        existsUser.setEmail(userDTO.getEmail());
        existsUser.setPhone(userDTO.getPhone());
        existsUser.setAddress(userDTO.getAddress());

        userRepository.save(existsUser);
        return existsUser;
    }

    @Override
    public Optional<User> getById(int id) throws DataNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.NOT_FOUND + id));

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getByAccountId(int accountId) throws DataNotFoundException {
        User user = userRepository.findByAccountId(accountId);
        return Optional.ofNullable(user);
    }

    @Override
    public Page<UserResponse> searchUsers(String keyword, Pageable pageable) {
        Page<User> userPage;
        userPage = userRepository.searchUsers(keyword,pageable);
        return userPage.map(UserResponse:: fromUser);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void delete(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresent(userRepository :: delete);
    }
}
