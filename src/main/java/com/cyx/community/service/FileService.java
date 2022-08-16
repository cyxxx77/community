package com.cyx.community.service;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

@Service
public class FileService {

    public String getSavePath() {
        // 这里需要注意的是ApplicationHome是属于SpringBoot的类
        // 获取项目下resources/static/img路径
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        // 保存目录位置根据项目需求可随意更改
        return applicationHome.getDir().getParentFile()
                .getParentFile().getAbsolutePath() + "\\src\\main\\resources\\static\\imges\\";
    }

    public String uploadLocal(InputStream fileStream, String mimeType, String fileName){
           String uploadPath = getSavePath();
           File file = new File(uploadPath);
           if(!file.exists()){
               file.mkdirs();
           }
        String[] split = fileName.split("\\.");
        String suffix = split.length>1?split[split.length-1]:"";
        String filename = UUID.randomUUID().toString().replaceAll("-","") + "." + suffix;
        File destination = new File(uploadPath+filename);
        try {
            FileUtils.copyToFile(fileStream, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String absolutePath = destination.getAbsolutePath();
        String[] splitPath = absolutePath.split("\\\\");
        return "http://localhost:8888/"+splitPath[splitPath.length-2]+"/"+splitPath[splitPath.length-1];
    }


    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("C:\\Users\\86159\\Desktop\\db6414629cdd869878964b223d7513e.jpg");
        FileService fileService = new FileService();
        InputStream inputStream = new FileInputStream(file);
        String jpg = fileService.uploadLocal(inputStream, "jpg", "db6414629cdd869878964b223d7513e.jpg");
        String[] split = jpg.split("\\\\");
        jpg = "http:\\localhost:8888\\"+split[split.length-2]+"\\"+split[split.length-1];
        System.out.println(jpg);
    }
}
