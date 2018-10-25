package cn.denua.v2ex.api;

import com.google.gson.JsonObject;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/*
 * v2ex search api
 *
 * 来自 https://www.sov2ex.com
 * Github: https://github.com/bynil/sov2ex/
 *
 *	 参数名称 		类型 	必须 	描述
 *    took 			int 	true 	搜索过程耗时(ms)
 *    timed_out 	bool 	true 	是否超时
 *    total 		int 	true 	命中主题总数
 *    hits 			array 	true 	主题列表
 *	  _source 		object 	true 	主题信息
 *    node 			int 	true 	节点 id
 *    replies 		int 	true 	回复数量
 *    created 		string 	true 	创建时间(UTC)
 *    member 		string 	true 	主题作者
 *    id 			int 	true 	主题 id
 *    title 		string 	true 	主题标题
 *    content 		string 	true 	主题内容
 *    highlight 	object 	false 	高亮
 *    title 		array 	false 	标题高亮（最多 1 条）
 *    content 		array 	false 	主题内容高亮（最多 1 条）
 *
 *    postscript_list.content 	array 	false 	附言高亮（最多 1 条）
 *    reply_list.content 		array 	false 	回复高亮（最多 1 条）

 * @author denua
 * @date 2018/10/21
 */
public interface SearchApi {

    /**
     * 搜索
     *
     * @param url  https://www.sov2ex.com/api/search
     * @param keywords 搜索关键词
     * @param from 与第一个结果的偏移
     * @param size  结果数量
     * @param sort  排序方式(权重|创建时间)[sumup|created]
     * @param order  升降序，sort 不为 sumup 时有效，(降序|升序)[0|1]
     * @param gte 最早发帖时间
     * @param lte 最晚发帖时间
     * @param node  指定节点
     * @param operate   关键词关系参数
     */
    @GET()
    Observable<JsonObject> search(@Url String url,
                              @Query("q")String keywords,
                              @Query("from")int from,
                              @Query("size")int size,
                              @Query("sort")String sort,
                              @Query("order")int order,
                              @Query("gte")int gte,
                              @Query("lte")int lte,
                              @Query("node")String node,
                              @Query("operate")String operate);

    @GET()
    Observable<JsonObject> search(@Url String url, @Query("q")String keywords);

    @GET()
    Observable<JsonObject> search(@Url String url, @QueryMap HashMap<String, String> param);
}
