package com.tanilsoo.citiesassignment.city;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

@Getter
@Jacksonized
@Builder
public class CityDto {

    private int id;

    @Builder.Default
    private Optional<String> name = Optional.empty();
    @Builder.Default
    private Optional<String> url = Optional.empty();

}
