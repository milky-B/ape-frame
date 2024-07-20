<#import "/spring.ftl"  as spring />
<#assign apiBasePath = "http://localhost:8081">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>应用导航</title>
    <link rel="icon" href="<@spring.url "/assets/photos/th.png" />" type="image/x-icon"/>
    <link rel="stylesheet" href="<@spring.url "/assets/element/index.css" />" media="all">

    <!-- 引入组件库 -->
    <script src="<@spring.url "/assets/js/jquery-3.4.1.min.js" />"></script>
    <script src="<@spring.url "/assets/element/vue.js" />"></script>
    <script src="<@spring.url "/assets/element/index.js" />"></script>
</head>

<body>
<div class="i_header">
    <div class="header_body">
        <h1 class="logo"><a href="/index"><img src="/assets/photos/logo.jpg"/></a></h1>
    </div>
</div>
<div id="app">
    <el-container>
        <el-aside width="200px"></el-aside>
        <el-main>
            <el-head>
                <div style="display: flex; align-items: center;">
                    <#--<el-image :src="photoSrc"></el-image>-->
                    <span style="font-size: 34px; margin-left: 10px"><b>物流应用门户首页</b></span>
                </div>
            </el-head>
            <div style="width: 80%">
                <div v-for="group in appGroupList">
                    <el-card class="box-card">
                        <h2>{{group.groupName}}</h2>
                        <template v-for="app in appList">
                            <div v-if="app.groupId === group.id" class="shadow"
                                 style="width: 22%; border: 1px #ddd solid; display: inline-block; margin: 15px;border-radius: 3px">
                                <div style="vertical-align: super; line-height: 0; padding: 10px">
                                    <el-link :href="app.homeUrl" target="_blank">
                                        <el-image
                                                style="width: 55px; height: 55px; border-radius: 10px"
                                                :src="app.icon"
                                                fit="fit"></el-image>
                                        <div style="display: inline-block; vertical-align: super">
                                            <h3 style="padding: 0 0 10px 0">{{app.appName}}</h3>
                                            <span style="padding-bottom: 3px">{{app.description}}</span>
                                        </div>
                                    </el-link>
                                </div>
                            </div>
                        </template>
                    </el-card>
                </div>
            </div>
        </el-main>
    </el-container>
</div>
</body>
<script>
    let app = new Vue({
        el: '#app',
        data: {
            appList: [{
                'appName': "123"
            }],
            appGroupList: [],
        },
        methods: {
            queryAppList() {
                let _this = this;
                let url = "${apiBasePath}/manage/app/queryValidCommonAppList";
                $.get(url, function (data) {
                    _this.appList = data.data;
                });
            },

            queryAppLGroupList() {
                let _this = this;
                let url = "${apiBasePath}/manage/app/queryValidAppGroupList";
                $.get(url, function (data) {
                    _this.appGroupList = data.data;
                });
            },
        },
        mounted: function () {
            this.queryAppLGroupList();
            this.queryAppList();
        },
    })
</script>

<style>
    .header_body {
        display: flex;
        align-items: center;
    }

    .box-card {
        width: 100%;
        border: 1px #deefef solid;
        box-shadow: 0 1px 1px rgba(0, 0, 0, 0.15) !important;
    }

    .shadow {
        float: left;
        margin-left: 20px;
        transition-duration: 0.5s;
    / / 停留时间显示
    }

    .shadow:hover {
        -webkit-box-shadow: #ccc 0px 10px 10px;
        -moz-box-shadow: #ccc 0px 10px 10px;
        box-shadow: #ccc 0px 10px 10px;
        cursor: pointer;
    }
</style>
</html>