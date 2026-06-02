package com.example.userdemo.user;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void listUsersReturnsAllUsers() {
        User user = userWithId(1L, "alice", "研发部");
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> users = userService.listUsers();

        assertThat(users).containsExactly(new UserResponse(1L, "alice", "研发部"));
    }

    @Test
    void getUserReturnsExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userWithId(1L, "bob", "财务部")));

        UserResponse user = userService.getUser(1L);

        assertThat(user).isEqualTo(new UserResponse(1L, "bob", "财务部"));
    }

    @Test
    void createUserSavesNewUser() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> userWithId(2L,
                invocation.getArgument(0, User.class).getUsername(),
                invocation.getArgument(0, User.class).getOrganization()));

        UserResponse user = userService.createUser(new UserRequest("carol", "市场部"));

        assertThat(user).isEqualTo(new UserResponse(2L, "carol", "市场部"));
    }

    @Test
    void updateUserUpdatesExistingUser() {
        User user = userWithId(3L, "old", "旧机构");
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UserResponse updated = userService.updateUser(3L, new UserRequest("new", "新机构"));

        assertThat(updated).isEqualTo(new UserResponse(3L, "new", "新机构"));
    }

    @Test
    void deleteUserDeletesExistingUser() {
        User user = userWithId(4L, "dave", "运营部");
        when(userRepository.findById(4L)).thenReturn(Optional.of(user));

        userService.deleteUser(4L);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).delete(captor.capture());
        assertThat(captor.getValue()).isSameAs(user);
    }

    @Test
    void getUserThrowsWhenMissing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("用户不存在");
    }

    private static User userWithId(Long userId, String username, String organization) {
        User user = new User(username, organization);
        try {
            Field field = User.class.getDeclaredField("userId");
            field.setAccessible(true);
            field.set(user, userId);
            return user;
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
