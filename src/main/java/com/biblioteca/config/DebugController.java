package com.biblioteca.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DebugController {

    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/debug/hash")
    public String gerarHash(@RequestParam String senha) {
        return passwordEncoder.encode(senha);
    }
}
