package com.biblioteca.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/usuarios")
    public String usuarios() {
        return "usuarios";
    }

    @GetMapping("/livros")
    public String livros() {
        return "livros";
    }

    @GetMapping("/emprestimos")
    public String emprestimos() {
        return "emprestimos";
    }

    @GetMapping("/multas")
    public String multas() {
        return "multas";
    }

    @GetMapping("/reservas")
    public String reservas() {
        return "reservas";
    }

    @GetMapping("/relatorios")
    public String relatorios() {
        return "relatorios";
    }
}
