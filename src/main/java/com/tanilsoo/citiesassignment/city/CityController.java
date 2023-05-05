package com.tanilsoo.citiesassignment.city;

import com.tanilsoo.citiesassignment.exception.EntityNotFoundException;
import com.tanilsoo.citiesassignment.exception.InvalidValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/city")
public class CityController {

    private final CityService cityService;

    private static final int MAX_NAME_CHARS = 255;
    private static final int MAX_URL_CHARS = 2048;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<CityDto>> get(@RequestParam(required = false) Integer page, @RequestParam(required = false) String search) {
        if (page == null) {
            page = 0;
        }

        var data = cityService.get(page, search).stream().map(city ->
                CityDto.builder()
                        .id(city.getId())
                        .name(Optional.of(city.getName()))
                        .url(Optional.of(city.getUrl()))
                        .build()
        ).toList();

        return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CityDto> patch(@PathVariable int id, @RequestBody CityDto dto) throws InvalidValueException, EntityNotFoundException {
        if (dto.getName().isPresent()) {
            var name = dto.getName().orElseThrow(() -> new InvalidValueException("Null not accepted"));

            if (name.length() > MAX_NAME_CHARS) { // add const
                throw new InvalidValueException("Exceeds max length");
            }
        }

        if (dto.getUrl().isPresent()) {
            var imgUrl = dto.getUrl()
                    .orElseThrow(() -> new InvalidValueException("Null not accepted"));

            if (imgUrl.length() > MAX_URL_CHARS) {
                throw new InvalidValueException("Exceeds max length");
            }
        }

        var city = cityService.patch(id, dto);

        return new ResponseEntity<>(
                CityDto.builder()
                        .id(city.getId())
                        .name(Optional.of(city.getName()))
                        .url(Optional.of(city.getUrl()))
                        .build()
                , HttpStatus.ACCEPTED);
    }

}
