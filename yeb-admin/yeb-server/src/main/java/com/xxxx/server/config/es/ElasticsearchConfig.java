package com.xxxx.server.config.es;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.cluster.metadata.AliasMetadata;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @Author dingc
 * @Date 2021/12/29 20:36
 */
public class ElasticsearchConfig {

	public static void main(String[] args) throws IOException {
		getConnection();
	}

	private static void getConnection() throws IOException {

		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		// 如果有账号密码, 在这里设置账号密码
		// credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("user","password"));

		// 创建rest client对象
		RestClientBuilder builder = RestClient.builder(new HttpHost("127.0.0.1", 9200))
				.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
					@Override
					public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
						return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
					}
				});

		RestHighLevelClient client = new RestHighLevelClient(builder);
		System.out.println(client.toString());

		GetRequest getRequest = new GetRequest("dingc", "doc", "100");
		GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
		System.out.println(response.getId());

		try {
			// 获取es连接中所有索引
			GetAliasesRequest request = new GetAliasesRequest();
			GetAliasesResponse getAliasesResponse = client.indices().getAlias(request, RequestOptions.DEFAULT);
			Map<String, Set<AliasMetadata>> map = getAliasesResponse.getAliases();
			Set<String> indices = map.keySet();
			for (String key : indices) {
				System.out.println("索引名称: " + key);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
