<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.loginName" placeholder="登录名">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
        :columns="columns"
        :row-key="record => record.id"
        :data-source="users"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="resetPassword(record)">
                重置密码
              </a-button>
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-popconfirm
                title="删除后不可恢复，确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
              >
                <a-button type="primary" danger >
                  删除
                </a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
    title="用户表单"
    v-model:visible="modalVisible"
    :confirm-loading="modalLoading"
    @ok="handleModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="登录名">
        <a-input v-model:value="user.loginName" :disabled="user.id"/>
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.name" />
      </a-form-item>
      <a-form-item label="密码" v-show="!user.id">
        <a-input v-model:value="user.password"/>
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
    title="重置密码"
    v-model:visible="resetModalVisible"
    :confirm-loading="resetModalLoading"
    @ok="handleResetModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="新密码">
        <a-input v-model:value="user.password"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'AdminUser',
  setup() {
    const param = ref();
    param.value = {};
    const users = ref();
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '登录名',
        dataIndex: 'loginName'
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '密码',
        dataIndex: 'password'
      },
      {
        title: 'Action',
        key: 'action',
      }
    ];

    /**
     * 数据查询
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      users.value = [];
      axios.get("/user/list", {
        params: {
          page: params.page,
          size: params.size,
          loginName: param.value.loginName
        }
      }).then((response) => {
        loading.value = false
        const data = response.data;
        if(data.success){
          users.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        }
        else{
          message.error(data.message);
        }
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    /****表单****/
    const user = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      user.value.password = hexMd5(user.value.password + KEY);

      axios.post("/user/save", user.value).then((response) => {
        const data = response.data;
        modalLoading.value = false;
        if(data.success){
          modalVisible.value = false;

          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize
          });
        }
        else{
          message.error(data.message)
        }
      });
    };

    const edit = (record:any) => {
      modalVisible.value = true;
      user.value = Tool.copy(record);
    };

    const add = () =>{
      modalVisible.value = true;
      user.value = {};
    };

    const handleDelete = (id: number) =>{
      axios.delete("/user/delete/" + id).then((response) => {
        const data = response.data;
        if(data.success){
          //重新加载列表
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize
          });
        } else {
          message.error(data.message);
        }
      });
    };

    /****重置密码****/
    const resetModalVisible = ref(false);
    const resetModalLoading = ref(false);
    const handleResetModalOk = () => {
      resetModalLoading.value = true;

      user.value.password = hexMd5(user.value.password + KEY);

      axios.post("/user/reset-password", user.value).then((response) => {
        const data = response.data;
        resetModalLoading.value = false;
        if(data.success){
          resetModalVisible.value = false;

          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize
          });
        }
        else{
          message.error(data.message)
        }
      });
    };

    /**
     * 重置密码
     */
    const resetPassword = (record: any) => {
      resetModalVisible.value = true;
      user.value = Tool.copy(record);
      user.value.password = null;
    };


    onMounted(() =>{
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      })
    });

    return {
      edit,
      add,
      handleDelete,

      user,
      modalVisible,
      modalLoading,
      handleModalOk,

      resetModalVisible,
      resetModalLoading,
      handleResetModalOk,
      resetPassword,


      param,
      users,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,
    }
  }
});
</script>