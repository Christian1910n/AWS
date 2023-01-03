package com.example.cursospring.controller;

import com.example.cursospring.entity.vm.Asset;
import com.example.cursospring.service.CursoServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    private CursoServiceImp s3Service;


    //metodo para subir un archivo
    @PostMapping("/upload")
   public Map<String, String > upload(@RequestParam MultipartFile foto){
        String key = s3Service.putObject(foto);

        Map<String, String> result=new HashMap<>();
        result.put("key", key);
        result.put("url", s3Service.getObjectUrl(key));

        return result;
    }

    //metodo para objener el objeto
    @GetMapping(value = "/get-object", params = "key")
   public ResponseEntity<ByteArrayResource> getObject(@RequestParam String key){
        Asset asset= s3Service.getObject(key);
        ByteArrayResource resource= new ByteArrayResource(asset.getContent());

        return ResponseEntity
                .ok()
                .header("Content-Type", asset.getContentType())
                .contentLength(asset.getContent().length)
                .body(resource);
    }

    //metodo para eliminar un objeto
    @DeleteMapping(value = "/delete-object", params = "key")
   public void eliminarObjeto(@RequestParam String key){
        s3Service.deleteObject(key);
    }

}
