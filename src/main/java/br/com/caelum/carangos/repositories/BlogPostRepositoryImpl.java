package br.com.caelum.carangos.repositories;

import javax.persistence.EntityManager;

import br.com.caelum.carangos.modelo.BlogPost;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class BlogPostRepositoryImpl extends Repository<BlogPost, Long>
		implements BlogPostRepository {

	protected BlogPostRepositoryImpl(EntityManager em) {
		super(em);
	}
}
