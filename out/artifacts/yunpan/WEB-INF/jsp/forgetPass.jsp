<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 3/3 0003
  Time: 13:45:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>忘记密码</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/static/images/logo.png">
    <link rel="bookmark" href="${pageContext.request.contextPath}/static/images/logo.png">
</head>
<!--引入Vue-->
<script src="${pageContext.request.contextPath}/static/js/vue.js"></script>
<!--引入axios-->
<script src="${pageContext.request.contextPath}/static/js/axios.min.js"></script>
<!-- 引入element ui组件库 -->
<script src="${pageContext.request.contextPath}/static/js/index.js"></script>
<!-- 引入element ui样式 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/index.css">
<style>
    body{
        background-color:#E9EEF3;
    }
</style>
<body>
<div id="app">
    <el-card class="box-card"
             style="width: 500px;height:460px;margin: calc(50vh - 230px) auto;background-color: white">
        <el-steps :active="active" align-center finish-status="success">
            <el-step title="确认用户名" description="请输入您的用户名"></el-step>
            <el-step title="邮箱验证" description="请输入绑定的邮箱收到的验证码"></el-step>
            <el-step title="新密码" description="请输入您的新密码"></el-step>
            <el-step title="修改完成" description="密码重置完成，请重新登录"></el-step>
        </el-steps>
        <div style="height: 230px">
            <el-input v-if="this.active==0" style="margin-top: 80px;" v-model="userName" placeholder="请输入用户名"></el-input>
            <el-input v-if="this.active==1" style="margin-top: 80px;" v-model="varCode" placeholder="请输入验证码"></el-input>
            <el-input v-if="this.active==2" show-password style="margin-top: 40px;" v-model="newPass" placeholder="请输入新密码"></el-input>
            <el-input v-if="this.active==2" show-password style="margin-top: 40px;" v-model="reNewPass" placeholder="请确认新密码"></el-input>
            <el-result v-if="this.active==3" icon="success" title="修改成功" subTitle="请使用新密码进行登录">
                <template slot="extra">
                    <el-button type="primary" size="medium" onclick="window.location.href='toLogin'">去登录</el-button>
                </template>
            </el-result>
        </div>
        <div v-if="this.active!=3">
            <el-button style="width: 100%" type="primary" @click="next">下一步</el-button>
        </div>
    </el-card>
</div>
<script src="${pageContext.request.contextPath}/static/js/forgetPass.js"></script>
</body>
</html>
