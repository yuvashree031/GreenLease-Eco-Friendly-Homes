package com.greenlease.repository;

import com.greenlease.model.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * JDBC Repository for Feedback entity
 */
@Repository
public class FeedbackRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Feedback> feedbackRowMapper = new RowMapper<Feedback>() {
        @Override
        public Feedback mapRow(ResultSet rs, int rowNum) throws SQLException {
            Feedback feedback = new Feedback();
            feedback.setId(rs.getLong("id"));
            feedback.setPropertyId(rs.getLong("property_id"));
            feedback.setTenantName(rs.getString("tenant_name"));
            feedback.setTenantEmail(rs.getString("tenant_email"));
            feedback.setOverallRating(rs.getInt("overall_rating"));
            feedback.setEcoRating(rs.getInt("eco_rating"));
            feedback.setComment(rs.getString("comment"));
            feedback.setIsVerified(rs.getBoolean("is_verified"));
            feedback.setIsRecommended(rs.getBoolean("is_recommended"));
            feedback.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            feedback.setInsulationExperience(rs.getInt("insulation_experience"));
            feedback.setEnergyBillSatisfaction(rs.getInt("energy_bill_satisfaction"));
            feedback.setSolarSystemSatisfaction(rs.getInt("solar_system_satisfaction"));
            feedback.setWaterEfficiencySatisfaction(rs.getInt("water_efficiency_satisfaction"));
            feedback.setGreenSpaceSatisfaction(rs.getInt("green_space_satisfaction"));
            return feedback;
        }
    };
    
    public List<Feedback> findAll() {
        String sql = "SELECT * FROM feedback ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, feedbackRowMapper);
    }
    
    public List<Feedback> findByPropertyId(Long propertyId) {
        String sql = "SELECT * FROM feedback WHERE property_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, feedbackRowMapper, propertyId);
    }
    
    public List<Feedback> findVerified() {
        String sql = "SELECT * FROM feedback WHERE is_verified = true ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, feedbackRowMapper);
    }
    
    public Optional<Feedback> findById(Long id) {
        String sql = "SELECT * FROM feedback WHERE id = ?";
        List<Feedback> feedbacks = jdbcTemplate.query(sql, feedbackRowMapper, id);
        return feedbacks.isEmpty() ? Optional.empty() : Optional.of(feedbacks.get(0));
    }
    
    public Feedback save(Feedback feedback) {
        if (feedback.getId() == null) {
            return insert(feedback);
        } else {
            return update(feedback);
        }
    }
    
    private Feedback insert(Feedback feedback) {
        String sql = """
            INSERT INTO feedback (property_id, tenant_name, tenant_email, overall_rating, 
                                eco_rating, comment, is_verified, is_recommended, created_at,
                                insulation_experience, energy_bill_satisfaction, 
                                solar_system_satisfaction, water_efficiency_satisfaction, 
                                green_space_satisfaction)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, feedback.getPropertyId());
            ps.setString(2, feedback.getTenantName());
            ps.setString(3, feedback.getTenantEmail());
            ps.setInt(4, feedback.getOverallRating());
            ps.setInt(5, feedback.getEcoRating() != null ? feedback.getEcoRating() : 0);
            ps.setString(6, feedback.getComment());
            ps.setBoolean(7, feedback.getIsVerified() != null ? feedback.getIsVerified() : false);
            ps.setBoolean(8, feedback.getIsRecommended() != null ? feedback.getIsRecommended() : false);
            ps.setTimestamp(9, Timestamp.valueOf(now));
            ps.setInt(10, feedback.getInsulationExperience() != null ? feedback.getInsulationExperience() : 0);
            ps.setInt(11, feedback.getEnergyBillSatisfaction() != null ? feedback.getEnergyBillSatisfaction() : 0);
            ps.setInt(12, feedback.getSolarSystemSatisfaction() != null ? feedback.getSolarSystemSatisfaction() : 0);
            ps.setInt(13, feedback.getWaterEfficiencySatisfaction() != null ? feedback.getWaterEfficiencySatisfaction() : 0);
            ps.setInt(14, feedback.getGreenSpaceSatisfaction() != null ? feedback.getGreenSpaceSatisfaction() : 0);
            return ps;
        }, keyHolder);
        
        feedback.setId(keyHolder.getKey().longValue());
        feedback.setCreatedAt(now);
        
        return feedback;
    }
    
    private Feedback update(Feedback feedback) {
        String sql = """
            UPDATE feedback SET tenant_name = ?, tenant_email = ?, overall_rating = ?, 
                              eco_rating = ?, comment = ?, is_verified = ?, is_recommended = ?,
                              insulation_experience = ?, energy_bill_satisfaction = ?, 
                              solar_system_satisfaction = ?, water_efficiency_satisfaction = ?, 
                              green_space_satisfaction = ?
            WHERE id = ?
            """;
        
        jdbcTemplate.update(sql,
            feedback.getTenantName(),
            feedback.getTenantEmail(),
            feedback.getOverallRating(),
            feedback.getEcoRating(),
            feedback.getComment(),
            feedback.getIsVerified(),
            feedback.getIsRecommended(),
            feedback.getInsulationExperience(),
            feedback.getEnergyBillSatisfaction(),
            feedback.getSolarSystemSatisfaction(),
            feedback.getWaterEfficiencySatisfaction(),
            feedback.getGreenSpaceSatisfaction(),
            feedback.getId()
        );
        
        return feedback;
    }
    
    public void deleteById(Long id) {
        String sql = "DELETE FROM feedback WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    public double getAverageRatingForProperty(Long propertyId) {
        String sql = "SELECT AVG(overall_rating) FROM feedback WHERE property_id = ? AND is_verified = true";
        Double result = jdbcTemplate.queryForObject(sql, Double.class, propertyId);
        return result != null ? result : 0.0;
    }
    
    public double getAverageEcoRatingForProperty(Long propertyId) {
        String sql = "SELECT AVG(eco_rating) FROM feedback WHERE property_id = ? AND is_verified = true AND eco_rating > 0";
        Double result = jdbcTemplate.queryForObject(sql, Double.class, propertyId);
        return result != null ? result : 0.0;
    }
    
    public long countByPropertyId(Long propertyId) {
        String sql = "SELECT COUNT(*) FROM feedback WHERE property_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, propertyId);
    }
}