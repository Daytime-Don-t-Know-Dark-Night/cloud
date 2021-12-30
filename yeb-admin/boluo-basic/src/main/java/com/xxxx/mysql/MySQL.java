package com.xxxx.mysql;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.execution.datasources.jdbc.JdbcOptionsInWrite;
import org.apache.spark.sql.execution.datasources.jdbc.JdbcUtils;
import org.apache.spark.sql.jdbc.JdbcDialect;
import org.apache.spark.sql.jdbc.JdbcDialects;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * MySQL数据库相关操作
 *
 * @Author dingc
 * @Date 2021/12/30 21:18
 */
public class MySQL {

	private static final Logger logger = LoggerFactory.getLogger(Mysql.class);

	public static void replace(Dataset<Row> ds, JdbcOptionsInWrite opts, String where) throws SQLException, ExecutionException, SparkException, InvocationTargetException {

		JdbcDialect dialect = JdbcDialects.get(opts.url());
		long max_allowed_packet = 4 * 1024 * 1024;
		long buffer_pool_size = 128 * 1024 * 1024;

		try (Connection conn = JdbcUtils.createConnectionFactory(opts).apply();
			 Statement statement = conn.createStatement()) {
			if (Objects.isNull(where) || where.equals("1=1")) {
				String sql = String.format("truncate table %s", opts.table());
				statement.executeUpdate(sql);
				System.out.println(sql);
			} else {
				String sql = String.format("delete from %s where %s", opts.table(), where);
				//countBefore -= statement.executeUpdate(sql);
				statement.executeUpdate(sql);
				System.out.println(sql);
			}

			try (ResultSet packetRes = statement.executeQuery("show global variables like 'max_allowed_packet'")) {
				while (packetRes.next()) {
					max_allowed_packet = packetRes.getLong("Value");
				}
			}

			try (ResultSet bufferRes = statement.executeQuery("show global variables like 'innodb_buffer_pool_size'")) {
				while (bufferRes.next()) {
					buffer_pool_size = bufferRes.getLong("Value");
				}
			}
		}

		StructType schema = ds.schema();
		String sql_ = JdbcUtils.getInsertStatement(opts.table(), schema, Option.empty(), true, dialect);

		// sql拼接时不使用''的类型
		List<DataType> specialType = ImmutableList.of(DataTypes.BooleanType, DataTypes.LongType, DataTypes.IntegerType);

		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage(); //堆内存使用情况
		long totalMemorySize = memoryUsage.getInit(); //初始的总内存
		long maxMemorySize = memoryUsage.getMax(); //最大可用内存
		long usedMemorySize = memoryUsage.getUsed(); //已使用的内存

		long partNum = Math.min(Math.round(0.6 * max_allowed_packet), Math.round(maxMemorySize * 0.06));
		long commitCount = Math.round(0.1 * buffer_pool_size);

		ds.foreachPartition((rows) -> {

			try (Connection conn = JdbcUtils.createConnectionFactory(opts).apply();
				 Statement statement = conn.createStatement()) {
				conn.setAutoCommit(false);

				int numFields = schema.fields().length;
				int partCount = 0, executeCount = 0, sumCount = 0;

				StringBuilder sb = new StringBuilder((int) partNum + 1000);
				String prev = sql_.substring(0, sql_.indexOf("(?"));
				sb.append(prev);

				while (rows.hasNext()) {
					Row row = rows.next();
					StringBuilder group = new StringBuilder("(");
					for (int i = 0; i < numFields; i++) {
						DataType type = schema.apply(i).dataType();

						if (row.isNullAt(i)) {
							// null值处理
							group.append("null,");
						} else if (specialType.contains(type)) {
							// 判断该类型数据是否需要''
							Object tmp = row.getAs(i);
							group.append(String.format("%s,", tmp));
						} else if (type == DataTypes.StringType) {
							// 如果该类型为字符串类型且包含', 则对'进行转义
							String tmp = row.getAs(i);
							group.append(String.format("'%s',", tmp.replaceAll("'", "''")));
						} else {
							Object tmp = row.getAs(i);
							group.append(String.format("'%s',", tmp));
						}
					}
					group.delete(group.length() - 1, group.length());
					group.append("),");
					sb.append(group);

					partCount++;
					if (sb.length() >= partNum) {
						executeCount += sb.length();    // 每执行一次, 累计 + sb.length
						String ex_sql = sb.substring(0, sb.length() - 1);
						statement.executeLargeUpdate(ex_sql);
						sb.setLength(0);
						sb.append(prev);
						partCount = 0;
						logger.info("execute执行次数: {}", sumCount++);
					}

					// 上面每执行一次, 累计 + max_allowed_packet, 累计加到缓冲池的70%, 提交
					if (executeCount >= commitCount) {
						logger.info("commit执行时间: {}", Instant.now());
						conn.commit();
						executeCount = 0;
					}
				}

				// 剩余数量不足partNum的
				if (partCount > 0) {
					String ex_sql = StringUtils.chop(sb.toString());
					statement.executeUpdate(ex_sql);
				}
				conn.commit();
			}

		});
	}
}
