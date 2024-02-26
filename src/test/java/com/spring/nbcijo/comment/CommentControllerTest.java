package com.spring.nbcijo.comment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import com.spring.nbcijo.common.CommentFixture;
import com.spring.nbcijo.common.ControllerTest;
import com.spring.nbcijo.common.PostFixture;
import com.spring.nbcijo.common.UserFixture;
import com.spring.nbcijo.dto.request.CommentRequestDto;
import com.spring.nbcijo.dto.response.CommentResponseDto;
import com.spring.nbcijo.entity.User;
import com.spring.nbcijo.global.enumeration.ErrorCode;
import com.spring.nbcijo.global.exception.InvalidInputException;
import com.spring.nbcijo.repository.PostRepository;
import com.spring.nbcijo.repository.UserRepository;
import com.spring.nbcijo.service.CommentService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import com.spring.nbcijo.service.MyPageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

public class CommentControllerTest extends ControllerTest implements CommentFixture, PostFixture,
    UserFixture {

    @MockBean
    private CommentService commentService;
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private MyPageService myPageService;

    @Nested
    @DisplayName("댓글 생성 요청")
    class createComment {

        @DisplayName("댓글 생성 요청 성공")
        @Test
        void createComment_success() throws Exception {
            //given //when
            var action = mockMvc.perform(post("/comments/{postId}", TEST_POST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_COMMENT_REQUEST_DTO)));

            //then
            action.andExpect(status().isOk());
            verify(commentService, times(1))
                .createComment(any(User.class), eq(TEST_POST_ID), any(CommentRequestDto.class));
        }

        @DisplayName("댓글 생성 요청 실패 - 존재하지 않는 게시글 id")
        @Test
        void createComment_fail() throws Exception {
            //given
            doThrow(new InvalidInputException(ErrorCode.NOT_FOUND_POST)).when(commentService)
                .createComment(any(User.class), eq(TEST_POST_ID), any(CommentRequestDto.class));

            //when
            var action = mockMvc.perform(post("/comments/{postId}", TEST_POST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TEST_COMMENT_REQUEST_DTO)));

            //then
            action
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value(ErrorCode.NOT_FOUND_POST.getMessage()));
        }
    }

    @Nested
    @DisplayName("댓글 조회 요청")
    class getComments {

        @DisplayName("댓글 조회 요청 성공")
        @Test
        void getComments_success() throws Exception {
            //given
            var testComment = CommentTestUtils.get(TEST_COMMENT, TEST_COMMENT_ID,
                LocalDateTime.now(), TEST_USER, TEST_POST);
            var testComment2 = CommentTestUtils.get(TEST_ANOTHER_COMMENT, TEST_ANOTHER_COMMENT_ID,
                LocalDateTime.now().minusMinutes(1), TEST_USER, TEST_POST);

            given(commentService.getComments(eq(TEST_POST_ID)))
                .willReturn(List.of(new CommentResponseDto(testComment),
                    new CommentResponseDto(testComment2)));
            //when
            var action = mockMvc.perform(get("/comments/{postId}", TEST_POST_ID)
                .accept(MediaType.APPLICATION_JSON));

            //then
            action
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[*].content")
                    .value(Matchers.containsInAnyOrder(TEST_COMMENT_CONTENT,
                        TEST_ANOTHER_COMMENT.getContent())))
                .andExpect(jsonPath("$.data[*].id")
                    .value(Matchers.containsInAnyOrder(TEST_COMMENT_ID.intValue(),
                        TEST_ANOTHER_COMMENT_ID.intValue())));
        }

        @DisplayName("댓글 조회 요청 실패 - 존재하지 않는 게시글 id")
        @Test
        void getComments_fail() throws Exception {
            //given
            given(commentService.getComments(eq(TEST_POST_ID)))
                .willThrow(new InvalidInputException(ErrorCode.NOT_FOUND_POST));

            //when
            var action = mockMvc.perform(get("/comments/{postId}", TEST_POST_ID)
                .accept(MediaType.APPLICATION_JSON));

            //then
            action
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                    .value(ErrorCode.NOT_FOUND_POST.getMessage()));
        }
    }
}
