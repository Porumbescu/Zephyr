package com.porumb.zephyr.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
public class UserQuizHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completionDate;

    public UserQuizHistory(User user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
        this.completionDate = new Date();
    }
}
