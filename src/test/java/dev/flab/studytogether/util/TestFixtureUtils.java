package dev.flab.studytogether.util;

import dev.flab.studytogether.core.domain.member.entity.MemberV2;
import dev.flab.studytogether.core.domain.studygroup.entity.ParticipantV2;
import dev.flab.studytogether.core.domain.studygroup.entity.StudyGroup;

import java.time.LocalDateTime;

public class TestFixtureUtils {
    public static MemberV2 randomMember() {
        String email = "testEmail@gmail.com";
        String password = "password123";
        String nickname = "testNickName";

        return MemberV2.createNewMember(email, password, nickname);
    }

    public static StudyGroup randomStudyGroup() {
        String groupTitle = "Java Study Group";
        int maxParticipants = 10;
        StudyGroup.ActivateStatus activateStatus = StudyGroup.ActivateStatus.ACTIVATED;

        return new StudyGroup(groupTitle, maxParticipants, activateStatus);
    }

    public static StudyGroup randomStudyGroupWithParticipants() {
        String groupTitle = "Java Study Group";
        int maxParticipants = 10;
        StudyGroup.ActivateStatus activateStatus = StudyGroup.ActivateStatus.ACTIVATED;

        StudyGroup studyGroup = new StudyGroup(groupTitle, maxParticipants, activateStatus);

        ReflectionTestUtils.setField(studyGroup, "id", 1L);

        ParticipantV2 manager = new ParticipantV2(studyGroup,
                1L,
                ParticipantV2.Role.GROUP_MANAGER,
                ParticipantV2.ParticipantStatus.JOINED,
                LocalDateTime.of(2023, 1, 2, 4, 5));

        ReflectionTestUtils.setField(manager, "id", 1L);

        studyGroup.joinGroup(manager);

        ParticipantV2 participant = new ParticipantV2(studyGroup,
                2L,
                ParticipantV2.Role.ORDINARY_PARTICIPANT,
                ParticipantV2.ParticipantStatus.JOINED,
                LocalDateTime.of(2023, 2, 1, 2, 3, 55));

        ReflectionTestUtils.setField(participant, "id", 2L);

        studyGroup.joinGroup(participant);

        return studyGroup;
    }
}
