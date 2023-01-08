package expertostech.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import expertostech.model.UsuarioModel;
import expertostech.repository.UsuarioRepository;

@RestController
@RequestMapping(value = "/api/usuario")
public class UsuarioController {

	private final UsuarioRepository repository;
	private final PasswordEncoder encoder;

	public UsuarioController(UsuarioRepository repository, PasswordEncoder encoder) {
		this.repository = repository;
		this.encoder = encoder;
	}

	@GetMapping(value = "/listarTodos")
	public ResponseEntity<List<UsuarioModel>> listarTodos() {
		return ResponseEntity.ok(this.repository.findAll());
	}

	@PostMapping(value = "/salvar")
	public ResponseEntity<UsuarioModel> salvar(@RequestBody UsuarioModel model) {
		model.setPassword(this.encoder.encode(model.getPassword()));
		return ResponseEntity.ok(this.repository.save(model));

	}

	@PostMapping(value = "/validarSenha")
	public ResponseEntity<Boolean> validarSenha(@RequestParam String login, @RequestParam String password) {
		Optional<UsuarioModel> optUsuario = this.repository.findByLogin(login);
		if (optUsuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		}
		UsuarioModel usuario = optUsuario.get();
		boolean valid = encoder.matches(password, usuario.getPassword());

		HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		return ResponseEntity.status(status).body(valid);
	}

}
