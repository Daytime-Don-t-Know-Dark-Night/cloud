<template>
	<div>
		<el-container>
			<el-header>Header</el-header>
			<el-container>
				<el-aside width="200px">
					<!-- 侧边栏 -->
					<el-menu @select="menuClick">
						<el-submenu index="1">
							<template slot="title"><i class="el-icon-location"></i>导航一</template>
							<el-menu-item index="/test1">选项1</el-menu-item>
							<el-menu-item index="/test2">选项2</el-menu-item>
						</el-submenu>
					</el-menu>

				</el-aside>
				<el-main>
					<router-view/>
				</el-main>
			</el-container>
		</el-container>
	</div>
</template>

<script>
export default {
	name: "Home",
	methods: {
		menuClick(index) {
			this.$router.push(index);
		}
	}
}
</script>

<style scoped>
.el-header, .el-footer {
	background-color: #B3C0D1;
	color: #333;
	text-align: center;
	line-height: 60px;
}

.el-aside {
	background-color: #D3DCE6;
	color: #333;
	text-align: center;
	line-height: 200px;
}

.el-main {
	background-color: #E9EEF3;
	color: #333;
	text-align: center;
	line-height: 160px;
}

body > .el-container {
	margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
	line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
	line-height: 320px;
}
</style>