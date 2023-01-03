package com.example.cursospring.service;

import java.awt.print.Pageable;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.cursospring.entity.Curso;
import com.example.cursospring.entity.vm.Asset;
import com.example.cursospring.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CursoServiceImp implements CursoService {
	private final static String BUCKET = "proyectospringboots3bucket";
	@Autowired
	private CursoRepository userR;

	@Autowired
	private AmazonS3Client s3Client;

	@Override
	@Transactional(readOnly=true)
	public Iterable<Curso> findAll() {
		return userR.findAll();
	}

	@Override
	public Page<Curso> findAll(Pageable pageable) {
		return (Page<Curso>) userR.findAll((Sort) pageable);
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<Curso> findById(Integer id) {
		return userR.findById(id);
	}

	@Override
	public Curso save(Curso user) {
		return userR.save(user);
	}

	@Override
	public void deleteById(Integer id) {
		userR.deleteById(id);
	}


	public String putObject(MultipartFile multipartFile){
		String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
		String key = String.format("%s.%s", UUID.randomUUID(), extension);//llave para acaceder al bucket

		ObjectMetadata objectMetadata= new ObjectMetadata();
		objectMetadata.setContentType(multipartFile.getContentType());

		try {
			PutObjectRequest putObjectRequest=new PutObjectRequest(BUCKET, key, multipartFile.getInputStream(), objectMetadata)
					.withCannedAcl(CannedAccessControlList.PublicRead);

			s3Client.putObject(putObjectRequest);
			return key;
		} catch (IOException e) {//	Error
			throw new RuntimeException(e);
		}
	}

	public Asset getObject(String key){
		S3Object s3Object = s3Client.getObject(BUCKET, key);
		ObjectMetadata metadata= s3Object.getObjectMetadata();

		try {
			S3ObjectInputStream inputStream = s3Object.getObjectContent();//Tipo de contenido=Visializar en el navegador la imagen, pdf, etc.
			byte[] bytes= IOUtils.toByteArray(inputStream);
			return new Asset(bytes, metadata.getContentType());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void deleteObject(String key){
		s3Client.deleteObject(BUCKET,key);
	}

	//Construir la url para los objetos publicos
	//En caso de que el cliente  quiera acceder desde una URL
	public String getObjectUrl(String key){
		return String.format("https://%s.s3.amazonaws.com/%s",BUCKET,key);
	}




}
