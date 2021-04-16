package com.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.print.attribute.standard.Media;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	// ���Ͼ��ε� ��� 1. input type=File �±� & form �±� �̿�
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form");
	}
	
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		String uploadFolder = "C:\\upload";
		for(MultipartFile multipartFile : uploadFile) {
			log.info("upload File Name : " + multipartFile.getOriginalFilename());
			log.info("upload Size : " + multipartFile.getSize());
			
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			try {
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload Ajax");
	}
	
	@PostMapping(value = {"/uploadAjaxAction" }, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
		List<AttachFileDTO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload";
		String uploadFolderPath = getFolder();
		// ������ ������ ���� �����
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		if(!uploadPath.exists()) uploadPath.mkdirs(); 
		for(MultipartFile multipartFile : uploadFile) {
			AttachFileDTO attachDTO = new AttachFileDTO();
			log.info("upload File Name : " + multipartFile.getOriginalFilename());
			log.info("upload Size : " + multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			// IE�� ���� �̸��� �������� ������ ��ü����̸� �� �̿��� �������� �����̸� ��ü�� �������Ƿ� IE�� ���� ó���� ����
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			attachDTO.setFileName(uploadFileName);
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			try {
				// 1. ���� ����
				File saveFile = new File(uploadFolder, uploadFileName);
				multipartFile.transferTo(saveFile);
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				// 2. ���� ������ ó�� �� �̹��� �������� Ȯ��
				if(checkImageType(saveFile)) {
					attachDTO.setImage(true);
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.flush();
					thumbnail.close();
				}
				list.add(attachDTO);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("fileName : " + fileName);
		File file = new File("c:\\upload\\" + fileName);
		log.info("file : " + file);
		ResponseEntity<byte[]> result = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	@GetMapping(value = "/download" , produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	// @RequestHeader�� request ���� �޾ƿ��� ���ε� ����
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName){
		// Resource Ÿ��????
		Resource resource = new FileSystemResource("c:\\upload\\" + fileName);
		if(!resource.exists()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		String resourceName = resource.getFilename();
		
		HttpHeaders headers = new HttpHeaders();
		try {
			// ���� �̸��� �ѱ��� �� ������ ������ �����ϱ� ���� header ���� (IE�� ���ڵ� ������ �޶�, User-Agent�� ����Ͽ� ó��
			String downloadName = null;
			if(userAgent.contains("Trident")) { // IE ������ ��� ��
				log.info("IE browser");
				downloadName = URLEncoder.encode(resourceName, "UTF-8").replace("\\+", " ");
			}else if(userAgent.contains("Edge")) { // Edge ������ ��� ��
				log.info("Edge browser");
				downloadName = URLEncoder.encode(resourceName, "UTF-8");
			}else {
				log.info("Chrome browser");
				downloadName = new String(resourceName.getBytes("UTF-8"), "ISO-8859-1");
			}
			headers.add("Content-Disposition", "attachment; fileName=" + downloadName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		// File.separator�� �ü���� ���� �ٸ�����, java���� ��������
		return str.replace("-", File.separator);
	}
	// Ajax�� �������� ���ؼ��� ������ ���� �ƴϱ� ������ �� ������ �̹��������� Ȯ���ؾ� �Ѵ�.
	private boolean checkImageType(File file) {
		try {
			// probeContentType�� ������ Ȯ���ڸ� ���� Ÿ���� Ȯ���Ѵ� (������ ������ �ƴ�)
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
