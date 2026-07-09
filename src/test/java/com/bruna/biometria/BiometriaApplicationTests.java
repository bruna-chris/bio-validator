package com.bruna.biometria;

import com.bruna.biometria.domain.model.User;
import com.bruna.biometria.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BiometriaApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	void deveExecutarFluxoCompletoDeBiometria() {
		//Cria um usuário de teste com um documento aleatório para não dar conflito
		String documentoUnico = "TESTE_" + System.currentTimeMillis();
		User novoUser = User.builder()
				.name("Usuario Teste Automatizado")
				.document(documentoUnico)
				.build();

		User usuarioSalvo = userService.createUser(novoUser);

		// Validações do passo 1
		assertNotNull(usuarioSalvo.getId(), "O ID deveria ter sido gerado");
		assertEquals("PENDING", usuarioSalvo.getStatus(), "O status inicial deve ser PENDING");
		System.out.println("======> USUÁRIO CRIADO COM SUCESSO! ID: " + usuarioSalvo.getId());

		//Tentar cadastrar o mesmo documento de novo (Deve estourar erro 409 Conflict)
		User usuarioDuplicado = User.builder()
				.name("Outro Nome")
				.document(documentoUnico)
				.build();

		assertThrows(ResponseStatusException.class, () -> {
			userService.createUser(usuarioDuplicado);
		}, "Deveria ter lançado ResponseStatusException por documento duplicado");
		System.out.println("======> VALIDAÇÃO DE DOCUMENTO DUPLICADO PASSOU!");

		//Listar todos e verificar se o nosso usuário está lá
		List<User> lista = userService.listAllUsers();
		assertTrue(lista.stream().anyMatch(u -> u.getId().equals(usuarioSalvo.getId())), "O usuário deveria estar na listagem");
		System.out.println("======> VALIDAÇÃO DE LISTAGEM PASSOU! TOTAL NO BANCO: " + lista.size());

		//Validar Biometria (O motor vai simular APPROVED ou REJECTED)
		User usuarioValidado = userService.validateBiometrics(usuarioSalvo.getId());
		
		assertNotEquals("PENDING", usuarioValidado.getStatus(), "O status deveria ter mudado");
		assertTrue(List.of("APPROVED", "REJECTED").contains(usuarioValidado.getStatus()), "O status deve ser APPROVED ou REJECTED");
		System.out.println("======> VALIDAÇÃO BIOMÉTRICA CONCLUÍDA! RESULTADO DO MOTOR: " + usuarioValidado.getStatus());
	}
}