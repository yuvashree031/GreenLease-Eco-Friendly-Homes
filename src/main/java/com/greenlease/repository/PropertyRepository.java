package com.greenlease.repository;

import com.greenlease.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JDBC Repository for Property entity
 */
@Repository
public class PropertyRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Property> propertyRowMapper = new RowMapper<Property>() {
        @Override
        public Property mapRow(ResultSet rs, int rowNum) throws SQLException {
            Property property = new Property();
            property.setId(rs.getLong("id"));
            property.setTitle(rs.getString("title"));
            property.setDescription(rs.getString("description"));
            property.setAddress(rs.getString("address"));
            property.setCity(rs.getString("city"));
            property.setState(rs.getString("state"));
            property.setZipCode(rs.getString("zip_code"));
            property.setRent(rs.getBigDecimal("rent"));
            property.setPropertyType(rs.getString("property_type"));
            property.setBedrooms(rs.getInt("bedrooms"));
            property.setBathrooms(rs.getInt("bathrooms"));
            property.setSquareFootage(rs.getDouble("square_footage"));
            property.setInsulationRating(rs.getInt("insulation_rating"));
            property.setSolarPanels(rs.getBoolean("solar_panels"));
            property.setSolarRating(rs.getInt("solar_rating"));
            property.setWaterConservationRating(rs.getInt("water_conservation_rating"));
            property.setGreenSpaceProximity(rs.getDouble("green_space_proximity"));
            property.setEnergyEfficiencyRating(rs.getInt("energy_efficiency_rating"));
            property.setOverallEcoScore(rs.getDouble("overall_eco_score"));
            property.setLandlordId(rs.getLong("landlord_id"));
            property.setImageUrl(rs.getString("image_url"));
            property.setIsAvailable(rs.getBoolean("is_available"));
            property.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            property.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return property;
        }
    };
    
    public List<Property> findAll() {
        String sql = "SELECT * FROM properties ORDER BY overall_eco_score DESC, created_at DESC";
        return jdbcTemplate.query(sql, propertyRowMapper);
    }
    
    public List<Property> findAvailable() {
        String sql = "SELECT * FROM properties WHERE is_available = true ORDER BY overall_eco_score DESC";
        return jdbcTemplate.query(sql, propertyRowMapper);
    }
    
    public Optional<Property> findById(Long id) {
        String sql = "SELECT * FROM properties WHERE id = ?";
        List<Property> properties = jdbcTemplate.query(sql, propertyRowMapper, id);
        return properties.isEmpty() ? Optional.empty() : Optional.of(properties.get(0));
    }
    
    public List<Property> findByCity(String city) {
        String sql = "SELECT * FROM properties WHERE LOWER(city) LIKE LOWER(?) AND is_available = true ORDER BY overall_eco_score DESC";
        return jdbcTemplate.query(sql, propertyRowMapper, "%" + city + "%");
    }
    
    public List<Property> findByEcoScoreRange(double minScore, double maxScore) {
        String sql = "SELECT * FROM properties WHERE overall_eco_score BETWEEN ? AND ? AND is_available = true ORDER BY overall_eco_score DESC";
        return jdbcTemplate.query(sql, propertyRowMapper, minScore, maxScore);
    }
    
    public List<Property> findByRentRange(BigDecimal minRent, BigDecimal maxRent) {
        String sql = "SELECT * FROM properties WHERE rent BETWEEN ? AND ? AND is_available = true ORDER BY overall_eco_score DESC";
        return jdbcTemplate.query(sql, propertyRowMapper, minRent, maxRent);
    }
    
    public List<Property> findBySolarPanels(boolean hasSolar) {
        String sql = "SELECT * FROM properties WHERE solar_panels = ? AND is_available = true ORDER BY overall_eco_score DESC";
        return jdbcTemplate.query(sql, propertyRowMapper, hasSolar);
    }
    
    public Property save(Property property) {
        if (property.getId() == null) {
            return insert(property);
        } else {
            return update(property);
        }
    }
    
    private Property insert(Property property) {
        String sql = """
            INSERT INTO properties (title, description, address, city, state, zip_code, rent, 
                                  property_type, bedrooms, bathrooms, square_footage, 
                                  insulation_rating, solar_panels, solar_rating, 
                                  water_conservation_rating, green_space_proximity, 
                                  energy_efficiency_rating, overall_eco_score, 
                                  landlord_id, image_url, is_available, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, property.getTitle());
            ps.setString(2, property.getDescription());
            ps.setString(3, property.getAddress());
            ps.setString(4, property.getCity());
            ps.setString(5, property.getState());
            ps.setString(6, property.getZipCode());
            ps.setBigDecimal(7, property.getRent());
            ps.setString(8, property.getPropertyType());
            ps.setInt(9, property.getBedrooms() != null ? property.getBedrooms() : 0);
            ps.setInt(10, property.getBathrooms() != null ? property.getBathrooms() : 0);
            ps.setDouble(11, property.getSquareFootage() != null ? property.getSquareFootage() : 0.0);
            ps.setInt(12, property.getInsulationRating() != null ? property.getInsulationRating() : 0);
            ps.setBoolean(13, property.getSolarPanels() != null ? property.getSolarPanels() : false);
            ps.setInt(14, property.getSolarRating() != null ? property.getSolarRating() : 0);
            ps.setInt(15, property.getWaterConservationRating() != null ? property.getWaterConservationRating() : 0);
            ps.setDouble(16, property.getGreenSpaceProximity() != null ? property.getGreenSpaceProximity() : 0.0);
            ps.setInt(17, property.getEnergyEfficiencyRating() != null ? property.getEnergyEfficiencyRating() : 0);
            ps.setDouble(18, property.getOverallEcoScore() != null ? property.getOverallEcoScore() : 0.0);
            ps.setLong(19, property.getLandlordId() != null ? property.getLandlordId() : 1L);
            ps.setString(20, property.getImageUrl());
            ps.setBoolean(21, property.getIsAvailable() != null ? property.getIsAvailable() : true);
            ps.setTimestamp(22, Timestamp.valueOf(now));
            ps.setTimestamp(23, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);
        
        property.setId(keyHolder.getKey().longValue());
        property.setCreatedAt(now);
        property.setUpdatedAt(now);
        
        return property;
    }
    
    private Property update(Property property) {
        String sql = """
            UPDATE properties SET title = ?, description = ?, address = ?, city = ?, state = ?, 
                                zip_code = ?, rent = ?, property_type = ?, bedrooms = ?, 
                                bathrooms = ?, square_footage = ?, insulation_rating = ?, 
                                solar_panels = ?, solar_rating = ?, water_conservation_rating = ?, 
                                green_space_proximity = ?, energy_efficiency_rating = ?, 
                                overall_eco_score = ?, landlord_id = ?, image_url = ?, 
                                is_available = ?, updated_at = ?
            WHERE id = ?
            """;
        
        LocalDateTime now = LocalDateTime.now();
        
        jdbcTemplate.update(sql,
            property.getTitle(),
            property.getDescription(),
            property.getAddress(),
            property.getCity(),
            property.getState(),
            property.getZipCode(),
            property.getRent(),
            property.getPropertyType(),
            property.getBedrooms(),
            property.getBathrooms(),
            property.getSquareFootage(),
            property.getInsulationRating(),
            property.getSolarPanels(),
            property.getSolarRating(),
            property.getWaterConservationRating(),
            property.getGreenSpaceProximity(),
            property.getEnergyEfficiencyRating(),
            property.getOverallEcoScore(),
            property.getLandlordId(),
            property.getImageUrl(),
            property.getIsAvailable(),
            Timestamp.valueOf(now),
            property.getId()
        );
        
        property.setUpdatedAt(now);
        return property;
    }
    
    public void deleteById(Long id) {
        String sql = "DELETE FROM properties WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    public long count() {
        String sql = "SELECT COUNT(*) FROM properties";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    
    public double getAverageEcoScore() {
        String sql = "SELECT AVG(overall_eco_score) FROM properties WHERE overall_eco_score > 0";
        Double result = jdbcTemplate.queryForObject(sql, Double.class);
        return result != null ? result : 0.0;
    }
}