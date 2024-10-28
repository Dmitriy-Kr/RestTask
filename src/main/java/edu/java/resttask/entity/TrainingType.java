package edu.java.resttask.entity;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Table(name="training_type")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainingtype_id")
    private Long id;
    @Column(name="training_type")
    private String trainingType;

    public TrainingType() {
    }

    public TrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public TrainingType(Long id, String trainingType) {
        this.id = id;
        this.trainingType = trainingType;
    }

    public String getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(String trainingType) {
        this.trainingType = trainingType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        TrainingType that = (TrainingType) o;
        return getId().equals(that.getId()) && getTrainingType().equals(that.getTrainingType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTrainingType());
    }

    @Override
    public String toString() {
        return "TrainingType{" +
                "id=" + id +
                ", trainingType='" + trainingType + '\'' +
                '}';
    }
}
