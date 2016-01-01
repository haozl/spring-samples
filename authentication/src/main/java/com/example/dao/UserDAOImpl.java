package com.example.dao;

import com.example.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public UserInfo findByName(String username) {
        UserInfo user;

        try {
            user = jdbcTemplate.queryForObject("select id, username, password from users where enabled=1 and username=?",
                    new Object[]{username}, new RowMapper<UserInfo>() {
                        @Override
                        public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                            UserInfo user = new UserInfo();
                            user.setId(rs.getLong("id"));
                            user.setUsername(rs.getString("username"));
                            user.setPassword(rs.getString("password"));
                            return user;
                        }
                    });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        List<String> roles = jdbcTemplate.queryForList("select role from user_roles where user_id=?", new Object[]{user.getId()}, String.class);
        user.setAuthorities(roles);

        return user;
    }
}
