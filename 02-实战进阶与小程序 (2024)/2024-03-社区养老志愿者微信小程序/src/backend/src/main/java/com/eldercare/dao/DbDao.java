package com.eldercare.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用数据访问层: 基于 JdbcTemplate 的 Map 结果集封装
 */
@Repository
public class DbDao {

    private final JdbcTemplate jdbc;

    public DbDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private static final RowMapper<Map<String, Object>> MAP_ROW = (rs, rowNum) -> mapRow(rs);

    private static Map<String, Object> mapRow(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int n = meta.getColumnCount();
        Map<String, Object> row = new LinkedHashMap<>();
        for (int i = 1; i <= n; i++) {
            String col = meta.getColumnLabel(i);
            Object val = rs.getObject(i);
            if (val instanceof Timestamp) {
                val = ((Timestamp) val).toLocalDateTime().toString().replace('T', ' ');
            } else if (val instanceof BigDecimal) {
                val = ((BigDecimal) val).doubleValue();
            }
            row.put(col, val);
        }
        return row;
    }

    public List<Map<String, Object>> query(String sql, Object... args) {
        return jdbc.query(sql, MAP_ROW, args);
    }

    public Map<String, Object> queryOne(String sql, Object... args) {
        List<Map<String, Object>> list = query(sql, args);
        return list.isEmpty() ? null : list.get(0);
    }

    public long count(String sql, Object... args) {
        Long c = jdbc.queryForObject(sql, Long.class, args);
        return c == null ? 0L : c;
    }

    public int update(String sql, Object... args) {
        return jdbc.update(sql, args);
    }

    /** 插入并返回自增主键 */
    public long insert(String sql, Object... args) {
        jdbc.update(sql, args);
        Long id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return id == null ? 0L : id;
    }

    public Object scalar(String sql, Object... args) {
        return jdbc.queryForObject(sql, Object.class, args);
    }

    /** 简单"WHERE 1=1"动态条件构建 */
    public static class SqlBuilder {
        private final StringBuilder sb;
        private final List<Object> args = new ArrayList<>();

        public SqlBuilder(String base) {
            this.sb = new StringBuilder(base);
        }

        public SqlBuilder and(String cond, Object... vals) {
            sb.append(" AND ").append(cond);
            for (Object v : vals) {
                args.add(v);
            }
            return this;
        }

        public SqlBuilder append(String fragment) {
            sb.append(fragment);
            return this;
        }

        public SqlBuilder orderBy(String orderBy) {
            sb.append(" ORDER BY ").append(orderBy);
            return this;
        }

        public SqlBuilder limit(int limit, int offset) {
            sb.append(" LIMIT ").append(limit).append(" OFFSET ").append(offset);
            return this;
        }

        public String sql() {
            return sb.toString();
        }

        public Object[] args() {
            return args.toArray();
        }
    }

    /** 为列表项补充展示字段 */
    public static Map<String, Object> newMap() {
        return new HashMap<>();
    }
}
