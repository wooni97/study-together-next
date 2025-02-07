package dev.flab.studytogether.infra.event.eventListener;

import dev.flab.studytogether.core.domain.event.EventListener;
import dev.flab.studytogether.core.domain.member.event.MemberV2SignUpEvent;
import dev.flab.studytogether.core.domain.notification.AbstractNotificationData;
import dev.flab.studytogether.core.domain.notification.MailNotificationData;
import dev.flab.studytogether.core.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberV2SingUpEventListenerImpl implements EventListener<MemberV2SignUpEvent> {

    private final NotificationService notificationService;

    @Override
    @org.springframework.context.event.EventListener(MemberV2SignUpEvent.class)
    @Transactional
    public void handleEvent(MemberV2SignUpEvent event) {
        String subject = "회원 가입 이메일 인증";
        String content = "<h1>[이메일 인증]</h1>" +
                "<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>" +
                "<a href='http://member/signUpConfirm?email=" +
                event.getUserEmail() +
                "&authKey=" +
                event.getAuthKey() +
                "' target='_blenk'>이메일 인증 확인</a>";

        AbstractNotificationData mailNotification =
                new MailNotificationData(content, null, subject, event.getUserEmail());
        notificationService.send(mailNotification);
    }

}
