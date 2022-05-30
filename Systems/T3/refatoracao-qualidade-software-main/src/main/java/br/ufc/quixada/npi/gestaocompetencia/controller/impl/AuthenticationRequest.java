package br.ufc.quixada.npi.gestaocompetencia.controller.impl;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

public class AuthenticationRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private String email;
    private String password;

    public AuthenticationRequest() {

    }

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @ApiModelProperty(hidden = true)
    public String getUsername() {
        return getEmail();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
