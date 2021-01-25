package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    CommonService commonService;

    /**
     *
     *
     * @param userUuid
     * @param authorization
     * @return UserDetailsResponse
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUserProfile(@PathVariable("userId") final String userUuid,
            @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        // Verify 'authorization' in header is valid or not, if not valid it will throw exception
        UserAuthEntity userAuth = commonService.validateUser(authorization);

        UserEntity user = commonService.getUserByUuid(userUuid);

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
        userDetailsResponse
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emailAddress(user.getEmail())
                .country(user.getCountry())
                .aboutMe(user.getAboutMe())
                .dob(user.getDob())
                .contactNumber(user.getContactNumber());

        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
