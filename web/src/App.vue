<template>
  <a-layout id="components-layout-demo-top-side-2">
   <TheHeader></TheHeader>
    <router-view/>
   <TheFooter></TheFooter>
  </a-layout>
</template>

<script lang="ts">
  import {defineComponent} from 'vue';
  import TheHeader from "@/components/the-header.vue";
  import TheFooter from "@/components/the-footer.vue";

  export default defineComponent({
    name: 'app',
    components:{
      TheHeader,
      TheFooter,
    },
  });
</script>
<!--<script lang="ts" setup>-->
<!--import { ref } from 'vue';-->
<!--import { UserOutlined, LaptopOutlined, NotificationOutlined } from '@ant-design/icons-vue';-->
<!--const selectedKeys1 = ref<string[]>(['2']);-->
<!--const selectedKeys2 = ref<string[]>(['1']);-->
<!--const openKeys = ref<string[]>(['sub1']);-->
<!--</script>-->
<style scoped>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

/*.ant-row-rtl #components-layout-demo-top-side-2 .logo {*/
/*  float: right;*/
/*  margin: 16px 0 16px 24px;*/
/*}*/

/*.site-layout-background {*/
/*  background: #fff;*/
/*}*/
</style>