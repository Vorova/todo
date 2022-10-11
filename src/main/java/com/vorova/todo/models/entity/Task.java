package com.vorova.todo.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "parent_id")
    private long parentId;

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

    @Column(name = "repeat")
    private String repeat;

    @Column(name = "is_first")
    private boolean isFirst;

    @Column(name = "id_next_task")
    private long idNextTask;

    @OneToMany
    private List<Label> labels;

    @ManyToOne
    private User user;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Section section;

}