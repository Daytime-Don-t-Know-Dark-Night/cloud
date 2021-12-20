<template>
	<div>
		<el-container>
			<el-header>Header</el-header>
			<el-container>
				<el-aside width="200px">
					<!-- 侧边栏 -->
					<el-menu router unique-opened>
						<el-submenu :index="index+''" v-for="(item, index) in routes"
						            :key="index"
						            v-if="!item.hidden">
							<template slot="title">
								<i :class="item.iconCls" style="color: #1accff; margin-right: 5px"></i>
								<span>{{ item.name }}</span>
							</template>
							<el-menu-item :index="children.path" v-for="(children, indexj) in item.children">
								{{ children.name }}
							</el-menu-item>
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
	computed: {
		routes(){
			return this.$store.state.routes;
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