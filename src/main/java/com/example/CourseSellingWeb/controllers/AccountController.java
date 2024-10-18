package com.example.CourseSellingWeb.controllers;

import com.example.CourseSellingWeb.components.LocalizationUtils;
import com.example.CourseSellingWeb.dtos.AccountAndUserDTO;
import com.example.CourseSellingWeb.dtos.AccountLoginDTO;
import com.example.CourseSellingWeb.exceptions.DataNotFoundException;
import com.example.CourseSellingWeb.exceptions.InvalidParamException;
import com.example.CourseSellingWeb.exceptions.InvalidPasswordException;
import com.example.CourseSellingWeb.models.Account;
import com.example.CourseSellingWeb.models.AccountAndUser;
import com.example.CourseSellingWeb.responses.LoginMessageResponse;
import com.example.CourseSellingWeb.responses.ResponseObject;
import com.example.CourseSellingWeb.responses.messages.RegisterMessageResponse;
import com.example.CourseSellingWeb.services.account.AccountServiceImpl;
import com.example.CourseSellingWeb.services.email.EmailSenderServiceImpl;
import com.example.CourseSellingWeb.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accountService;
    private final EmailSenderServiceImpl senderService;
    private final LocalizationUtils localizationUtils;
//    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/register")
    public ResponseEntity<RegisterMessageResponse> create (
            @Valid @RequestBody AccountAndUserDTO accountAndUserDTO,
            BindingResult result
    ){
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(RegisterMessageResponse.builder()
                        .message(localizationUtils.getLocalizationMessage(MessageKeys.REGISTER_FAILED,errorMessages))
                        .build());

            }

            if(!accountAndUserDTO.getAccountDTO().getPassword().equals(accountAndUserDTO.getAccountDTO().getRetypePassword())){
                return ResponseEntity.badRequest().body(RegisterMessageResponse.builder()
                        .message(localizationUtils.getLocalizationMessage(MessageKeys.PASSWORD_NOT_MATCH))
                        .build());
            }
            AccountAndUser accountAndUser = accountService.register(accountAndUserDTO);
            return  ResponseEntity.ok(RegisterMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.REGISTER_SUCCESSFULLY))
                    .accountAndUser(accountAndUser)
                    .build());
        }catch(Exception e){
            return  ResponseEntity.badRequest().body(RegisterMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.REGISTER_FAILED,e.getMessage()))
                    .build());
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginMessageResponse> login(
//            @Valid @RequestBody AccountLoginDTO accountLoginDTO
//            ) throws InvalidParamException{
//        try {
//            String token = accountService.login(accountLoginDTO.getUsername(),accountLoginDTO.getPassword(),
//                    String.valueOf(accountLoginDTO.getRoleId()) == null ? 1 : accountLoginDTO.getRoleId());
//            return ResponseEntity.ok(LoginMessageResponse.builder()
//                            .message(localizationUtils.getLocalizationMessage(MessageKeys.LOGIN_SUCCESSFULLY))
//                            .token(token)
//                    .build());
//        }catch(Exception e){
//            return ResponseEntity.ok(LoginMessageResponse.builder()
//                    .message(localizationUtils.getLocalizationMessage(MessageKeys.LOGIN_FAILED, e.getMessage()))
//                    .build());
//        }
//    }
    @PostMapping("/login")
    public ResponseEntity<LoginMessageResponse> login(
            @Valid @RequestBody AccountLoginDTO accountLoginDTO
    ) throws InvalidParamException{
        try {
            String token = accountService.login(accountLoginDTO.getUsername(), accountLoginDTO.getPassword());
            return ResponseEntity.ok(LoginMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(localizationUtils.getLocalizationMessage(MessageKeys.LOGIN_SUCCESSFULLY)))
                    .token(token)
                    .build());
        } catch(Exception e) {
            return ResponseEntity.ok(LoginMessageResponse.builder()
                    .message(localizationUtils.getLocalizationMessage(localizationUtils.getLocalizationMessage(MessageKeys.LOGIN_FAILED, e.getMessage())))
                    .build());
        }
    }

    @PutMapping("/reset-password/{email}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> resetPassword(@Valid @PathVariable String email) throws DataNotFoundException, InvalidPasswordException {
        try {
            String newPassword = UUID.randomUUID().toString().substring(0, 6); // Create new password
            accountService.resetPassword(email, newPassword);
            String subject = localizationUtils.getLocalizationMessage(MessageKeys.SEND_EMAIL_NEW_PASSWORD);
            senderService.senderEmail(email,subject,newPassword);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.RESET_PASSWORD_SUCCESSFULLY))
                    .data(newPassword)
                    .status(HttpStatus.OK)
                    .build());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.PASSWORD_NOT_MATCH))
                    .data("")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizationMessage(MessageKeys.NOT_FOUND) )
                    .data("")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }


    @PostMapping("/details")
    public ResponseEntity<ResponseObject> getUserDetailsFromToken(@RequestHeader("Authorization") String authorizationHeader){
        try {
            String extractedToken = authorizationHeader.substring(7);
            Account account = accountService.getAccountDetailsFromToken(extractedToken);
            return ResponseEntity.ok(ResponseObject.builder()
                            .data(account)
                            .status(HttpStatus.OK)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
