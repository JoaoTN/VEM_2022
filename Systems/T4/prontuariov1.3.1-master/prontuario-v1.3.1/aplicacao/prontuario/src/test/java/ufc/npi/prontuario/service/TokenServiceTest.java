package ufc.npi.prontuario.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.model.Token;
import ufc.npi.prontuario.model.Usuario;

@DatabaseSetup(TokenServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { TokenServiceTest.DATASET })
public class TokenServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-token.xml";

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UsuarioService usuarioService;

	@Test
	public void buscarPorUsuario() {
		Usuario usuario = usuarioService.buscarPorEmail("servidor1@email.com");
		Token token = tokenService.buscarPorUsuario(usuario);
		assertNotNull(token);

		usuario = usuarioService.buscarPorEmail("servidor2@email.com");
		token = tokenService.buscarPorUsuario(usuario);
		assertNull(token);
	}

	@Test
	public void buscar() {
		String idToken = "6a8b39b2-361b-4842-a80f-48b234f7918d";
		Token token = tokenService.buscar(idToken);
		assertNotNull(token);

		idToken = "6a8bnm9b2-3610-4802-ae0f-48bo34f7t18d";
		token = tokenService.buscar(idToken);
		assertNull(token);
	}

	@Test
	public void existe() {
		assertTrue(tokenService.existe("6a8b39b2-361b-4842-a80f-48b234f7918d"));

		assertFalse(tokenService.existe("6a8bnm9b2-3610-4802-ae0f-48bo34f7t18d"));
	}

	@Test
	public void salvar() {
		Usuario usuario = usuarioService.buscarPorEmail("servidor2@email.com");

		Token token = new Token();
		token.setToken("1ec6914a-a337-4829-88fd-76a41e040fff");
		token.setUsuario(usuario);

		tokenService.salvar(token);

		assertTrue(tokenService.existe("1ec6914a-a337-4829-88fd-76a41e040fff"));

		Usuario usuario2 = usuarioService.buscarPorEmail("aluno1@email.com");

		Token token2 = new Token();
		token2.setToken("1ec6914a-a337-4829-88fd-76a41e040fff");
		token2.setUsuario(usuario);

		tokenService.salvar(token2);

		assertNull(tokenService.buscarPorUsuario(usuario2));
	}

	@Test
	public void deletar() {
		Token token = new Token();
		token.setToken("6a8b39b2-361b-4842-a80f-48b234f7918d");

		tokenService.deletar(token);

		assertFalse(tokenService.existe("6a8b39b2-361b-4842-a80f-48b234f7918d"));
	}
}
