package com.example.dao;

import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public User findUserByEmail(String email) {
        try {
            User user = jdbcTemplate.queryForObject("select * from users where email=?", new Object[]{email}, new UserRowMapper());
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User findByName(String name) {
        try {
            User user = jdbcTemplate.queryForObject("select * from users where username=?", new Object[]{name}, new UserRowMapper());
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void addAuthority(String username, String role) {
        jdbcTemplate.update("insert into user_roles(user_id,role) " +
                "values((select id from users u where u.username=?),?)", username, role);
    }


    @Override
    public void save(User user) {
        jdbcTemplate.update("insert into users(username,password,enabled,email) values(?,?,?,?)",
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getEmail()
        );
    }

    @Override
    public void delete(String username) {
        jdbcTemplate.update("delete from users where username=?", username);
    }

    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEnabled(rs.getBoolean("enabled"));
            user.setEmail(rs.getString("email"));
            return user;
        }
    }
}
