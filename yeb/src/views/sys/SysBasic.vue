<template>
	<div>
		<el-tabs v-model="activeName" type="card">
			<el-tab-pane label="部门管理" name="DepMana">部门管理</el-tab-pane>
			<el-tab-pane label="职位管理" name="EcMana">职位管理</el-tab-pane>
			<el-tab-pane label="职称管理" name="JoblevelMana">职称管理</el-tab-pane>
			<el-tab-pane label="奖惩规则" name="PermissMana">奖惩规则</el-tab-pane>
			<el-tab-pane label="权限组" name="PosMana">权限组</el-tab-pane>
		</el-tabs>
	</div>
</template>

<script>
import DepMana from "@/components/sys/basic/DepMana";
import EcMana from "@/components/sys/basic/EcMana";
import JoblevelMana from "@/components/sys/basic/JoblevelMana";
import PermissMana from "@/components/sys/basic/PermissMana";
import PosMana from "@/components/sys/basic/PosMana";

export default {
	name: "SysBasic",
	data() {
		return {
			activeName: 'DepMana'
		}
	},
	components: {
		DepMana,
		EcMana,
		JoblevelMana,
		PermissMana,
		PosMana
	}
}
</script>

<style scoped>

</style>