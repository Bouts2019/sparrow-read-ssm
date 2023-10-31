<template>
<div>
  <div class="more-info-wrapper">
    <ul class="more-list">
      <li class="more-list-item">
        <a href="/about" target="_blank">关于</a>
      </li>
      <li class="more-list-item">营业执照</li>
      <li class="more-list-item">使用指南</li>
    </ul>
    <ul class="more-list">
      <li class="more-list-item">用户协议</li>
      <li class="more-list-item">隐私政策</li>
      <li class="more-list-item">Cookie协议</li>
    </ul>
    <ul class="more-list">
      <li class="more-list-item">友情链接</li>
    </ul>
    <ul class="more-list">
      <li class="more-list-item">版权所有：天涯陌路人</li>
    </ul>
    <ul class="more-list">
      <li class="more-list-item">联系方式：QQ: 468244916</li>
    </ul>
    <ul class="more-list">
      <li class="more-list-item">&copy;2020</li>
    </ul>
  </div>
</div>
</template>

<script>
export default {
  name: "MoreInfo"
}
</script>

<style scoped>
.more-info-wrapper {
  font-size: 12px;
  color: #909090;
  display: flex;
  flex-direction: column;
}
.more-list {
  margin: 3px 0;
  padding: 0;
  list-style: none;
  display: flex;
}
.more-list-item a {
  text-decoration: none;
  color: #909090;
}
.more-list-item:nth-child(n + 2)::before {
  content: '·';
  margin: 0 5px;
}
</style>
