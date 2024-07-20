<#import "/spring.ftl"  as spring />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>物流项目脚手架</title>
    <link rel="icon" href="/assets/ptotos/th.png" type="image/x-icon"/>
    <link rel="stylesheet" href="/assets/element/index.css" media="all">

    <!-- 引入组件库 -->
    <script src="/assets/js/jquery-3.4.1.min.js"></script>
    <script src="/assets/element/vue.js"></script>
    <script src="/assets/element/index.js"></script>
</head>

<body>
<div id="app">
    <el-container>
        <el-aside width="200px"></el-aside>
        <el-main>
            <el-head>
                <div style="display: flex; align-items: center;">
                    <el-image :src="photoSrc"></el-image>
                    <span style="font-size: 34px; margin-left: 10px"><b>物流项目脚手架</b></span>
                </div>
            </el-head>
            <div style="width: 80%">
                <el-steps style="margin: 2% 5% 5% 5%" :active="active" finish-status="success">
                    <el-step title="步骤 1" description="选择项目模板" icon="el-icon-edit"></el-step>
                    <el-step title="步骤 2" description="填写项目信息" icon="el-icon-upload"></el-step>
                    <el-step title="步骤 3" description="导出项目" icon="el-icon-s-flag"></el-step>
                </el-steps>

                <el-form ref="formData" style="width: 60%; margin-left: 20%" label-position="right" label-width="auto" :rules="rules" :model="formData">
                    <el-form-item prop="type" :required="true" v-if="active === 0" label="项目类型：">
                        <el-select style="width: 80%" v-model="formData.type" placeholder="请选择">
                            <el-option label="web" value="web"></el-option>
                            <el-option label="datacenter" value="datacenter"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="artifactId" :required="true" v-if="active === 1" label="artifactId：">
                        <el-input v-model="formData.artifactId" placeholder="web_common_demo"></el-input>
                    </el-form-item>
                    <el-form-item prop="groupId" :required="true" v-if="active === 1" label="groupId：">
                        <el-input v-model="formData.groupId" placeholder="airport.cargos.demo"></el-input>
                    </el-form-item>
                    <el-form-item v-if="active === 1" label="description：">
                        <el-input v-model="formData.description"></el-input>
                    </el-form-item>
                </el-form>

                <div style="text-align: center; margin-top: 100px">
                    <el-button v-if="active > 0 && active < 2" style="margin-top: 12px;" @click="pre">上一步</el-button>
                    <el-button type="primary" v-if="active < 1" style="margin-top: 12px;" @click="next('formData')">下一步</el-button>
                    <el-button type="primary" v-if="active === 1" style="margin-top: 12px;" @click="next('formData')">创建项目</el-button>
                </div>
            </div>

            <el-result icon="success" title="项目生成中" subTitle="请不要关闭此页面，项目正在生成中...">
            </el-result>
        </el-main>
    </el-container>
</div>
</body>
<script>
    let app = new Vue({
        el: '#app',
        data: {
            photoSrc:'../assets/photos/logo.jpg',
            active: 0,
            formData: {
                type: "",
                artifactId: "",
                packageId: "",
                groupId: "",
                description: ""
            },
            rules: {
                type: [
                    {required: true, message: '请选择项目类型', trigger: 'change'}
                ],
                artifactId: [
                    {required: true, message: '请填写artifactId', trigger: 'change'}
                ],
                groupId: [
                    {required: true, message: '请填写groupId', trigger: 'change'}
                ],
            }
        },
        methods: {
            pre() {
                if (this.active-- < 0) {
                    return callback(new Error('年龄不能为空'));
                }
            },
            next(formName) {
                let _this = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.active === 1){
                            //调用接口
                            let baseUrl = "http://10.20.132.20:8180";
                            // let baseUrl = "http://localhost:8180";
                            window.open(baseUrl + "/generate/generateProject?type=" + _this.formData.type + "&artifactId=" +
                                _this.formData.artifactId + "&groupId=" + _this.formData.groupId + "&description=" + _this.formData.description);
                            window.location.reload()
                        }
                        if (this.active++ > 2) {
                            return
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        },
        mounted: function () {

        },
    })
</script>
</html>