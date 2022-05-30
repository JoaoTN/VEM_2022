package ufc.npi.prontuario.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_ALTERAR_SENHA;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.GetEmailUsuario;
import ufc.npi.prontuario.model.Token;
import ufc.npi.prontuario.model.Usuario;

@DatabaseSetup(UsuarioServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { UsuarioServiceTest.DATASET })
public class UsuarioServiceTest extends AbstractServiceTest {

	public static final String DATASET = "/database-tests-usuario.xml";

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private TokenService tokenService;

	@Test
	public void buscarPorEmail() {
		String email = "servidor1@email.com";

		Usuario usuario = usuarioService.buscarPorEmail(email);

		assertEquals(email, GetEmailUsuario.getUsuarioEmail(usuario));
	}
	
	public Integer mostrarIdUsuario(Usuario usuario) {
		return usuario.getId();
	}

	@Test
	public void alterarSenha() {
		Usuario usuario = usuarioService.buscarPorEmail("servidor1@email.com");

		try {
			usuarioService.alterarSenha(mostrarIdUsuario(usuario), "1234", "12345");
		} catch (ProntuarioException e) {
			e.printStackTrace();
		}

		assertTrue(new BCryptPasswordEncoder().matches("12345", usuario.getSenha()));

		try {
			usuarioService.alterarSenha(mostrarIdUsuario(usuario), "4321", "12345");
		} catch (ProntuarioException e) {
			assertThat(e).isInstanceOf(ProntuarioException.class).hasMessage(ERRO_ALTERAR_SENHA);
		}
	}

	@Test
	public void recuperarSenhaUsuarioInvalido() {
		String email = "nomeEmail@email.com.br";

		usuarioService.recuperarSenha(email);

		Usuario usuario = usuarioService.buscarPorEmail(email);

		assertNull(tokenService.buscarPorUsuario(usuario));
	}

	@Test
	public void recuperarSenhaUsuarioComSolicitacaoAtiva() {
		String email = "servidor1@email.com";
		Usuario usuario = usuarioService.buscarPorEmail(email);

		String aux = tokenService.buscarPorUsuario(usuario).getToken();

		usuarioService.recuperarSenha(email);

		assertEquals(aux, tokenService.buscarPorUsuario(usuario).getToken());
	}

	@Test
	public void recuperarSenhaUsuarioSemSolicitacaoAtiva() {
		String email = "servidor2@email.com";

		usuarioService.recuperarSenha(email);

		Usuario usuario = usuarioService.buscarPorEmail(email);

		assertNotNull(tokenService.buscarPorUsuario(usuario));
	}

	@Test
	public void novaSenha() {
		Token token = tokenService.buscar("6a8b39b2-361b-4842-a80f-48b234f7918d");

		Usuario usuario = token.getUsuario();

		usuarioService.novaSenha(token, "12345");

		assertTrue(new BCryptPasswordEncoder().matches("12345", usuario.getSenha()));
		assertFalse(tokenService.existe("6a8b39b2-361b-4842-a80f-48b234f7918d"));
	}
}
