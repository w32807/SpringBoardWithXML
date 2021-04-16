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
	// 파일업로드 방법 1. input type=File 태그 & form 태그 이용
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
		// 파일을 저장할 폴더 만들기
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		if(!uploadPath.exists()) uploadPath.mkdirs(); 
		for(MultipartFile multipartFile : uploadFile) {
			AttachFileDTO attachDTO = new AttachFileDTO();
			log.info("upload File Name : " + multipartFile.getOriginalFilename());
			log.info("upload Size : " + multipartFile.getSize());
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			// IE는 파일 이름을 가져오는 형식이 전체경로이며 그 이외의 브라우저는 파일이름 자체만 가져오므로 IE를 위한 처리를 해줌
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
			attachDTO.setFileName(uploadFileName);
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName;
			
			try {
				// 1. 파일 저장
				File saveFile = new File(uploadFolder, uploadFileName);
				multipartFile.transferTo(saveFile);
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				// 2. 파일 섬네일 처리 전 이미지 파일인지 확인
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
	// @RequestHeader로 request 값을 받아오며 바인딩 가능
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName){
		// Resource 타입????
		Resource resource = new FileSystemResource("c:\\upload\\" + fileName);
		if(!resource.exists()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		String resourceName = resource.getFilename();
		
		HttpHeaders headers = new HttpHeaders();
		try {
			// 파일 이름이 한글일 때 깨지는 현상을 방지하기 위한 header 설정 (IE는 인코딩 형식이 달라, User-Agent를 사용하여 처리
			String downloadName = null;
			if(userAgent.contains("Trident")) { // IE 브라우저 사용 시
				log.info("IE browser");
				downloadName = URLEncoder.encode(resourceName, "UTF-8").replace("\\+", " ");
			}else if(userAgent.contains("Edge")) { // Edge 브라우저 사용 시
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
		// File.separator는 운영체제에 따라 다르지만, java에서 대응해줌
		return str.replace("-", File.separator);
	}
	// Ajax는 브라우저를 통해서만 들어오는 것이 아니기 때문에 꼭 파일이 이미지인지를 확인해야 한다.
	private boolean checkImageType(File file) {
		try {
			// probeContentType는 파일의 확장자를 통해 타입을 확인한다 (파일의 내용이 아님)
			String contentType = Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
}
