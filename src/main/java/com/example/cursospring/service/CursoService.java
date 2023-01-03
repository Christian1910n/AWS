package com.example.cursospring.service;

import java.awt.print.Pageable;
import java.util.Optional;

import com.example.cursospring.entity.Curso;

import org.springframework.data.domain.Page;

public interface CursoService {
	public Iterable<Curso> findAll();
	public Page<Curso> findAll(Pageable pageable);
	public Optional<Curso>findById(Integer id);
	public Curso save(Curso user);
	public void deleteById(Integer id);

}
