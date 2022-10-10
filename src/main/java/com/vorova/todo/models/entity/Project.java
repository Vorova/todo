package com.vorova.todo.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    private User author;

    @Column(name = "next_project_id")
    private long nextProjectId;

    @Column(name = "is_show_done")
    private boolean isShowDone;

    @OneToMany(mappedBy = "project")
    private List<Section> sections;

    @Column(name = "id_first_section")
    private long idFirstSection;

}
