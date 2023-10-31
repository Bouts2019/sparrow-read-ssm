<template>
  <div class="tinymce-editor">
    <Editor v-model="myValue"
            :init="init"
            :disabled="disabled"
            @onClick="onClick">
    </Editor>
  </div>
</template>

<script>
import tinymce from 'tinymce/tinymce'
import Editor from '@tinymce/tinymce-vue'
import 'tinymce/themes/silver'
import 'tinymce/plugins/image';
import 'tinymce/plugins/media';
import 'tinymce/plugins/table';
import 'tinymce/plugins/lists';
import 'tinymce/plugins/contextmenu';
import 'tinymce/plugins/wordcount';
import 'tinymce/plugins/colorpicker';
import 'tinymce/plugins/textcolor';
import 'tinymce/plugins/codesample';
import 'tinymce/plugins/preview'
import 'tinymce/icons/default/icons'

export default {
  name: "Tinymce",
  components: {
    Editor
  },
  props: {
    //传入一个value，使组件支持v-model绑定
    value: {
      type: String,
      default: ''
    },
    disabled: {
      type: Boolean,
      default: false
    },
    plugins: {
      type: [String, Array],
      default: 'lists image media table wordcount codesample preview'
    },
    toolbar: {
      type: [String, Array],
      default: 'undo redo | formatselect | bold italic codesample | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | lists image media table | removeformat | preview'
    }
  },
  data() {
    return {
      //初始化配置
      init: {
        // inline: true,
        language_url: '../../../static/tinymce/langs/zh_CN.js',// 语言包的路径
        language: 'zh_CN',//语言
        skin_url: '../../../static/tinymce/skins/ui/oxide',// skin路径
        content_css: "../../../static/tinymce/skins/content/default/content.css",
        // height: this.window.document.documentElement.scrollHeight ,//编辑器高度
        height: 800,//编辑器高度
        plugins: this.plugins,
        toolbar: this.toolbar,
        branding: true,//是否禁用“Powered by TinyMCE”
        menubar: true,//顶部菜单栏显示
        //此处为图片上传处理函数，这个直接用了base64的图片形式上传图片，
        //如需ajax上传可参考https://www.tiny.cloud/docs/configure/file-image-upload/#images_upload_handler
        images_upload_handler: (blobInfo, success, failure) => {
          console.log(blobInfo)
          // const img = 'data:image/jpeg;base64,' + blobInfo.base64();
          let formData = new FormData()
          formData.append('image', blobInfo.blob(), 'abc')
          let config = {
            headers:{'Content-Type':'multipart/form-data'}
          };
          this.$http.post('http://localhost:8080/sparrow/editor/uploadPic', formData, config).then(res => {
            success(res.data.data)
          })
          // success('https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1608171766476&di=12e8abb5bbee099dc3b21a6fbd366433&imgtype=0&src=http%3A%2F%2Fpic.jj20.com%2Fup%2Fallimg%2F1113%2F041620103S8%2F200416103S8-4-1200.jpg')
        }
      },
      myValue: this.value
    }
  },
  mounted() {
    tinymce.init({});
  },
  methods: {
    //添加相关的事件，可用的事件参照文档=> https://github.com/tinymce/tinymce-vue => All available events
    //需要什么事件可以自己增加
    onClick(e) {
      this.$emit('onClick', e, tinymce)
    },
    //可以添加一些自己的自定义事件，如清空内容
    clear() {
      this.myValue = ''
    }
  },
  watch: {
    value(newValue) {
      this.myValue = newValue
    },
    myValue(newValue) {
      this.$emit('input', newValue)
    }
  }
}

</script>

<style scoped>
</style>
