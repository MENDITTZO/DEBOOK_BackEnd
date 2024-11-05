package com.mendittzo.user.command.application.service;

import com.mendittzo.image.command.service.ImageService;
import com.mendittzo.user.command.application.dto.UserCreateRequestDTO;
import com.mendittzo.user.command.application.dto.UserUpdateDTO;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import com.mendittzo.user.command.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final ImageService imageService;

    private static final String[] adj = {
            "책 먹는", "열정적인", "교양 있는", "다독하는", "문학",
            "안경 낀", "상상력 넘치는", "지적인", "사색하는", "탐구하는",
            "호기심 가득한", "비판적인", "통찰력 있는", "밤 샌", "은하수를 여행하는",
            "빨간 머리", "발이 큰", "지혜로운", "자유로운", "졸고 있는"
    };
    private static final String[] noun = {
            "여우", "강아지", "독서가", "지식인", "독서광",
            "학자", "여행자", "히치하이커", "이야기꾼", "몽상가",
            "탐험가", "시인", "수집가", "사색가", "작가",
            "평론가", "매니아", "고양이", "양철나무꾼", "거위"
    };

    // 새로운 사용자 생성
    // todo: 안 쓰면 지우기
    public User registerNewUser(UserCreateRequestDTO userRequest) {

        User newUser = UserMapper.toEntity(userRequest);
        return userRepository.save(newUser);
    }

    // 사용자의 닉네임을 생성하는 메소드
    public String generateUserNickname() {

        String prefix = adj[(int) (Math.random() * adj.length)];
        String postfix = noun[(int) (Math.random() * noun.length)];

        String uuid = UUID.randomUUID().toString().substring(0, 8);

        return prefix + " " + postfix + uuid;

    }

    // 유저 정보 업데이트
    public void updateUser(UserUpdateDTO userUpdateDTO) throws IOException {

        // 업데이트 할 유저 조회
        User updateUser = userRepository.findByLoginId(userUpdateDTO.getUserId());

        // S3에서 이미지 삭제를 위해 기존 프로필 이미지 URL 반환
        String oldImageUrl = updateUser.getProfileImg();

        // 새 이미지 업로드 및 예전 이미지 삭제
        String newImageUrl = imageService.updateImage(userUpdateDTO.getProfileImage(), oldImageUrl);

        updateUser.updateUser(userUpdateDTO.getNickname(), newImageUrl);

        // 유저 DB 업데이트
        userRepository.save(updateUser);

    }
}
