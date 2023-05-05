package com.tanilsoo.citiesassignment.user;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {

    private int id;

    private String username;

    private String password;

}
