<template>
	<div>
		<div>
			<el-input size="small" v-model="jl.name"
			          placeholder="添加职称等级"
			          prefix-icon="el-icon-plus"
			          style="width: 300px">
			</el-input>
			<el-select size="small" v-model="jl.titleLevel" placeholder="职称等级"
			           style="margin-left: 6px; margin-right: 6px">
				<el-option
					v-for="item in titleLevels"
					:key="item"
					:label="item"
					:value="item">
				</el-option>
			</el-select>
			<el-button size="small" type="primary" icon="el-icon-plus" @click="addJobLevel">添加</el-button>
		</div>
		<div style="margin-top: 10px">
			<el-table
				:data="jls"
				stripe
				border
				size="small"
				style="width: 70%">
				<el-table-column
					prop="id"
					label="编号"
					width="55">
				</el-table-column>
				<el-table-column
					prop="name"
					label="职称名称"
					width="150">
				</el-table-column>
				<el-table-column
					prop="titleLevel"
					label="职称等级"
					width="150">
				</el-table-column>
				<el-table-column
					prop="createDate"
					label="创建日期"
					width="150">
				</el-table-column>
				<el-table-column
					prop="enabled"
					label="是否启用"
					width="150">
					<template slot-scope="scope">
						<el-tag v-if="scope.row.enabled" type="success">已启用</el-tag>
						<el-tag v-else type="danger">未启用</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="small">编辑</el-button>
						<el-button size="small" type="danger" @click="deleteHandler(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
	</div>
</template>

<script>
export default {
	name: "JoblevelMana",
	data() {
		return {
			jl: {
				name: '',
				titleLevel: ''
			},
			titleLevels: [
				'正高级',
				'副高级',
				'高级',
				'中级',
				'初级'
			],
			jls: []
		}
	},
	mounted() {
		this.initJls();
	},
	methods: {
		// 初始化页面, 获取所有职称信息
		initJls() {
			this.getRequest('/system/basic/joblevel/').then(resp => {
				if (resp) {
					this.jls = resp;
					this.jl.name = '';
					this.jl.titleLevel = '';
				}
			})
		},
		// 添加职称
		addJobLevel() {
			if (this.jl.name && this.jl.titleLevel) {
				this.postRequest('/system/basic/joblevel/', this.jl).then(resp => {
					if (resp) {
						this.initJls();
					}
				})
			} else {
				this.$message.error("字段不能为空哦 ^_^")
			}
		},
		// 删除职称
		deleteHandler(data){
			this.$confirm('此操作将永久删除[' + data.name + ']职称, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.deleteRequest('/system/basic/joblevel/' + data.id).then(resp => {
					if (resp) {
						console.log(resp);
						this.initJls();
					}
				})
			}).catch(() => {
				this.$message({
					type: 'info',
					message: '已取消删除'
				});
			});
		}
	}
}
</script>

<style scoped>

</style>