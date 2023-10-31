<template>
<div style="margin-top: 10px;">
  <el-form ref="form" :model="form" label-width="80px">
    <el-form-item label="我的头像">
      <div style="margin-left: 20px; display: flex; align-items: center">
        <img :src="form.avatar" class="avatar" style="width: 60px; height: 60px; border: 1px solid #999; border-radius: 50%;" alt="">
        <el-upload
          class="upload-demo"
          :on-success="uploadSuccessfully"
          :on-change="fileChange"
          :show-file-list="false"
          action="http://localhost:8080/sparrow/user/avatar">
          <el-button type="primary" :plain="true" style="margin-left: 20px; height: 40px; width: 100px;">上传<i class="el-icon-upload el-icon--right"></i></el-button>
        </el-upload>
      </div>
    </el-form-item>
    <el-form-item label="我的昵称">
      <el-input v-model="form.nickname"></el-input>
    </el-form-item>
    <el-form-item label="我的生日">
      <el-col :span="11">
        <el-date-picker placeholder="选择日期" v-model="form.birthday"></el-date-picker>
      </el-col>
    </el-form-item>
    <el-form-item label="我是">
      <el-radio-group v-model="form.gender">
        <el-radio label="0">男生</el-radio>
        <el-radio label="1">女生</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="修改密码">
      <el-button type="text" style="" @click="changePasswordDialog = true">修改密码</el-button>
    </el-form-item>
    <el-form-item label="绑定手机">
      <el-link type="info" :underline="false" v-text="form.phone"></el-link>
<!--      <el-button type="text" style="margin-left: 30px;">修改绑定</el-button>-->
    </el-form-item>
    <el-form-item label="绑定邮箱">
      <el-link type="info" v-text="form.email === null ? '尚未绑定' : form.email"></el-link>
      <el-button type="text" style="margin-left: 30px;" @click="changeEmailDialog = true">修改绑定</el-button>
    </el-form-item>
    <el-form-item label="第三方">
      <el-link type="info">尚未绑定</el-link>
      <el-button type="text" style="margin-left: 30px;">修改绑定</el-button>
    </el-form-item>
    <el-form-item label="我的简介">
      <el-input type="textarea" v-model="form.description"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :plain="true" @click="saveUpdate">保存修改</el-button>
    </el-form-item>
  </el-form>
  <el-dialog
    title="提示"
    :visible.sync="changePasswordDialog"
    width="30%">
    <el-form ref="passwordForm" :model="passwordForm" label-width="80px">
      <el-form-item label="原始密码" label-width="">
        <el-input show-password v-model="passwordForm.originPassword" placeholder="原始密码"></el-input>
      </el-form-item>
    </el-form>
    <el-form ref="passwordForm" :model="passwordForm" label-width="80px">
      <el-form-item label="新的密码">
        <el-input show-password v-model="passwordForm.firstInputPassword" placeholder="请输入新的密码"></el-input>
      </el-form-item>
    </el-form>
    <el-form ref="passwordForm" :model="passwordForm" label-width="80px">
      <el-form-item label="确认密码">
        <el-input show-password v-model="passwordForm.secondInputPassword" placeholder="重新输入新密码"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
    <el-button @click="changePasswordDialog = false">取 消</el-button>
    <el-button type="primary" @click="confirmChangePassword">确 定</el-button>
  </span>
  </el-dialog>
  <el-dialog
    :close-on-click-modal="false"
    title="提示"
    :visible.sync="changeEmailDialog"
    width="30%">
    <el-form ref="passwordForm" :model="emailForm" label-width="80px">
      <el-form-item label="输入邮箱">
        <el-input v-model="emailForm.newEmailAddress" placeholder="输入邮箱"></el-input>
      </el-form-item>
    </el-form>
    <el-form ref="passwordForm" label-width="80px">
      <el-form-item label="验证码">
        <el-input v-model="emailForm.captcha" placeholder="输入验证码"></el-input>
        <el-button type="primary" style="margin-top: 20px;" @click="sendCaptcha()" :disabled="emailResendRemainTime !== 0 || sending" >
          <span v-if="emailResendRemainTime === 0" >{{sending ? '发送中...' : '发送验证码'}}</span>
          <span v-else>剩余{{emailResendRemainTime}}秒</span>
        </el-button>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
    <el-button @click="changeEmailDialog = false">取 消</el-button>
    <el-button type="primary" :disabled="!validated" @click="confirmChangeEmail">确 定</el-button>
  </span>
  </el-dialog>
</div>
</template>

<script>
export default {
  name: "HomeSettings",
  data() {
    return {
      form: {},
      imageUrl: '',
      changePasswordDialog: false,
      emailResendRemainTime: 0,
      emailForm: {
        newEmailAddress: '',
        captcha: ''
      },
      passwordForm: {
        originPassword: '',
        firstInputPassword: '',
        secondInputPassword: ''
      },
      changeEmailDialog: false,
      sending: false,
      validated: false
    }
  },
  methods: {
    confirmChangeEmail() {
      this.$http.put('http://localhost:8080/sparrow/captcha/' + this.me.uid, {
        captcha: this.emailForm.captcha
      }).then(res => {
        this.$alert('验证成功！(❁´◡`❁)', '验证成功', {
          type: 'success',
          callback: action => {
            this.changeEmailDialog = false
          }
        })
        this.form.email = this.emailForm.newEmailAddress
      }).catch(err => {
        this.$alert('验证失败(┬┬﹏┬┬), 检查验证码是否正确？', '验证失败', {
          type: 'error',
          callback: action => {
          }
        })
      })
    },
    sendCaptcha() {
      this.sending = true
      this.$http.post('http://localhost:8080/sparrow/captcha/' + this.me.uid, {
        email: this.emailForm.newEmailAddress
      }).then(res => {
        this.$alert('发送成功！', '成功', {
          type: 'success'
        })
        this.validated = true
        this.sending = false
        this.emailResendRemainTime = 60
        let handleNumber = window.setInterval(() => {
          this.emailResendRemainTime--
          if (this.emailResendRemainTime === 0) {
            window.clearInterval(handleNumber)
          }
        }, 1000)
      })
    },
    confirmChangePassword() {
      if (this.passwordForm.firstInputPassword === '' || this.passwordForm.secondInputPassword === '' || this.passwordForm.originPassword === '') {
        this.$alert('输入不能为空')
        return false
      }
      if (this.passwordForm.firstInputPassword !== this.passwordForm.secondInputPassword) {
        this.$alert('两次输入不一致')
        return false
      }
      let user = {}
      let loading = true
      this.$http.get('http://localhost:8080/sparrow/user/' + this.me.uid).then(res => {
        user = res.data.data
        if (user.password !== this.passwordForm.originPassword) {
          this.$alert('原始密码输入错误')
          return false
        }
        this.form.password = this.passwordForm.firstInputPassword
        this.$alert('修改成功，请点击保存修改', '修改成功', {
          type: 'success',
          callback: action => {
            this.changePasswordDialog = false
          }
        })
      })
    },
    uploadSuccessfully(resp, file, fileList) {
      this.$alert('上传成功', {
        type: 'success',
        callback: action => {
          this.form.avatar = resp.data
        }
      })
    },
    fileChange(file, fileList) {

    },
    saveUpdate() {
      this.$confirm('确认修改吗，确认后需要重新登录', '确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$http.put('http://localhost:8080/sparrow/user/' + this.me.uid, this.form).then(res => {
          this.$alert('修改成功, 重新登陆', '操作成功', {
            type: 'success',
            callback: action => {
              localStorage.removeItem('JWT_TOKEN')
              window.open('/login#login', '_blank');
            }
          })
        })
      }).catch(() => {
        console.log('cancel')
      })
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    }
  },
  props: {
    me: [Object]
  },
  watch: {
    me: function () {
      this.form = this.me
      this.form.gender += ''
    }
  }
}
</script>

<style scoped>

</style>
