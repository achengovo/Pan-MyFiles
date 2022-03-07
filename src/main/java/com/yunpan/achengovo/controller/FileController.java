package com.yunpan.achengovo.controller;
import com.yunpan.achengovo.entity.User;
import com.yunpan.achengovo.entity.UserFile;
import com.yunpan.achengovo.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.yunpan.achengovo.utils.judgeFileType.judgeType;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;
    /**
     * 移动文件
     * @param userFile
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/moveFile")
    public String moveFile(@RequestBody UserFile userFile, HttpSession session) {
        HashSet hashSet=new HashSet();
        if(!"".equals(userFile.getDir())){
            hashSet=getUpDir(Integer.valueOf(userFile.getDir()),hashSet,session);
            if(hashSet.contains(String.valueOf(userFile.getUserFileId()))){
                return "不能将文件夹移动到下级目录";
            }
        }
        User user = (User) session.getAttribute("USER_SESSION");
        userFile.setUserId(user.getUserId());
        if(fileService.moveFile(userFile)!=0){
            return "success";
        }else {
            return "移动失败";
        }
    }
    //递归获取上级目录
    public HashSet getUpDir(Integer userFileId,HashSet hashSet,HttpSession session){
        hashSet.add(String.valueOf(userFileId));
        User user = (User) session.getAttribute("USER_SESSION");
        UserFile userFile=new UserFile();
        userFile.setUserId(user.getUserId());
        userFile.setUserFileId(userFileId);
        userFile=fileService.getFileByUserFileId(userFile);
        if(userFile!=null){
            if(!"".equals(userFile.getDir())){
                hashSet.add(userFile.getDir());
                getUpDir(Integer.valueOf(userFile.getDir()),hashSet,session);
            }
        }
        return hashSet;
    }
    /**
     * 重命名
     * @param userFile
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/reName")
    public String reName(@RequestBody UserFile userFile, HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("USER_SESSION");
        userFile.setUserId(user.getUserId());
        if (fileService.reName(userFile)!=0) {
            return "success";
        } else {
            return "重命名失败";
        }
    }
    /**
     * 删除
     * @param userFile
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delFileById")
    public String delFileById(@RequestBody UserFile userFile, HttpServletRequest request, HttpSession session) {
        if (delFileByIdType(userFile,session)) {
            return "success";
        } else {
            return "删除失败";
        }
    }
    //递归删除
    public boolean delFileByIdType(UserFile userFile,HttpSession session) {
        User user = (User) session.getAttribute("USER_SESSION");
        userFile.setUserId(user.getUserId());
        userFile = fileService.getFileByUserFileId(userFile);
        if (fileService.delFileById(userFile) != 0) {
            if("dir".equals(userFile.getFileType())){
                userFile.setDir(String.valueOf(userFile.getUserFileId()));
                List<UserFile> userFileList=fileService.getFileByDir(userFile);
                for (UserFile file:userFileList) {
                    if(delFileByIdType(file,session)){
                        continue;
                    };
                }
            }
        }
        return true;
    }

    @ResponseBody
    @RequestMapping(value = "/getFileList", method = RequestMethod.GET)
    public List<UserFile> getFileList(@RequestParam("dir") String dir, HttpSession session) {
        User user = (User) session.getAttribute("USER_SESSION");
        UserFile userFile = new UserFile();
        userFile.setDir(dir);
        userFile.setUserId(user.getUserId());
        System.out.println(userFile);
        return fileService.getFileByDir(userFile);
    }
    @ResponseBody
    @RequestMapping("/view")
    public ResponseEntity<byte[]> view(@RequestParam("userFileId") Integer userFileId, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        User user = (User) session.getAttribute("USER_SESSION");
        UserFile userFile = new UserFile();
        userFile.setUserFileId(userFileId);
        userFile.setUserId(user.getUserId());
        UserFile userFile1 = new UserFile();
        userFile1 = fileService.getFileByUserFileId(userFile);
//        String path="D://桌面/upload/"+userFile1.getFileLocation();
        String path = request.getServletContext().getRealPath("/upload/");
        File file = new File(path + File.separator + userFile1.getFileLocation());
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentDispositionFormData("attachment", this.getEncodeName(request, userFile1.getUserFileName()));
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);

    }
    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("userFileId") Integer userFileId, HttpServletRequest request, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("USER_SESSION");
        UserFile userFile = new UserFile();
        userFile.setUserFileId(userFileId);
        userFile.setUserId(user.getUserId());
        UserFile userFile1 = new UserFile();
        userFile1 = fileService.getFileByUserFileId(userFile);
//        String path="D://桌面/upload/"+userFile1.getFileLocation();
        String path = request.getServletContext().getRealPath("/upload/");
        File file = new File(path + File.separator + userFile1.getFileLocation());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", this.getEncodeName(request, userFile1.getUserFileName()));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * 根据浏览器的不同进行编码设置，返回编码后的文件名
     */
    public String getEncodeName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String[] IEBrowserKeyWords = {"MSIE", "Trident", "Edge"};
        String userAgent = request.getHeader("User-Agent");
        for (String keyWord : IEBrowserKeyWords) {
            if (userAgent.contains(keyWord)) {
                return URLEncoder.encode(fileName, "UTF-8");
            }
        }
        return new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
    }
    /**
     * 上传文件
     * @param uploadfile
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/toUpload",method = RequestMethod.POST)
    public String upload(
            @RequestParam("uploadfile") List<MultipartFile> uploadfile,
            @RequestParam("nowDirId") String nowDirId,
            HttpServletRequest request, HttpSession session) {
        User user= (User) session.getAttribute("USER_SESSION");
//        System.out.println(name);
        System.out.println(nowDirId);
        //判断上传文件是否存在
        if (!uploadfile.isEmpty() && uploadfile.size() > 0) {
            String newFilename="";
            //循环输出上传的文件
            for (MultipartFile file : uploadfile) {
                //上传文件的原始名称
                String originalFilename = file.getOriginalFilename();
                //设置上传文件的保存地址目录
//                String dirPath = "D://桌面/upload/";
                String dirPath=request.getServletContext().getRealPath("/upload/");
                File filePath = new File(dirPath);
                //如果保存文件的地址不存在就先创建目录
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                //使用UUID重新命名上传的文件名称
                newFilename = UUID.randomUUID() + "_" + originalFilename;
                try {
                    file.transferTo(new File(dirPath + newFilename));
                    UserFile userFile=new UserFile();
                    userFile.setUserFileName(originalFilename);
                    userFile.setUserId(user.getUserId());
                    userFile.setFileType(judgeType(originalFilename));
                    userFile.setFileLocation(newFilename);
                    userFile.setDir(nowDirId);
                    userFile.setUserFileSize(String.valueOf(file.getSize()));
                    if(fileService.insertToUserFiles(userFile)==0){
                        return "error";
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error";
                }
            }
            return "success";
        } else {
            return "error";
        }
    }
    /**
     * 新建目录
     * fileId, fileType,fileLocation
     */
    @ResponseBody
    @RequestMapping("/newDir")
    public String newDir(@RequestBody UserFile userFile,
                         HttpSession session){
        System.out.println(userFile);
        User user= (User) session.getAttribute("USER_SESSION");
        userFile.setUserId(user.getUserId());
        if(fileService.newDir(userFile)!=0){
            return "success";
        }
        return "创建失败";
    }
}
