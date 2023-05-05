package com.tanilsoo.citiesassignment;

import com.tanilsoo.citiesassignment.city.City;
import com.tanilsoo.citiesassignment.role.Role;
import com.tanilsoo.citiesassignment.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class Db {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public Db(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final int PAGE_SIZE = 20;

    public City findOne(int id) {
        String sql = "SELECT id, name, url FROM cities WHERE id=?";

        return jdbcTemplate.query(sql, preparedStatement -> {
            preparedStatement.setInt(1, id);
        }, resultSet -> {
            if (resultSet.next()) {
                return City.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .url(resultSet.getString("url"))
                        .build();
            }
            return null;
        });
    }
    public List<City> get(int page) {
        return get(page, null);
    }

    public List<City> get(int page, String keyword) {
        var search = keyword != null;

        List<City> cities = new ArrayList<>();

        var startIndex = page * PAGE_SIZE;
        var endIndex = startIndex + PAGE_SIZE;

        String sql = search ?
            "SELECT id, name, url FROM cities WHERE name LIKE ? LIMIT ?,?" :
            "SELECT id, name, url FROM cities LIMIT ?,?";

        jdbcTemplate.query(sql,
                ps -> {
                    if (search)
                        ps.setString(1, keyword + "%");
                    ps.setInt(search ? 2 : 1, startIndex);
                    ps.setInt(search ? 3 : 2, endIndex);
                },
                resultSet -> {
                    while (resultSet.next()) {
                        cities.add(
                            City.builder()
                                .id(resultSet.getInt(1))
                                .name(resultSet.getString(2))
                                .url(resultSet.getString(3))
                                .build()
                        );
                    }
                    return null;
                });
        return cities;
    }

    public void insertCity(City city){
        String query = "INSERT INTO cities VALUES (?,?,?)";
        jdbcTemplate.update(query, city.getId(), city.getName(), city.getUrl());
    }

    public void save(City city) {
        String query = "UPDATE cities SET name=?, url=? WHERE id=?";
        jdbcTemplate.update(query, ps -> {
            ps.setString(1, city.getName());
            ps.setString(2, city.getUrl());
            ps.setInt(3, city.getId());
        });
    }

    public User getUserByName(String username) {
        return jdbcTemplate.query("SELECT id, username, password FROM users WHERE username=?",
            ps -> {
                ps.setString(1, username);
            },
            resultSet -> {
                if (resultSet.next()) {
                    return User.builder()
                            .id(resultSet.getInt(1))
                            .username(resultSet.getString(2))
                            .password(resultSet.getString(3))
                            .build();
                }
                return null;
            });
    }

    public List<Role> getUserRoles2(int userId) {
        List<Role> roles = new ArrayList<>();
        jdbcTemplate.query("SELECT r.id, r.name FROM users u JOIN users_roles ur ON u.id = ur.user_id JOIN roles r ON ur.role_id = r.id WHERE u.id=?",
                ps -> {
                    ps.setInt(1, userId);
                },
                resultSet -> {
                    while (resultSet.next()) {
                        roles.add(Role.builder()
                                .id(resultSet.getInt(1))
                                .name(resultSet.getString(2))
                                .build()
                        );
                    }
                    return null;
                });
        return roles;
    }

    public boolean hasCityData() {
        return jdbcTemplate.query("SELECT COUNT(*) FROM cities", result -> {
            result.next();
            return result.getInt(1) > 0;
        });
    }
}
