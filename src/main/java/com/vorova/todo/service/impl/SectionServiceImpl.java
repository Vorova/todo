package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.SectionDao;
import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.TypeErrorDto;
import com.vorova.todo.models.entity.Project;
import com.vorova.todo.models.entity.Section;
import com.vorova.todo.service.abstracts.ProjectService;
import com.vorova.todo.service.abstracts.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionDao sectionDao;
    private final ProjectService projectService;

    @Autowired
    public SectionServiceImpl(SectionDao sectionDao,
                              ProjectService projectService) {
        this.sectionDao = sectionDao;
        this.projectService = projectService;
    }

    @Override
    @Transactional
    public Section add(Section section) {
        List<TypeErrorDto> errors = new ArrayList<>();
        if (section.getAuthor() == null) {
            errors.add(new TypeErrorDto("Section - not exist author", 1));
        }
        if(!errors.isEmpty()) {
            throw new CheckRequestException(errors);
        }

        Project project;
        if (section.getProject() == null) {
            project = null;
        } else {
            Optional<Project> projectOptional = projectService.getProjectById(section.getProject().getId());
            project = projectOptional.orElse(null);
        }

        Section persistedSection;
        if (project != null) {
            section.setProject(project);
            persistedSection = reorderInProject(section);
        } else {
            persistedSection = reorderInInbox(section);
        }
        return persistedSection;
    }

    @Override
    public Section update(Section section) {
        return sectionDao.update(section);
    }

    @Override
    public List<Section> getSectionsByProjectId(long projectId) {
        return sectionDao.getSectionByProjectId(projectId);
    }

    private Section reorderInProject(Section section) {
        Optional<Section> lastSection = sectionDao.getLastSectionOfProjectByProjectId(section.getProject().getId());

        Section persistedSection = sectionDao.persist(section);

        Optional<Section> nextSection = getSectionById(section.getNextIdSection());
        if (section.getNextIdSection() == 0 || nextSection.isEmpty()) {
            if (lastSection.isPresent()) {
                Section lastSectionGet = lastSection.get();
                lastSectionGet.setNextIdSection(persistedSection.getId());
                update(lastSectionGet);
            } else {
                persistedSection.setFirst(true);
                persistedSection = update(persistedSection);
            }
        } else {
            Section prevSection = getPrevSection(section.getNextIdSection());
            prevSection.setNextIdSection(persistedSection.getId());
            sectionDao.update(prevSection);
        }
        return persistedSection;
    }

    private Section reorderInInbox(Section section) {
        // получаем последнюю секцию в inbox
        Optional<Section> lastSectionOptional = getLastSectionInInboxByUserId(section.getAuthor().getId());

        Section persistedSection = sectionDao.persist(section);
        Optional<Section> nextSection = getSectionById(section.getNextIdSection());

        if (section.getNextIdSection() == 0 || nextSection.isEmpty()) {
            if (lastSectionOptional.isEmpty()) {
                persistedSection.setFirst(true);
                sectionDao.update(persistedSection);
            } else {
                lastSectionOptional.get().setNextIdSection(persistedSection.getId());
                sectionDao.update(lastSectionOptional.get());
            }
        } else {
            Section prevSection = getPrevSection(nextSection.get().getId());
            prevSection.setNextIdSection(persistedSection.getId());
            sectionDao.update(prevSection);
        }
        return persistedSection;
    }

    @Override
    public Optional<Section> getSectionById(long id) {
        return sectionDao.getSectionById(id);
    }

    @Override
    public Section getPrevSection(long sectionId) {
        return sectionDao.getPrevSection(sectionId);
    }

    @Override
    public Optional<Section> getLastSectionInInboxByUserId(long userId) {
        return sectionDao.getLastSectionInInboxByUserId(userId);
    }

}