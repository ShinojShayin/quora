package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignoutResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.common.UserRole;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserService userService;


    /**
     * signup method will create new user, this method will receive user input and once user is succefully created
     * it will sent back uuid of user along with success message
     *
     * @param signupUserRequest
     * @return SignupUserResponse
     * @throws SignUpRestrictedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/user/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> signup(SignupUserRequest signupUserRequest) throws SignUpRestrictedException {

        UserEntity reqUserEntity = new UserEntity();
        reqUserEntity.setFirstName(signupUserRequest.getFirstName());
        reqUserEntity.setLastName(signupUserRequest.getLastName());
        reqUserEntity.setUserName(signupUserRequest.getUserName());
        reqUserEntity.setEmail(signupUserRequest.getEmailAddress());
        reqUserEntity.setPassword(signupUserRequest.getPassword());
        reqUserEntity.setCountry(signupUserRequest.getCountry());
        reqUserEntity.setAboutMe(signupUserRequest.getAboutMe());
        reqUserEntity.setDob(signupUserRequest.getDob());
        reqUserEntity.setRole(UserRole.NON_ADMIN.getName());
        reqUserEntity.setContactNumber(signupUserRequest.getContactNumber());

        UserEntity respUserEntity = userService.signUp(reqUserEntity);
        SignupUserResponse signupUserResponse =
                new SignupUserResponse()
                        .id(respUserEntity.getUuid())
                        .status("USER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupUserResponse>(signupUserResponse, HttpStatus.CREATED);
    }


    /**
     * signin method will login the user, for that user need to provide user name and password
     * in following format "Basic username:password" where username:password of the String is encoded to Base64 format
     * in the authorization header. This method will decode this string and extract username and password and
     * pass it to service layer.
     *
     * @param authorization
     * @return SigninResponse
     * @throws AuthenticationFailedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/user/signin", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SigninResponse> signin(@RequestHeader("authorization") final String authorization)
            throws AuthenticationFailedException {

        byte[] decode = Base64.getDecoder().decode(authorization.split("Basic ")[1]);
        String decodedText = new String(decode);
        String[] decodedArray = decodedText.split(":");

        UserAuthEntity userAuthEntity = userService.signIn(decodedArray[0], decodedArray[1]);

        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", userAuthEntity.getAccessToken());

        SigninResponse signinResponse = new SigninResponse();
        signinResponse.setId(userAuthEntity.getUserEntity().getUuid());
        signinResponse.setMessage("SIGNED IN SUCCESSFULLY");

        return new ResponseEntity<SigninResponse>(signinResponse, headers, HttpStatus.OK);
    }

    /**
     * This method help to signout user based accesstoken/authorization
     *
     * @param acessToken
     * @return SignoutResponse
     * @throws SignOutRestrictedException
     */
    @RequestMapping(method = RequestMethod.POST, path = "/user/signout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignoutResponse> signout(@RequestHeader("authorization") final String acessToken) throws SignOutRestrictedException {
        UserEntity userEntity = userService.signOut(acessToken);
        SignoutResponse signoutResponse = new SignoutResponse().id(userEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
        return new ResponseEntity<SignoutResponse>(signoutResponse, HttpStatus.OK);
    }
}
