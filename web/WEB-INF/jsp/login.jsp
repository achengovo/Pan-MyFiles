<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/logo.png">
    <link rel="bookmark" href="${pageContext.request.contextPath}/static/images/logo.png">
    <title>MyFiles登录</title>
</head>
<!--引入Vue-->
<script src="${pageContext.request.contextPath}/static/js/vue.js"></script>
<!--引入axios-->
<script src="${pageContext.request.contextPath}/static/js/axios.min.js"></script>
<!-- 引入element ui组件库 -->
<script src="${pageContext.request.contextPath}/static/js/index.js"></script>
<!-- 引入element ui样式 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/index.css">

<body>
<div id="particles-js" style="position: fixed;">
    <link rel="stylesheet" media="screen" href="${pageContext.request.contextPath}/static/css/style.css">
    <script src="${pageContext.request.contextPath}/static/js/particles.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/app.js"></script>
    <div id="app">
        <el-card :body-style="{ padding: '0px' }" class="box-card"
                 style="width: 500px;height:460px;margin: calc(50vh - 230px) auto;">
            <img height="160px"
                 src="${pageContext.request.contextPath}/static/images/logo2.png"
                 class="image">
            <div style="padding:20px 80px 20px 80px;">
                <el-form action="${pageContext.request.contextPath}/login" method="POST" ref="dataAddForm"
                         :rules="rules" :model="formData" class="demo-ruleForm">
                    <el-form-item prop="userName">
                        <el-input placeholder="请输入用户名" name="userName" type="text" v-model="formData.userName"
                                  autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item prop="userPass" style="margin-bottom: 5px;">
                        <el-input show-password placeholder="请输入密码" name="userPass" type="password"
                                  v-model="formData.userPass" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item style="margin-bottom: 0;">
                        <el-checkbox v-model="formData.autoLogin">7天内自动登录</el-checkbox>
                        <input style="display: none;" type="checkbox" name="autoLogin" :value="formData.autoLogin"
                               checked="checked"/>
                        <el-link onclick="window.location.href='toForgetPass'" style="float: right;" :underline="false"
                                 type="primary">忘记密码
                        </el-link>
                    </el-form-item>
                    <el-form-item>
                        <button style="width: 100%;" type="button" v-on:click="submitlogin()"
                                class="el-button el-button el-button--primary">登录
                        </button>
                    </el-form-item>
                    <el-form-item>
                        <button style="width: 100%;" type="button" onclick="window.location.href='toRegister'"
                                class="el-button el-button el-button--primary">没有账号，去注册
                        </button>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>
    </div>
    <script src="${pageContext.request.contextPath}/static/js/login.js"></script>
</div>
</body>

</html>