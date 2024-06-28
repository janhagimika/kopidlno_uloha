package com.example.kopidlno.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveObec(Obec obec) {
        String sql = "INSERT INTO obec (kod, nazev) VALUES (?, ?) ON CONFLICT (kod) DO NOTHING";
        jdbcTemplate.update(sql, obec.getKod(), obec.getNazev());
    }

    public void saveCastObce(CastObce castObce) {
        String sql = "INSERT INTO cast_obce (kod, nazev, obec_kod) VALUES (?, ?, ?) ON CONFLICT (kod) DO NOTHING";
        jdbcTemplate.update(sql, castObce.getKod(), castObce.getNazev(), castObce.getObecKod());
    }

    public List<Obec> getAllObec() {
        String sql = "SELECT * FROM obec";
        return jdbcTemplate.query(sql, new ObecRowMapper());
    }

    private static class ObecRowMapper implements RowMapper<Obec> {
        @Override
        public Obec mapRow(ResultSet rs, int rowNum) throws SQLException {
            Obec obec = new Obec();
            obec.setKod(rs.getLong("kod"));
            obec.setNazev(rs.getString("nazev"));
            return obec;
        }
    }

    public List<CastObce> getAllCastObce() {
        String sql = "SELECT * FROM cast_obce";
        return jdbcTemplate.query(sql, new CastObceRowMapper());
    }

    private static class CastObceRowMapper implements RowMapper<CastObce> {
        @Override
        public CastObce mapRow(ResultSet rs, int rowNum) throws SQLException {
            CastObce castObce = new CastObce();
            castObce.setKod(rs.getLong("kod"));
            castObce.setNazev(rs.getString("nazev"));
            castObce.setObecKod(rs.getLong("obec_kod"));
            return castObce;
        }
    }
}
