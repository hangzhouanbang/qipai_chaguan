package com.anbang.qipai.chaguan.web.controller;

//@RunWith(SpringJUnit4ClassRunner.class) // 表示整合JUnit4进行测试
//@ContextConfiguration(locations = { "classpath:springmongodb.xml" }) // 加载spring配置文件
public class SpringMongodbTest {

	// @Autowired
	// private MongoTemplate mongoTemplate;
	//
	// @Test
	// public void test() {
	// long startTime = System.currentTimeMillis();
	// Query query = new Query();
	// String mapFunction = "function()
	// {emit(this.dayingjiaId,this.playerResultList);}";
	// String reduceFunction = "function(key,values) {var total=values.length;" +
	// "return {count:total}}";
	// MapReduceResults<BasicDBObject> mrr = mongoTemplate.mapReduce(query,
	// "gameHistoricalJuResult", mapFunction,
	// reduceFunction, BasicDBObject.class);
	// long endTime = System.currentTimeMillis();
	// System.out.println("use:" + (endTime - startTime) + "ms");
	// if (mrr != null) {
	// for (BasicDBObject db : mrr) {
	// System.out.println(db);
	// }
	// }
	// System.out.println("------------");
	// }
}
