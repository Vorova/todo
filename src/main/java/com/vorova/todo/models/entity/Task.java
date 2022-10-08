package com.vorova.todo.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "is_done")
    private boolean isDone;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "date_persist")
    private LocalDateTime datePersist;

    @Column(name = "date_last_change")
    private LocalDateTime dateLastChange;

    @Column(name = "date_deadline")
    private LocalDateTime dateDeadline;

    @Column(name = "is_repeat")
    private boolean isRepeat;

    @Column(name = "repeat")
    private String repeat;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}