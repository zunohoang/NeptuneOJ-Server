package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.auth.RegisterRequestDTO;
import org.example.neptuneojserver.dto.user.UserDTO;
import org.example.neptuneojserver.models.Submission;
import org.example.neptuneojserver.models.User;
import org.example.neptuneojserver.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ProblemService problemService;
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        UserDTO userDTO = new UserDTO();

        assert user != null;
        mapUserToUserDTO(user, userDTO);
        userDTO.setCreatedProblems(user.getAuthorProblems());
        userDTO.setSolvedProblems(problemService.getProblemsAcceptedByUsername(user.getUsername()));
        userDTO.setSubmissions(user.getSubmissions());


        return userDTO;
    }

    public User loginByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        } else return null;
    }

    public User register(RegisterRequestDTO registerRequest) {
        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getFullName(),
                userRepository.count() + 1
        );
        if(userRepository.findByUsername(user.getUsername()) == null) {
            return userRepository.save(user);
        } else return null;
    }


    // cai nay support cho spring security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority(user.getRole())
        );
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    public Page<UserDTO> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        Page<User> users = userRepository.findAllPaged(pageable);

        return users.map(user -> {
            UserDTO userDTO = new UserDTO();
            mapUserToUserDTO(user, userDTO);
            return userDTO;
        });
    }

    private void mapUserToUserDTO(User user, UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setPoint(user.getPoint());
        userDTO.setDescription(user.getDescription());
        userDTO.setNumberOfProblems(user.getNumberOfProblems());
        userDTO.setRank(user.getRank());
    }

    public void updateUser(String username, UserDTO userDTO) {
        User user = userRepository.findByUsername(username);
        assert user != null;

        user.setFullName(userDTO.getFullName());
        user.setDescription(userDTO.getDescription());
        userRepository.save(user);
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        assert user != null;

        userRepository.delete(user);
    }

    public void setRank(User user, Submission submission) {
        Float point = submission.getProblem().getPoint();
        Float oldPoint = user.getPoint();
        Float newPoint = oldPoint + point;
        user.setPoint(newPoint);
        user.setNumberOfProblems(user.getNumberOfProblems() + 1);
        userRepository.save(user);

        List<User> affectedUsers;
        if (newPoint > oldPoint) {
            affectedUsers = userRepository.findByPointBetween(oldPoint, newPoint);
        } else {
            affectedUsers = userRepository.findByPointBetween(newPoint, oldPoint);
        }

        affectedUsers.add(user);

        affectedUsers.sort((u1, u2) -> Float.compare(u2.getPoint(), u1.getPoint()));

        for (int i = 0; i < affectedUsers.size(); i++) {
            User currentUser = affectedUsers.get(i);
            currentUser.setRank((long) (i + 1));
        }

        userRepository.saveAll(affectedUsers);
    }
}
