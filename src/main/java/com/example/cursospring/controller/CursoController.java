package com.example.cursospring.controller;


import com.example.cursospring.entity.Curso;
import com.example.cursospring.repository.CursoRepository;
import com.example.cursospring.service.CursoServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    //Implementamos

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private CursoServiceImp s3service;

    //Listar
    @GetMapping
    List<Curso> getAll(){
        return  cursoRepository.findAll()
                .stream()
                .peek(curso -> curso.setImagenUrl(s3service.getObjectUrl(curso.getImagenPath())))
                .collect(Collectors.toList());
    }

    //Crear
    @PostMapping
    Curso create(@RequestBody Curso curso){
         cursoRepository.save(curso);
         curso.setImagenUrl(s3service.getObjectUrl(curso.getImagenPath()));
        return  curso;
    }

}
