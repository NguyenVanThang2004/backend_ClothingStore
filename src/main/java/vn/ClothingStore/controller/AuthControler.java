package vn.ClothingStore.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import vn.ClothingStore.dtos.LoginDTO;
import vn.ClothingStore.dtos.ResLoginDTO;
import vn.ClothingStore.util.SecurityUtil;

@RestController
@RequestMapping("api/v1/auth")
public class AuthControler {

    private final AuthenticationManagerBuilder authenticationManagerBuilder ;
    private final SecurityUtil securityUtil ;

    public AuthControler(AuthenticationManagerBuilder authenticationManagerBuilder , SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil  = securityUtil ;
    }

    @PostMapping("login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO){

        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken); // import// org.springframework.security.core.Authentication;

        // create token

        String access_token = this.securityUtil.CreateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        res.setAccessToken(access_token);

       return ResponseEntity.ok().body(res);
    }
}
