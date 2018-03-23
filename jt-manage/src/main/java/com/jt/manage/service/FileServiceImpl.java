package com.jt.manage.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;

@Service
public class FileServiceImpl implements FileService {

	@Value("${localpath}")
	private String LPath;
	@Value("${urlpath}")
	private String uPath;
	
	/**
	 * 文件上传步骤
	 * 1.判断是否为图片 .png|.jpg|.gif
	 * 2.判断是否为非法程序 通过BufferedImage对象
	 * 3.为提高效率,使用分文件夹存储
	 * 4.让图片名称不一致
	 * 5.文件写入
	 * 6.确定返回URL
	 */
	@Override
	public PicUploadResult uploadFile(MultipartFile uploadFile) {
		PicUploadResult result = new PicUploadResult();
		String fileName = uploadFile.getOriginalFilename();
		if(!fileName.matches("^.*(jpg|png|gif|jpeg)$")){
			result.setError(1);
			return result;
		}
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			int height = bufferedImage.getHeight();
			int width = bufferedImage.getWidth();
			if(height==0||width==0){
				result.setError(1);
				return result;
			}
			//String LPath="E:/jt-upload/";
			String datePath=new SimpleDateFormat("yyyy/MM/dd/HH").format(new Date());
			String filePath = LPath+datePath;
			File file = new File(filePath);
			if(!file.exists()){
				file.mkdirs();
			}
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			String uuid = UUID.randomUUID().toString().replace("-", "");
			String realPath = filePath+"/"+uuid+fileType;
			uploadFile.transferTo(new File(realPath));
			
			//String uPath = "http://image.jt.com/";
			String realUrlPath = uPath+datePath+"/"+uuid+fileType;
			result.setUrl(realUrlPath);
			result.setHeight(height+"");
			result.setWidth(width+"");
			return result;		
		} catch (Exception e) {
			e.printStackTrace();
			result.setError(1);
			return result;
		}
	}

}
