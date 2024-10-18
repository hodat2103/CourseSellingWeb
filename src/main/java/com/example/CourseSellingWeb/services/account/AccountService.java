package com.example.CourseSellingWeb.services.account;

import com.example.CourseSellingWeb.components.JwtTokenUtils;
import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.AccountAndUserDTO;
import com.example.CourseSellingWeb.dtos.AccountDTO;
import com.example.CourseSellingWeb.dtos.UserDTO;
import com.example.CourseSellingWeb.exceptions.*;
import com.example.CourseSellingWeb.models.*;
import com.example.CourseSellingWeb.respositories.AccountRepository;
import com.example.CourseSellingWeb.respositories.RoleRepository;
import com.example.CourseSellingWeb.respositories.TokenRepository;
import com.example.CourseSellingWeb.respositories.UserRepository;
import com.example.CourseSellingWeb.utils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountServiceImpl {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final LocalizationUtils localizationUtils;


    @Override
    public AccountAndUser register(AccountAndUserDTO accountAndUserDTO) throws Exception{
        AccountDTO accountDTO = accountAndUserDTO.getAccountDTO();
        UserDTO userDTO = accountAndUserDTO.getUserDTO();
        String phoneNumber = accountDTO.getUsername();
        if(accountRepository.existsByUsername(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(accountDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException(localizationUtils.getLocalizationMessage(MessageKeys.NOT_FOUND)));
        if(role.getName().toUpperCase().equals(Role.ADMIN)){
            throw new PermissionDenyException("Cannot register a admin account");
        }
        Account newAccount  = Account.builder()
                .username(accountDTO.getUsername())
                .password(accountDTO.getPassword())
                .email(accountDTO.getEmail())
                .role(role)
                .active(true)
                .build();
        String password = accountDTO.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        newAccount.setPassword(encodePassword);
        Account account = accountRepository.save(newAccount);
        User newUser = new User();
        if(newAccount.getRole().getName().equals("user")){

            newUser = User.builder()
                    .name(userDTO.getName())
                    .email(userDTO.getEmail())
                    .phone(userDTO.getPhone())
                    .address(userDTO.getAddress())
                    .account(newAccount)
                    .build();
        }

        return new AccountAndUser(newAccount,newUser);
    }

    @Override
    public String login(String username, String password) throws DataNotFoundException, InvalidParamException {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (optionalAccount.isEmpty()) {
            throw new DataNotFoundException(localizationUtils.getLocalizationMessage(MessageKeys.PASSWORD_NOT_MATCH));
        }
        Account existingAccount = optionalAccount.get();

        if (!existingAccount.isActive()) {
            throw new DataNotFoundException(localizationUtils.getLocalizationMessage(MessageKeys.ACCOUNT_IS_LOCKED));
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password, existingAccount.getAuthorities()
        );

        // Authenticate user with Spring Security
        authenticationManager.authenticate(authenticationToken);

        return jwtTokenUtils.generateToken(existingAccount);
    }
    @Override
    @Transactional
    public String resetPassword(String email, String newPassword)
            throws InvalidPasswordException, DataNotFoundException {
        User existingUser = userRepository.findByEmail(email);
        Optional<Account> existsAccount = accountRepository.findById(existingUser.getAccount().getId());
        if(existingUser == null){
            throw new DataNotFoundException(localizationUtils.getLocalizationMessage(MessageKeys.NOT_FOUND)+existingUser.getId() );
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        existsAccount.get().setPassword(encodedPassword);
        accountRepository.save(existsAccount.get());
        //reset password => clear token
        List<Token> tokens = tokenRepository.findByAccount(existsAccount.get());
        for (Token token : tokens) {
            tokenRepository.delete(token);
        }
        return email;
    }



    @Override
    public void updatePassword(String newPassword, Integer accountId) throws DataNotFoundException {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new DataNotFoundException(localizationUtils.getLocalizationMessage(MessageKeys.NOT_FOUND) + accountId);
        }

        int updatedRows = accountRepository.updatePassword(newPassword, accountId);
        if (updatedRows == 0) {
            throw new RuntimeException(localizationUtils.getLocalizationMessage(MessageKeys.UPDATE_PASSWORD_FAILED) + accountId);
        }
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountDetailsFromToken(String extractedToken) throws Exception {
        if(jwtTokenUtils.isTokenExpired(extractedToken)){
            throw new Exception(localizationUtils.getLocalizationMessage(MessageKeys.TOKEN_EXPIRED));
        }
        String phoneNumber = jwtTokenUtils.extractUsername(extractedToken);
        Optional<Account> account = accountRepository.findByUsername(phoneNumber);
        if(account.isPresent()){
            return account.get();
        }else{
            throw new Exception(localizationUtils.getLocalizationMessage(MessageKeys.NOT_FOUND));
        }
    }



}
