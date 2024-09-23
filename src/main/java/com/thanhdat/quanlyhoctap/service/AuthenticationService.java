package com.thanhdat.quanlyhoctap.service;

import com.nimbusds.jose.JOSEException;
import com.thanhdat.quanlyhoctap.dto.request.AuthenticationRequest;
import com.thanhdat.quanlyhoctap.dto.request.IntrospectRequest;
import com.thanhdat.quanlyhoctap.dto.response.AuthenticationResponse;
import com.thanhdat.quanlyhoctap.dto.response.IntrospectResponse;

import java.text.ParseException;

public interface AuthenticationService {
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws JOSEException, ParseException;
    AuthenticationResponse authenticate(AuthenticationRequest authRequest);
}
