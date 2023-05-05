package com.tanilsoo.citiesassignment.city;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class City {

    private int id;
    private String name;
    private String url;

}
