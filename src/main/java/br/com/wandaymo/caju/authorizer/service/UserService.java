package br.com.wandaymo.caju.authorizer.service;

import br.com.wandaymo.caju.authorizer.api.dto.UserDTO;
import br.com.wandaymo.caju.authorizer.domain.User;
import br.com.wandaymo.caju.authorizer.log.Logged;
import br.com.wandaymo.caju.authorizer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Logged
    public String save(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()) != null) {
            return "This username is already in use.";
        }

        var user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);

        return generateToken(userDTO);
    }

    @Logged
    public String getToken(UserDTO userDTO) {
        var userData = userRepository.findByUsername(userDTO.getUsername());
        if (userData!= null && bCryptPasswordEncoder.matches(userDTO.getPassword(), userData.getPassword())) {
            return generateToken(userDTO);
        } else {
            return "Unregistered user";
        }
    }

    private String generateToken(UserDTO userDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(),
                        userDTO.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        var user = (User) authenticate.getPrincipal();
        return tokenService.getToken(user);
    }
}
