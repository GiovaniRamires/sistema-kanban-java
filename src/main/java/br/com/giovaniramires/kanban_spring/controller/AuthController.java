package br.com.giovaniramires.kanban_spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.giovaniramires.kanban_spring.dto.LoginDTO;
import br.com.giovaniramires.kanban_spring.dto.RegistroDTO;
import br.com.giovaniramires.kanban_spring.dto.TokenDTO;
import br.com.giovaniramires.kanban_spring.model.Usuario;
import br.com.giovaniramires.kanban_spring.repository.UsuarioRepository;
import br.com.giovaniramires.kanban_spring.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegistroDTO dto){
        if(usuarioRepository.findByEmail(dto.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email não cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto){
        var usuario = usuarioRepository.findByEmail(dto.getEmail())
            .orElse(null);

        if(usuario == null || !passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha incorreta");
        }
        String token = jwtUtil.gerarToken(usuario.getEmail());
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(token);
        return ResponseEntity.ok(tokenDTO);
    }

}
