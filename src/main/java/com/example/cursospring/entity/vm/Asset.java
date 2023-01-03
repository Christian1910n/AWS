package com.example.cursospring.entity.vm;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor//construir un constructor con todas las propiedades
public class Asset {
    private byte [] content;//almacenar los bytes
    private String contentType;//Tipo de contenido
}
