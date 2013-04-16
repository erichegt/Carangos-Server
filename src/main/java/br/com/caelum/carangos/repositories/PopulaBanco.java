package br.com.caelum.carangos.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.caelum.carangos.modelo.BlogPost;
import br.com.caelum.carangos.modelo.Usuario;

public class PopulaBanco {
	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("default");

		EntityManager em = factory.createEntityManager();

		System.out.println("POPULANDO!!");

		popula(em);
		
		em.close();
	}

	private static void popula(EntityManager em) {
		em.getTransaction().begin();

		List<Usuario> usuarios = new ArrayList<Usuario>();

		usuarios.add(new Usuario("Erich"));
		usuarios.add(new Usuario("Andre"));
		usuarios.add(new Usuario("Felipe"));

		Map<Usuario, Integer> kilometragens = new HashMap<Usuario, Integer>();
		kilometragens.put(usuarios.get(0), 100);
		kilometragens.put(usuarios.get(1), 200);
		kilometragens.put(usuarios.get(2), 300);

		for (Usuario usuario : usuarios) {
			em.persist(usuario);
		}

		for (int i = 0; i < 2000; i++) {
			Usuario usuarioAleatorio = usuarios.get(new Random().nextInt(3));

			Integer kilometragem = kilometragens.get(usuarioAleatorio);
			Integer novaKilometragem = kilometragem + 1000;
			kilometragens.put(usuarioAleatorio, novaKilometragem);

			em.persist(new BlogPost("Meu carro fez " + novaKilometragem + "Km",
					usuarioAleatorio));
		}

		em.getTransaction().commit();
	}
}
