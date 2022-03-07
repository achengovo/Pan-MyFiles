<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 3/1 0001
  Time: 09:35:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/logo.png">
    <link rel="bookmark" href="${pageContext.request.contextPath}/static/images/logo.png">
    <title>MyFiles</title>
</head>
<!--引入Vue-->
<script src="${pageContext.request.contextPath}/static/js/vue.js"></script>
<!--引入axios-->
<script src="${pageContext.request.contextPath}/static/js/axios.min.js"></script>
<!-- 引入element ui组件库 -->
<script src="${pageContext.request.contextPath}/static/js/index.js"></script>
<!-- 引入element ui样式 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/index.css">
<!--引入页面样式-->
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/user.css"/>
<body>
<div id="app">
    <el-container class="my-el-container">
        <el-aside width="">
            <el-menu
                    @select="clickMenu"
                    default-active="3"
                    class="el-menu-vertical-demo"
                    @open="handleOpen"
                    @close="handleClose"
                    :collapse="isCollapse"
                    collapse-transition:true
                    left
            >
                <div @click="isCollapse=!isCollapse" style="height: 60px;line-height: 60px">
                    <i
                            v-if="isCollapse"
                            class="el-icon-arrow-right"
                    ></i>
                    <i
                            v-if="!isCollapse"
                            class="el-icon-arrow-left"
                    ></i>
                    <span v-if="!isCollapse" slot="title">收起</span>
                </div>
                <el-menu-item index="2">
                    <i class="el-icon-folder"></i>
                    <span slot="title">我的文件</span>
                </el-menu-item>
                <el-menu-item index="3">
                    <i class="el-icon-star-off"></i>
                    <span slot="title">个人中心</span>
                </el-menu-item>
            </el-menu>
        </el-aside>
        <el-container>
            <el-header>
                <el-row :gutter="24">
                    <el-col :span="22">
                        <img height="60" style="float: left;cursor: pointer;" onclick="window.location.href='file'"
                             src="${pageContext.request.contextPath}/static/images/logo1.png">
                    </el-col>
                    <el-col style="height: 60px;padding: 10px;" :span="2">
                        <el-dropdown
                                trigger="hover"
                                placement="bottom-end"
                                style="height: 40px;"
                                @command="avatarCommand"
                        >
                            <el-avatar
                                    src="${pageContext.request.contextPath}/static/avatar/${USER_SESSION.userAvatar}"></el-avatar>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item command="loginOut">
                                    退出登录
                                </el-dropdown-item>
                                <el-dropdown-item command="user">
                                    个人中心
                                </el-dropdown-item>
                            </el-dropdown-menu>

                        </el-dropdown>
                    </el-col>
                </el-row>
            </el-header>

            <el-main style="">
                <div style="background-color: white;height:99%">
                    <el-header style="height: 60px;">
                        <el-button-group style="width: 100%">
                            <el-button :class="{'el-button--primary':isone}" v-on:click="isone=true" style="width: 50%">用户信息</el-button>
                            <el-button :class="{'el-button--primary':!isone}" v-on:click="isone=false" style="width: 50%">修改密码</el-button>
                        </el-button-group>
                    </el-header>
                    <el-form :class="{'show':!isone}" :rules="rules" label-width="80px"
                             style="text-align: left;padding-left: 30px;padding-top: 30px;" ref="user" :model="user">
                        <el-form-item label="头像：" style="line-height: 54px;">
                            <el-avatar style="float: left;"
                                       :src="avatarSrc(user.userAvatar)"></el-avatar>
                            <el-upload
                                    name="uploadAvatar"
                                    class="upload-demo"
                                    action="${pageContext.request.contextPath}/uploadAvatar/"
                                    :on-change="handleChange"
                                    :show-file-list="false"
                                    :on-success="uploadSuccess">
                                <el-button style="margin-left: 20px;" size="small" type="primary">点击上传</el-button>
                            </el-upload>
                        </el-form-item>
                        <el-form-item prop="userName" label="用户名：">
                            <el-input autocomplete="off" v-model="user.userName" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item prop="userEmail" label="邮箱：">
                            <el-input v-model="user.userEmail" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item v-if="nowEmail!=user.userEmail" label="验证码：">
                            <el-input v-model="user.varCode" style="width: 200px">
                                <el-button slot="append" v-on:click="sendMail()" type="primary" style="width: 80px;">发送</el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item label="性别：">
                            <el-radio-group v-model="user.userSex">
                                <el-radio label="男"></el-radio>
                                <el-radio label="女"></el-radio>
                                <el-radio label="不公开"></el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item>
                            <el-button style="width:200px" type="primary" @click="changeUser">确定修改</el-button>
                        </el-form-item>
                        <el-form-item>
                            <el-button style="width:200px" @click="getUserInfo">取消</el-button>
                        </el-form-item>
                    </el-form>
                    <el-form :class="{'show':isone}" :rules="rules2" label-width="90px"
                             style="text-align: left;padding-left: 30px;padding-top: 30px;" ref="pass" :model="pass">
                        <el-form-item prop="userPass" label="密码：">
                            <el-input show-password placeholder="请输入当前密码" autocomplete="off" v-model="pass.userPass" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item prop="newPass" label="新密码：">
                            <el-input show-password placeholder="请输入新密码" autocomplete="off" v-model="pass.newPass" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item prop="reNewPass" label="确认密码：">
                            <el-input show-password placeholder="请再次输入新密码" autocomplete="off" v-model="pass.reNewPass" style="width: 200px"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button style="width:200px" type="primary" @click="changePassByPass()">确定修改</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-main>
        </el-container>
    </el-container>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/user.js"></script>
</html>