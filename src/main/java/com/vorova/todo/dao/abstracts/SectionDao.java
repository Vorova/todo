package com.vorova.todo.dao.abstracts;

import com.vorova.todo.models.entity.Section;

import java.util.List;
import java.util.Optional;

public interface SectionDao {

    Section persist(Section section);

    Optional<Section> getLastSectionOfProjectByProjectId(long id);

    Section update(Section section);

    List<Section> getSectionByProjectId(long projectId);

    Section getPrevSection(long sectionId);

    Optional<Section> getLastSectionInInboxByUserId(long userId);

    Optional<Section> getSectionById(long sectionId);
}
