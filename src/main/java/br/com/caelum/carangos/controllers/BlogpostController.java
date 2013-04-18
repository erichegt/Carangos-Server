package br.com.caelum.carangos.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;

import br.com.caelum.carangos.modelo.BlogPost;
import br.com.caelum.carangos.repositories.BlogPostRepository;
import br.com.caelum.carangos.view.Pagina;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
public class BlogpostController {

	private final Result result;
	private final BlogPostRepository repository;

	private final Validator validator;

	public BlogpostController(Result result, BlogPostRepository repository,
			Validator validator) {
		this.result = result;
		this.repository = repository;

		this.validator = validator;
	}

	// @Consumes(value={"image/png", "application/json"})
	@Post("/post/image")
	public void uploadImage(UploadedFile file, BlogPost blogPost, ServletContext context) {
		System.out.println(blogPost.getMensagem());
		String caminhoDaFoto = context.getRealPath("/imagens") + "/" + file.getFileName();
		File arquivo = new File(caminhoDaFoto);
		try {
			IOUtils.copy(file.getFile(), new FileOutputStream(arquivo));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		blogPost.setFoto(caminhoDaFoto);
		validator.validate(blogPost);
		validator.onErrorUse(Results.status()).notAcceptable();
		
		repository.create(blogPost);
		
		result.use(Results.json()).from(blogPost).recursive().serialize();
		
	}

	@Get("/posts")
	public List<BlogPost> index() {
		return repository.findAll();
	}

	@Get("/post/list")
	public void buscaPaginada(Pagina pagina) {
		if (pagina == null) {
			result.nothing();
		} else {
			List<BlogPost> posts = repository.find(pagina);
			// result.use(Results.representation()).from(posts).serialize();
			result.use(Results.json()).from(posts).recursive().serialize();
		}
	}

	@Post("/posts")
	public void create(BlogPost post) {
		validator.validate(post);
		validator.onErrorUsePageOf(this).newBlogpost();
		repository.create(post);
		result.redirectTo(this).index();
	}

	@Post("/post/salvar")
	public void createPost(BlogPost post) {
		System.out.println(post);

		BlogPost novoPost = repository.create(post);

		result.use(Results.json()).from(novoPost).recursive().serialize();
	}

	@SuppressWarnings("deprecation")
	@Get("/posts/new")
	public BlogPost newBlogpost() {
		return new BlogPost();
	}

	@Put("/posts")
	public void update(BlogPost post) {
		validator.validate(post);
		validator.onErrorUsePageOf(this).edit(post);
		repository.update(post);
		result.redirectTo(this).index();
	}

	@Get("/posts/{post.id}/edit")
	public BlogPost edit(BlogPost post) {

		return repository.find(post.getId());
	}

	@Get("/posts/{post.id}")
	public BlogPost show(BlogPost post) {
		return repository.find(post.getId());
	}

	@Delete("/posts/{post.id}")
	public void destroy(BlogPost post) {
		repository.destroy(repository.find(post.getId()));
		result.redirectTo(this).index();
	}
}