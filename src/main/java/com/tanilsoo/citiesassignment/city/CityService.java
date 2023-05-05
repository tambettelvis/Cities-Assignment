package com.tanilsoo.citiesassignment.city;

import com.tanilsoo.citiesassignment.Db;
import com.tanilsoo.citiesassignment.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    public final Db db;

    @Autowired
    public CityService(Db db) {
        this.db = db;
    }

    public List<City> get(int page, String search) {
        return db.get(page, search);
    }

    public City patch(int id, CityDto dto) throws EntityNotFoundException {
        City city = db.findOne(id);

        if (city == null) {
            throw new EntityNotFoundException("City not found");
        }

        if (dto.getName().isPresent()) {
            city.setName(dto.getName().get());
        }

        if (dto.getUrl().isPresent()) {
            city.setUrl(dto.getUrl().get());
        }

        db.save(city);

        return city;
    }

}
