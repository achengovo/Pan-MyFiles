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
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/file.css"/>
<body>
<div id="app">
    <el-drawer
            title="上传列表"
            :visible.sync="drawer"
            :direction="direction">
  <span><el-upload style="padding: 10px;"
                   multiple
                   :data="{nowDirId:nowDirId}"
                   name="uploadfile"
                   class="upload-demo"
                   action="${pageContext.request.contextPath}/toUpload/"
                   :on-change="handleChange"
                   :file-list="uploadList"
                   :on-success="getFilesByDir">
    <el-button size="small" type="primary">点击上传</el-button>
  </el-upload></span>
    </el-drawer>
    <el-container class="my-el-container">
        <el-aside width="">
            <el-menu
                    @select="clickMenu"
                    default-active="2"
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
                    <el-col :span="16">
                        <img height="60" style="float: left;cursor: pointer;" onclick="window.location.href='file'"
                             src="${pageContext.request.contextPath}/static/images/logo1.png">
                    </el-col>
                    <el-col :span="6">
                        &nbsp;
<%--                        <el-input--%>
<%--                                class="my-search-input"--%>
<%--                                @keyup.enter.native="search"--%>
<%--                                v-model="searchtxt"--%>
<%--                                clearable--%>
<%--                                placeholder="请输入内容">--%>
<%--                            <i slot="prefix" class="el-input__icon el-icon-search"></i>--%>
<%--                        </el-input>--%>
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
            <el-header
                    style="background-color: #e9eef3; padding:10px 30px 10px 20px; height: auto;"
                    class="my-el-breadcrumb"
            >
                <el-row :gutter="24">
                    <el-col :span="14">
                        <el-breadcrumb
                                style="line-height: 28px;"
                                separator-class="el-icon-arrow-right"
                        >
                            <el-breadcrumb-item v-for="(item,index) in dirList">
                                <i v-if="index==0" class="el-icon-folder"></i>
                                <a v-on:click="toDir(item)">{{item.userFileName}}</a>
                            </el-breadcrumb-item>
                        </el-breadcrumb>
                    </el-col>
                    <el-col style="line-height: 20px;padding-right: 0;" :span="10">
                        <div style="height: 28px;">
                            <el-row>
                                <el-button-group style="float:right;margin:0 auto;">
                                    <el-button v-if="multipleSelection.length!=0 && moveList.length==0" type="primary"
                                               v-on:click="downLoadList()" size="mini">下载
                                    </el-button>
                                    <el-button v-if="multipleSelection.length!=0 && moveList.length==0" type="danger"
                                               v-on:click="delList()" size="mini">删除
                                    </el-button>
                                    <el-button v-if="multipleSelection.length==1 && moveList.length==0" type="primary"
                                               size="mini" v-on:click="reNameList()">重命名
                                    </el-button>
                                    <el-button v-if="moveList.length!=0" type="primary" size="mini"
                                               v-on:click="moveList=[]">取消移动
                                    </el-button>
                                    <el-button v-if="moveList.length!=0" type="primary" size="mini"
                                               v-on:click="moveToThis()">移动到此
                                    </el-button>
                                    <el-button v-if="multipleSelection.length!=0 && moveList.length==0" type="primary"
                                               size="mini" v-on:click="addToMoveList()">移动
                                    </el-button>
                                    <el-button type="primary" @click="newDir()" size="mini">新建文件夹</el-button>
                                    <el-button type="primary" @click="drawer = true" size="mini">上传文件</el-button>
                                </el-button-group>
                            </el-row>
                        </div>
                    </el-col>
                </el-row>
            </el-header>
            <el-main style="padding-top: 0px;">
                <el-table
                        ref="multipleTable"
                        :data="tableData"
                        height="calc(100vh - 150px)"
                        style="width: 100%;"
                        @row-click="doPriveFile"
                        @selection-change="handleSelectionChange">
                    >
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column prop="userFileName" sortable label="名称" width="300">
                        <template slot-scope="scope">
                            <div>
                                <!--图片-->
                                <i
                                        v-if="scope.row.fileType=='pic'"
                                        class="el-icon-picture-outline"
                                ></i>
                                <!--文件夹-->
                                <i v-if="scope.row.fileType=='dir'" class="el-icon-folder"></i>
                                <!--word-->
                                <i v-if="scope.row.fileType=='word'" class=""><img width="15" height="15" src="${pageContext.request.contextPath}/static/images/file-word.png"></i>
                                <!--excel-->
                                <i v-if="scope.row.fileType=='excel'" class=""><img width="15" height="15" src="${pageContext.request.contextPath}/static/images/file-excel.png"></i>
                                <!--ppt-->
                                <i v-if="scope.row.fileType=='ppt'" class=""><img width="15" height="15" src="${pageContext.request.contextPath}/static/images/file-ppt.png"></i>
                                <!--pdf-->
                                <i v-if="scope.row.fileType=='pdf'" class=""><img width="15" height="15" src="${pageContext.request.contextPath}/static/images/pdf.png"></i>
                                <!--text-->
                                <i v-if="scope.row.fileType=='text'" class=""><img width="15" height="15" src="${pageContext.request.contextPath}/static/images/text.png"></i>
                                <!--music-->
                                <i v-if="scope.row.fileType=='music'" class=""><img width="15" height="15" src="${pageContext.request.contextPath}/static/images/音乐.png"></i>
                                <!--未知-->
                                <i v-if="scope.row.fileType==null"><img width="15" height="15" src="${pageContext.request.contextPath}/static/images/未知.png"></i>
                                <!--视频-->
                                <i v-if="scope.row.fileType=='video'" class="el-icon-video-camera"></i>
                                {{scope.row.userFileName}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column align="right">
                        <template slot-scope="scope">
                            <el-dropdown
                                    trigger="hover"
                                    placement="bottom-end"
                                    @command="handleCommand"
                            >
                                <el-button @click.stop="" class="my-hover-button">
                                    <i
                                            style="margin-left: 0px;"
                                            class="el-icon-more el-icon--right"
                                    ></i>
                                </el-button>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item
                                            :command="composeValue('download',scope.row)"
                                    >
                                        下载
                                    </el-dropdown-item>
                                    <el-dropdown-item
                                            :command="composeValue('del',scope.row)"
                                    >
                                        删除
                                    </el-dropdown-item>
                                    <el-dropdown-item
                                            :command="composeValue('move',scope.row)"
                                    >
                                        移动
                                    </el-dropdown-item>
                                    <el-dropdown-item
                                            :command="composeValue('rename',scope.row)"
                                    >
                                        重命名
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </template>
                    </el-table-column>
                    <el-table-column
                            sortable
                            prop="userFileSaveTime"
                            label="保存时间"
                            align="right"
                            width="180"
                    ></el-table-column>

                    <el-table-column
                            prop="userFileSize"
                            label="大小"
                            align="right"
                            width="100"
                    >
                        <template slot-scope="scope">
                            {{change(scope.row.userFileSize)}}
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
        </el-container>
    </el-container>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/js/file.js"></script>
</html>
