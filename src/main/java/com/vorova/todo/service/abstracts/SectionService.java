package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.entity.Section;
import com.vorova.todo.models.entity.Task;

import java.util.List;
import java.util.Optional;

public interface SectionService {

    Section add(Section section);

    Section update(Section section);

    List<Section> getSectionsByProjectId(long projectId);

    Optional<Section> getSectionById(long id);

    boolean isBelongTaskOfSection(Task task, Section section);

    Section getPrevSection(long sectionId);

    long getIdFirstSectionInInboxByUserId(long userId);
    Section getLastSectionInInboxByUserId(long userId);
}
