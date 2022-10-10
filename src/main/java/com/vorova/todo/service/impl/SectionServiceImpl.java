package com.vorova.todo.service.impl;

import com.vorova.todo.dao.abstracts.SectionDao;
import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.TypeErrorDto;
import com.vorova.todo.models.entity.Project;
import com.vorova.todo.models.entity.Section;
import com.vorova.todo.models.entity.Task;
import com.vorova.todo.models.entity.User;
import com.vorova.todo.service.abstracts.ProjectService;
import com.vorova.todo.service.abstracts.SectionService;
import com.vorova.todo.service.abstracts.UserService;
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
    private final UserService userService;

    @Autowired
    public SectionServiceImpl(SectionDao sectionDao, ProjectService projectService, UserService userService) {
        this.sectionDao = sectionDao;
        this.projectService = projectService;
        this.userService = userService;
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

        // Получаем проект
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

    @Override
    public Optional<Section> getSectionById(long id) {
        return sectionDao.getSectionById(id);
    }

    @Override
    public boolean isBelongTaskOfSection(Task task, Section section) {
        Optional<Section> sectionOptional = getSectionById(task.getId());
        if(sectionOptional.isPresent()) {
            Section sectionGet = sectionOptional.get();
            if (sectionGet.getId() != task.getSection().getId()) {
                return false;
            }
            Optional<Project> projectOptional = projectService.getProjectById(sectionGet.getProject().getId());
            if (projectOptional.isEmpty()) {
                return false;
            }
            return sectionGet.getProject().getId() == projectOptional.get().getId();
        }
        return false;
    }

    private Section reorderInProject(Section section) {
        Section persistedSection;

        if (section.getNextIdSection() == 0) {
            // получаем id последней секции в проекте
            Section lastSectionInTheProject = section.getProject().getIdFirstSection() == 0
                    ? null
                    : sectionDao.getLastSectionOfProjectByProjectId(section.getProject().getId());

            persistedSection = sectionDao.persist(section);
            if (lastSectionInTheProject == null) {
                section.getProject().setIdFirstSection(persistedSection.getId());
                projectService.update(section.getProject());
            } else {
                lastSectionInTheProject.setNextIdSection(persistedSection.getId());
                update(lastSectionInTheProject);
            }
        } else {
            persistedSection = sectionDao.persist(section);
            Section prevSection = getPrevSection(section.getNextIdSection());
            prevSection.setNextIdSection(persistedSection.getId());
            sectionDao.update(prevSection);
        }
        return persistedSection;
    }

    private Section reorderInInbox(Section section) {
        Section persistedSection;
        // получаем id последней секции в inbox
        Section lastSection = getIdFirstSectionInInboxByUserId(section.getAuthor().getId()) == 0 ?
                null : getLastSectionInInboxByUserId(section.getAuthor().getId());
        if (section.getNextIdSection() == 0) {
            persistedSection = sectionDao.persist(section);
            if (lastSection == null) {
                Optional<User> userOptional = userService.getById(section.getAuthor().getId());
                if(userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setIdFirstSection(persistedSection.getId());
                    userService.update(user);
                }
            } else {
                lastSection.setNextIdSection(persistedSection.getId());
                sectionDao.update(lastSection);
            }
        } else {
            persistedSection = sectionDao.persist(section);
            Section prevSection = getPrevSection(section.getNextIdSection());
            prevSection.setNextIdSection(persistedSection.getId());
            sectionDao.update(prevSection);
        }
        return persistedSection;
    }

    @Override
    public Section getPrevSection(long sectionId) {
        return sectionDao.getPrevSection(sectionId);
    }

    @Override
    public long getIdFirstSectionInInboxByUserId(long userId) {
        return sectionDao.getIdFirstSectionInInboxByUserId(userId);
    }

    @Override
    public Section getLastSectionInInboxByUserId(long userId) {
        return sectionDao.getLastSectionInInboxByUserId(userId);
    }

}