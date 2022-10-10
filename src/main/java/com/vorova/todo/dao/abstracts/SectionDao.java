package com.vorova.todo.dao.abstracts;

import com.vorova.todo.models.entity.Section;

import java.util.List;
import java.util.Optional;

public interface SectionDao {

    Section persist(Section section);

    Section getLastSectionOfProjectByProjectId(long id);

    Section update(Section section);

    List<Section> getSectionByProjectId(long projectId);

    Optional<Section> getSectionById(long sectionId);

    Section getPrevSection(long sectionId);

    long getIdFirstSectionInInboxByUserId(long userId);

    Section getLastSectionInInboxByUserId(long userId);

}
