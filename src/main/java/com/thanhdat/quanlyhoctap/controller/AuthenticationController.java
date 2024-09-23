package com.thanhdat.quanlyhoctap.controller;

import com.nimbusds.jose.JOSEException;
import com.thanhdat.quanlyhoctap.dto.request.AuthenticationRequest;
import com.thanhdat.quanlyhoctap.dto.request.IntrospectRequest;
import com.thanhdat.quanlyhoctap.dto.response.ApiResponse;
import com.thanhdat.quanlyhoctap.dto.response.AuthenticationResponse;
import com.thanhdat.quanlyhoctap.dto.response.IntrospectResponse;
import com.thanhdat.quanlyhoctap.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authRequest) {
            return ApiResponse.<AuthenticationResponse>builder()
                    .result(authenticationService.authenticate(authRequest))
                    .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(introspectRequest))
                .build();
    }
}
