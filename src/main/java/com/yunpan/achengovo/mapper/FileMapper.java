package com.yunpan.achengovo.mapper;

import com.yunpan.achengovo.entity.UserFile;

import java.util.List;

public interface FileMapper {
    //通过dir查询文件夹列表
    List<UserFile> getFileByDir(UserFile userFile);
    //通过fileId查询文件信息
    UserFile getFileByUserFileId(UserFile userFile);
    //删除文件更新userFiles表
    Integer delFileById(UserFile userFile);
    //重命名
    Integer reName(UserFile userFile);
    //移动文件
    Integer moveFile(UserFile userFile);
    //上传文件向userFiles表插入信息
    Integer insertToUserFiles(UserFile userFile);
    //新建文件夹向userFiles表插入信息
    Integer newDir(UserFile userFile);

}
