<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/logo.png">
    <link rel="bookmark" href="${pageContext.request.contextPath}/static/images/logo.png">
    <title>MyFiles注册</title>
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
                 style="width: 500px;height:605px;margin: calc(50vh - 300px) auto;">
            <img height="160px"
                 src="${pageContext.request.contextPath}/static/images/logo2.png"
                 class="image">
            <div style="padding:20px 80px 20px 80px;">
                <el-form action="${pageContext.request.contextPath}/register" method="POST" ref="dataAddForm"
                         :rules="rules" :model="formData"
                         class="demo-ruleForm">
                    <el-form-item prop="userName">
                        <el-input placeholder="请输入用户名" name="userName" type="text" v-model="formData.userName"
                                  autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item prop="userPass">
                        <el-input show-password placeholder="请输入密码" name="userPass" type="password"
                                  v-model="formData.userPass" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item prop="reUserPass">
                        <el-input show-password placeholder="确认密码" name="reUserPass" type="password"
                                  v-model="formData.reUserPass" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item prop="userEmail">
                        <el-input placeholder="请输入邮箱" name="userEmail" type="text" v-model="formData.userEmail"
                                  autocomplete="off">
                            <el-button slot="append" v-on:click="sendMail()" type="primary" style="width: 28%;margin: 0">发送</el-button>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="verCode">
                        <el-input placeholder="请输入验证码" name="verCode" type="text"
                                  v-model="formData.verCode" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item style="margin-bottom: 15px;">
                        <button style="width: 100%;" type="button" v-on:click="submitregister()"
                                class="el-button el-button el-button--primary">注册
                        </button>
                    </el-form-item>
                    <el-form-item>
                        <button style="width: 100%;" type="button" onclick="window.location.href='toLogin'"
                                class="el-button el-button el-button--primary">已有账号，去登录
                        </button>
                    </el-form-item>
                </el-form>
            </div>
        </el-card>
    </div>
    <script src="${pageContext.request.contextPath}/static/js/register.js"></script>
</div>
</body>

</html>