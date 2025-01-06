<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-button type="primary" @click="add()" size:large>
          新增
        </a-button>
      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.dataIndex === 'cover'">
            <img v-if="record.cover" :src="record.cover" alt="avatar"/>
          </template>
          <template v-if="column.key === 'action'">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)">
                编辑
              </a-button>
              <a-popconfirm
                  title="删除后不可恢复，确认删除?"
                  ok-text="是"
                  cancel-text="否"
                  @confirm="handelDelete(record.id)"
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
      title="电子书表单"
      v-model:open="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类1">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类2">
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>

</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import axios from 'axios';
export default defineComponent({
  name: 'AdminEbook',
  setup() {
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '封面',
        dataIndex: 'cover',
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '分类1',
        dataIndex: 'category1Id'
      },
      {
        title: '分类2',
        dataIndex: 'category2Id'
      },
      {
        title: '文档数',
        dataIndex: 'docCount'
      },
      {
        title: '阅读数',
        dataIndex: 'viewCount'
      },
      {
        title: '点赞数',
        dataIndex: 'voteCount'
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

      axios.get("/ebook/list", {
        params:{
          page:params.page,
          size:params.size
        }
      }).then((response) => {
        loading.value = false
        const data = response.data;
        ebooks.value = data.content.list;

          // 重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
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

    const ebook = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/ebook/save", ebook.value).then((response) => {
        const data = response.data;
        if(data.success){
          modalVisible.value = false;
          modalLoading.value = false;

          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize
          });
        }
      });
    };

    const edit = (record:any) => {
      modalVisible.value = true;
      ebook.value = record;
    };

    const add = () =>{
      modalVisible.value = true;
      ebook.value = {};
    };

    const handelDelete = (id: number) =>{
      axios.delete("/ebook/delete/" + id).then((response) => {
        const data = response.data;
        if(data.success){
          //重新加载列表
          handleQuery({
            page:pagination.value.current,
            size:pagination.value.pageSize
          });
        }
      });
    };

    onMounted(() =>{
      handleQuery({
        page:1,
        size:pagination.value.pageSize
      });
    });



    return {
      edit,
      add,
      handelDelete,

      modalVisible,
      modalLoading,
      handleModalOk,
      ebook,

      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange
    }
  }
});
</script>