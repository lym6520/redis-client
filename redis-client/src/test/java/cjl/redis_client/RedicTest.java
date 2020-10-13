package cjl.redis_client;

import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedicTest {
	
	public static void main(String args[]) throws InterruptedException{
		
		Map map =  new HashMap();
		map.put("master", "mymaster");
		
		List<String> sentinels = new ArrayList();
		sentinels.add("192.168.1.122:26379");
		sentinels.add("192.168.1.122:26380");
		sentinels.add("192.168.1.122:26381");
		
		map.put("sentinels", sentinels);
		map.put("password", "apex@123");
		map.put("timeout", "5000");

		List<Map> masters = new ArrayList();
		masters.add(map);

		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(4);
		config.setMinIdle(4);
		config.setMaxTotal(4);
		config.setMaxWaitMillis( 10000L);
		Redic redic = new Redic(masters, config);

		int i=1;

		redic.set("test", "val-"+i);
		
		System.out.println(redic.get("test"));

		
		List<RedicNode> nodes = redic.getRedicNodes();

		while(true) {
			i++;
			for(RedicNode node : nodes) {
				node.printJedisInfos();

				System.out.println(redic.get("test"));

				if(i == 8){
					node.printJedisInfos();
				}

				try{
					System.out.println("set:"+redic.set("test", "val-"+i));
				}catch (Exception e){
					e.printStackTrace();
				}

				if(i == 8){
					node.printJedisInfos();
				}
			}
			
			Thread.sleep(2000);
		}
		
	}
}
