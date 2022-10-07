package com.vorova.todo.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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

    @Column
    private boolean isDone;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "date_persist")
    private Date datePersist;

    @Column(name = "date_last_change")
    private Date dateLastChange;

    @Column(name = "date_deadline")
    private Date dateDeadline;

    @Column(name = "is_repeat")
    private boolean isRepeat;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}