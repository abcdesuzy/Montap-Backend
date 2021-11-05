package com.project.montap.service;

import com.project.montap.domain.entity.ConfirmationToken;
import com.project.montap.domain.repository.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

// 메일을 만들어서 보내게끔 하는
@RequiredArgsConstructor
@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailSenderService emailSenderService;

    /**
     * 이메일 인증 토큰 생성
     *
     * @return
     */
    public Long createEmailConfirmationToken(String receiverEmail, String userId) {

        ConfirmationToken emailConfirmationToken = ConfirmationToken.createEmailConfirmationToken(userId);
        confirmationTokenRepository.save(emailConfirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiverEmail);
        mailMessage.setSubject("회원가입 이메일 인증");
        mailMessage.setText("http://localhost:8090/confirm/email/" + emailConfirmationToken.getIdx());
        emailSenderService.sendEmail(mailMessage);

        return emailConfirmationToken.getIdx();
    }

    /**
     * 유효한 토큰 가져오기
     *
     * @param confirmationTokenId
     * @return
     */
    @Transactional
    public ConfirmationToken findByIdAndExpirationDateAfterAndExpired(Long confirmationTokenId) throws Exception {
        Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenRepository.findByIdxAndExpirationDateAfterAndExpired(confirmationTokenId, LocalDateTime.now(), 0);
        if (optionalConfirmationToken.isEmpty()) {
            throw new Exception("인증 토큰 만료");
        }
        return optionalConfirmationToken.get();
    }

    ;

}