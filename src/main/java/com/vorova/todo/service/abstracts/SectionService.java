package com.vorova.todo.service.abstracts;

import com.vorova.todo.models.entity.Section;

import java.util.List;
import java.util.Optional;

public interface SectionService {

    Section add(Section section);

    Section update(Section section);

    List<Section> getSectionsByProjectId(long projectId);

    Optional<Section> getSectionById(long id);

    Section getPrevSection(long sectionId);

    Optional<Section> getLastSectionInInboxByUserId(long userId);
}
