package com.tanilsoo.citiesassignment.user;

import com.tanilsoo.citiesassignment.Db;
import com.tanilsoo.citiesassignment.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final Db db;

    @Autowired
    UserService(Db db) {
        this.db = db;
    }

    public User getUserByUsername(String username) {
        return this.db.getUserByName(username);
    }

    public List<Role> getUserRoles(int userId) {
        return this.db.getUserRoles2(userId);
    }
}
