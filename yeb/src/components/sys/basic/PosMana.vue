<template>
	<div>
		<div>
			<el-input
				size="small"
				class="addPosInput"
				placeholder="添加职位"
				suffix-icon="el-icon-plus"
				@keydown.enter.native="addPosition"
				v-model="pos.name">
			</el-input>
			<el-button size="small" icon="el-icon-plus" type="primary" @click="addPosition">添加</el-button>
		</div>
		<div class="posManaMain">
			<el-table
				size="small"
				stripe
				border
				:data="positions"
				style="width: 70%">
				<el-table-column
					type="selection"
					width="55">
				</el-table-column>
				<el-table-column
					prop="id"
					label="编号"
					width="55">
				</el-table-column>
				<el-table-column
					prop="name"
					label="职位"
					width="120">
				</el-table-column>
				<el-table-column
					prop="createDate"
					label="创建时间"
					width="200">
				</el-table-column>
				<el-table-column
					label="操作">
					<template slot-scope="scope">
						<el-button
							size="mini"
							@click="handleEdit(scope.$index, scope.row)">编辑
						</el-button>
						<el-button
							size="mini"
							type="danger"
							@click="handleDelete(scope.$index, scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
	</div>
</template>

<script>
export default {
	name: "PosMana",
	data() {
		return {
			pos: {
				name: ''
			},
			positions: []
		}
	},
	// 生命周期: 页面初始化时
	mounted() {
		this.initPositions();
	},
	methods: {
		// 页面初始化
		initPositions() {
			this.getRequest('/sys/basic/pos/').then(resp => {
				if (resp) {
					this.positions = resp
				}
			})
		},
		// 添加
		addPosition() {
			if (this.pos.name) {
				this.postRequest('/sys/basic/pos/', this.pos).then(resp => {
					if (resp) {
						this.initPositions();
						// 清空输入框信息
						this.pos.name = '';
					}
				})
			} else {
				this.$message.error('职位名称不可以为空哦 ^_^');
			}
		},
		// 编辑
		handleEdit(index, data) {

		},
		// 删除
		handleDelete(index, data) {
			this.$confirm('此操作将永久删除[' + data.name + ']职位, 是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.deleteRequest('/sys/basic/pos/' + data.id).then(resp => {
					if (resp) {
						this.initPositions();
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

<style>
.addPosInput {
	width: 300px;
	margin-right: 8px;
}

.posManaMain {
	margin-top: 10px;
}
</style>