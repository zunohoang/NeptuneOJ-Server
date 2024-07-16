package org.example.neptuneojserver.services;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.auth.RegisterRequestDTO;
import org.example.neptuneojserver.dto.user.UserDTO;
import org.example.neptuneojserver.models.User;
import org.example.neptuneojserver.repositorys.UserRepository;
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

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFullName(user.getFullName());
        userDTO.setPoint(user.getPoint());
        userDTO.setDescription(user.getDescription());
        userDTO.setNumberOfProblems(user.getNumberOfProblems());
        userDTO.setRank(user.getRank());
        userDTO.setSolvedProblems(user.getAuthorProblems());
        userDTO.setCreatedProblems(user.getAuthorProblems());
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
}
