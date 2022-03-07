package com.yunpan.achengovo.service;

import com.yunpan.achengovo.entity.UserFile;
import com.yunpan.achengovo.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    FileMapper fileMapper;
    //通过dir查询文件夹列表
    public List<UserFile> getFileByDir(UserFile userFile) {
        return fileMapper.getFileByDir(userFile);
    }
    //通过fileId查询文件信息
    public UserFile getFileByUserFileId(UserFile userFile){
        return fileMapper.getFileByUserFileId(userFile);
    }
    //删除文件更新userFiles表
    public Integer delFileById(UserFile userFile){
        return fileMapper.delFileById(userFile);
    }
    //重命名
    public Integer reName(UserFile userFile){
        return fileMapper.reName(userFile);
    }
    //移动文件
    public Integer moveFile(UserFile userFile){
        return fileMapper.moveFile(userFile);
    }
    //上传文件向userFiles表插入信息
    public Integer insertToUserFiles(UserFile userFile){
        return fileMapper.insertToUserFiles(userFile);
    }
    //新建文件夹向userFiles表插入信息
    public Integer newDir(UserFile userFile){
        return fileMapper.newDir(userFile);
    }
}
