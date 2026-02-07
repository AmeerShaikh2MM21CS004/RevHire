package com.revhire.dao;

import com.revhire.model.Resume;
import java.sql.SQLException;
import java.util.List;

public interface ResumesDAO {

    void upsertResume(
            int seekerId,
            String objective,
            String education,
            String experience,
            String skills,
            String projects
    ) throws SQLException;

    Resume fetchResumeBySeekerId(int seekerId) throws SQLException;

    List<Resume> fetchAllResumes() throws SQLException;

}
