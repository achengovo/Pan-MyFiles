# Pan-MyFiles

## 仓库地址

Gitee:[https://gitee.com/achengovo/Pan-MyFiles](https://gitee.com/achengovo/Pan-MyFiles)

GitHub:[https://github.com/achengovo/Pan-MyFiles](https://github.com/achengovo/Pan-MyFiles)

## 预览地址

[https://pan.achengovo.com](https://pan.achengovo.com)

预览账号：test 密码：test

## 介绍

Java web课程设计项目、Spring MVC、SSM、Vue、Element UI
MyFiles个人网盘系统已完成文件登录、注册、自动登录、邮件验证码、文件上传、文件下载、文件预览、移动、删除、重命名、新建文件夹、编辑个人资料、修改密码、找回密码等功能。

## 软件架构

Spring MVC、SSM、Vue、Element UI、MySQL

## 安装教程

1. 导入数据库sql文件，修改db.properties中数据库用户名、密码
2. 修改SendMail.java中的邮箱和授权码
3. 在Tomcat中运行

## 功能说明

### 登录

使用账号密码登录，登录成功后跳转到主页面。

### 注册

输入用户名、密码、邮箱、邮箱验证码，注册成功后跳转到登录页面。

### 自动登录

自动登录，登录成功后跳转到主页面。

### 文件上传

支持多文件上传

### 文件下载

支持多文件下载，不支持文件夹下载

### 文件预览

支持预览的文件类型

- 音频文件 mp3
- 视频文件 mp4、mkv、webm、ogg
- 文档文件 pdf
- 图片文件 jpg、jpeg、png、gif、ico
- 文本文件 txt、xml、html、js、css、py、json、java

### 移动

支持移动文件和文件夹

### 删除

支持删除文件和文件夹

### 重命名

支持重命名文件和文件夹

### 新建文件夹

可自定义文件夹名称

### 编辑个人资料

修改个人资料头像、用户名、性别、邮箱（需验证邮箱）、密码（需验证原密码）

### 找回密码

用户名->邮箱->邮箱验证码->新密码->确认新密码->修改密码->登录